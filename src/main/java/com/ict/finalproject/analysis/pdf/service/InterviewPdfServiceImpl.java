package com.ict.finalproject.analysis.pdf.service;

import com.ict.finalproject.analysis.pdf.mapper.InterviewPdfMapper;
import com.ict.finalproject.analysis.pdf.vo.InterviewPdfDataVO;
import com.ict.finalproject.analysis.pdf.vo.InterviewPdfVO;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewPdfServiceImpl implements InterviewPdfService {

    private final InterviewPdfMapper interviewPdfMapper;


    @Value("${file.pdf-dir}")
    private String pdfDir;

    private static final DateTimeFormatter FILE_NAME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    public void createPdf(int sIdx) {

        InterviewPdfDataVO d = interviewPdfMapper.selectPdfData(sIdx);
        if (d == null) {
            throw new IllegalStateException("PDF 생성용 분석 데이터가 존재하지 않습니다. sIdx=" + sIdx);
        }

        LocalDateTime now = LocalDateTime.now();

        try {
            Path dirPath = Paths.get(pdfDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String fileName = "analysis_session_" + sIdx + "_" + now.format(FILE_NAME_FORMATTER) + ".pdf";
            Path filePath = dirPath.resolve(fileName);

            // 기존 active 전부 비활성화 (중복 active 방지)
            interviewPdfMapper.deactivateActiveBySession(sIdx);
            log.info("[PDF] deactivateActiveBySession 실행 sIdx={}", sIdx);

            generatePdfFile(d, filePath);

            InterviewPdfVO vo = new InterviewPdfVO();
            vo.setFSIdx(sIdx);
            vo.setFTitle(fileName);
            vo.setFPath(filePath.toString());
            vo.setFRegdate(now);
            vo.setFActive(1);
            vo.setFDownloadCount(0);

            interviewPdfMapper.insertPdf(vo);

        } catch (Exception e) {
            log.error("PDF 생성 실패", e);
            throw new IllegalStateException("PDF 생성 실패");
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadPdfResponse(int sIdx) {
        log.info("[PDF DOWNLOAD] 요청 sIdx = {}", sIdx);
        // ✅ 다운로드는 생성하지 않는다.
        InterviewPdfVO vo = interviewPdfMapper.findActiveBySessionIdx(sIdx);
        if (vo == null) {
            throw new IllegalStateException("다운로드 가능한 PDF가 없습니다. 먼저 PDF를 생성하세요.");
        }

        // 로그
        log.info("[PDF CHECK] dbPath = {}", vo.getFPath());
        Path path = Paths.get(vo.getFPath());

        // 로그
        log.info("[PDF CHECK] exists = {}", Files.exists(path));
        log.info("[PDF CHECK] absolute = {}", path.toAbsolutePath());
        if (!Files.exists(path)) {
            throw new IllegalStateException("PDF 파일이 존재하지 않습니다.");
        }

        try {
            byte[] bytes = Files.readAllBytes(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            String fileName = URLEncoder.encode(vo.getFTitle(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            headers.setContentDisposition(
                    ContentDisposition.attachment().filename(fileName).build()
            );
            headers.setContentLength(bytes.length);

            interviewPdfMapper.increaseDownloadCount(sIdx);

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("PDF 다운로드 오류", e);
            throw new IllegalStateException("PDF 다운로드 중 오류가 발생했습니다.");
        }
    }

    @Override
    public InterviewPdfVO getPdfInfo(int sIdx) {
        // ✅ 시간 제한 제거: active만 있으면 반환
        return interviewPdfMapper.findActiveBySessionIdx(sIdx);
    }

    // ======================= PDF 생성(디자인 복구) =======================
    private void generatePdfFile(InterviewPdfDataVO d, Path filePath) throws Exception {

        // 컬러 팔레트(기존 디자인 유지)
        Color primary = new Color(120, 81, 169);   // 퍼플
        Color borderSoft = new Color(220, 220, 220);
        Color bgSoft = new Color(248, 248, 252);

        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {

            Document document = new Document(PageSize.A4, 46, 46, 54, 54);
            PdfWriter writer = PdfWriter.getInstance(document, fos);

            BaseFont bf = loadKoreanBaseFont();
            writer.setPageEvent(new PdfFooter(bf));

            Font h1 = new Font(bf, 18, Font.BOLD, primary);
            Font titleFont = new Font(bf, 12, Font.BOLD, primary);
            Font bodyFont = new Font(bf, 10, Font.NORMAL, new Color(30, 30, 30));
            Font scoreFont = new Font(bf, 16, Font.BOLD, primary);

            document.open();

            // HEADER
            Paragraph header = new Paragraph("AI 면접 분석 리포트", h1);
            header.setSpacingAfter(8f);
            document.add(header);

            Paragraph info = new Paragraph(
                    "지원자  " + safe(d.getMName())
                            + "   │   직무  " + safe(d.getSJob())
                            + "   │   유형  " + safe(d.getSType())
                            + "   │   분석일  " + (d.getSaCreateAt() != null ? d.getSaCreateAt().toString() : "-"),
                    bodyFont
            );
            info.setSpacingBefore(6f);
            info.setSpacingAfter(16f);
            document.add(info);

            // MAIN SCORE
            addMainScoreCard(document, d.getSaTotalScore(), bf, primary, borderSoft, bgSoft);

            // SUB SCORES (3개)
            PdfPTable subScore = new PdfPTable(3);
            subScore.setWidthPercentage(100);
            subScore.setSpacingAfter(14f);
            subScore.setWidths(new float[]{1f, 1f, 1f});

            addScoreCard(subScore, "기술", d.getSaTechScore(), scoreFont, bf, borderSoft, bgSoft);
            addScoreCard(subScore, "논리", d.getSaLogicScore(), scoreFont, bf, borderSoft, bgSoft);
            addScoreCard(subScore, "소프트", d.getSaSoftScore(), scoreFont, bf, borderSoft, bgSoft);

            document.add(subScore);

            // ANALYSIS GRID (2열)
            PdfPTable grid = new PdfPTable(2);
            grid.setWidthPercentage(100);
            grid.setSpacingAfter(14f);
            grid.setWidths(new float[]{1f, 1f});

            addMagazineBox(grid, "면접 요약", d.getSaSummary(), titleFont, bodyFont, borderSoft);
            addMagazineBox(grid, "지원자 강점", d.getSaStrengths(), titleFont, bodyFont, borderSoft);
            addMagazineBox(grid, "개선 필요", d.getSaWeaknesses(), titleFont, bodyFont, borderSoft);
            addMagazineBox(
                    grid,
                    "난이도 평균",
                    "평균 난이도 : " + (d.getSaLevelAvg() != null ? d.getSaLevelAvg() : "-") + " / 5",
                    titleFont,
                    bodyFont,
                    borderSoft
            );

            document.add(grid);

            // FINAL FEEDBACK (하이라이트 박스)
            addHighlightBox(
                    document,
                    "종합 피드백",
                    d.getSaFeedback(),
                    titleFont,
                    bodyFont,
                    primary
            );

            document.close();
        }
    }

    // ===================== COMPONENTS =====================
    private void addMainScoreCard(
            Document document,
            Integer score,
            BaseFont bf,
            Color primary,
            Color border,
            Color background
    ) throws DocumentException {

        Font label = new Font(bf, 11, Font.BOLD);
        Font scoreFont = new Font(bf, 26, Font.BOLD, primary);

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setSpacingAfter(12f);

        PdfPCell cell = new PdfPCell();
        cell.setPadding(16f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(background);
        cell.setBorderColor(primary);
        cell.setBorderWidth(1.5f);

        cell.addElement(new Paragraph("총점", label));
        String text = (score != null ? score + " / 100" : "- / 100");
        cell.addElement(new Paragraph(text, scoreFont));

        table.addCell(cell);
        document.add(table);
    }

    private void addScoreCard(
            PdfPTable table,
            String title,
            Integer score,
            Font scoreFont,
            BaseFont bf,
            Color border,
            Color background
    ) {
        Font label = new Font(bf, 10);

        PdfPCell cell = new PdfPCell();
        cell.setPadding(10f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(background);
        cell.setBorderColor(border);

        cell.addElement(new Paragraph(title, label));
        cell.addElement(new Paragraph((score != null ? score + " / 100" : "- / 100"), scoreFont));

        table.addCell(cell);
    }

    private void addMagazineBox(
            PdfPTable table,
            String title,
            String text,
            Font titleFont,
            Font bodyFont,
            Color border
    ) {

        PdfPCell cell = new PdfPCell();
        cell.setPadding(12f);
        cell.setBorderColor(border);

        Paragraph t = new Paragraph(title, titleFont);
        t.setSpacingAfter(8f);
        cell.addElement(t);

        Paragraph body = new Paragraph(clean(text), bodyFont);
        body.setLeading(17f);
        cell.addElement(body);

        table.addCell(cell);
    }

    private void addHighlightBox(
            Document document,
            String title,
            String text,
            Font titleFont,
            Font bodyFont,
            Color color
    ) throws DocumentException {

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell();
        cell.setPadding(14f);
        cell.setBorderColor(color);
        cell.setBorderWidth(1.5f);

        Paragraph t = new Paragraph(title, titleFont);
        t.setSpacingAfter(10f);
        cell.addElement(t);

        Paragraph body = new Paragraph(clean(text), bodyFont);
        body.setLeading(18f);
        cell.addElement(body);

        table.addCell(cell);
        document.add(table);
    }

    // ===================== FOOTER =====================
    private static class PdfFooter extends PdfPageEventHelper {

        private final BaseFont bf;

        PdfFooter(BaseFont bf) {
            this.bf = bf;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            Font font = new Font(bf, 9, Font.NORMAL, new Color(150, 150, 150));

            Phrase footer = new Phrase(
                    "AI Mock Interview Report  |  ICT Final Project",
                    font
            );

            ColumnText.showTextAligned(
                    writer.getDirectContent(),
                    Element.ALIGN_CENTER,
                    footer,
                    (document.right() + document.left()) / 2,
                    document.bottom() - 18,
                    0
            );
        }
    }

    // ===================== UTIL =====================
    private BaseFont loadKoreanBaseFont() throws Exception {

        // 너 프로젝트 기준: src/main/resources/font/NotoSansKR-Regular.ttf
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("font/NotoSansKR-Regular.ttf")) {

            if (is == null) {
                // 여기 메시지 “경로/폴더명” 정확하게 박아둠 (헷갈리지 말라고)
                throw new IllegalStateException(
                        "폰트 리소스를 찾을 수 없습니다: src/main/resources/font/NotoSansKR-Regular.ttf"
                );
            }

            byte[] bytes = is.readAllBytes();

            return BaseFont.createFont(
                    "NotoSansKR-Regular.ttf",
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED,
                    false,
                    bytes,
                    null
            );
        }
    }

    private String clean(String t) {
        if (t == null) return "-";
        return t.replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .replace(",", "\n ");
    }

    private String safe(String v) {
        return v == null ? "-" : v;
    }
}
