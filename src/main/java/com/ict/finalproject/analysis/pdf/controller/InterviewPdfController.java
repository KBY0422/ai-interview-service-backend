package com.ict.finalproject.analysis.pdf.controller;

import com.ict.finalproject.analysis.pdf.service.InterviewPdfService;
import com.ict.finalproject.analysis.pdf.vo.InterviewPdfVO;
import com.ict.finalproject.common.vo.DataVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/analysis/pdf")
@RequiredArgsConstructor
public class InterviewPdfController {

    private final InterviewPdfService service;

    /**
     * PDF 생성
     * - 항상 새로 생성 (기존 active는 비활성화)
     */
    @PostMapping("/create/{sIdx}")
    public DataVO<Void> create(@PathVariable int sIdx) {
        service.createPdf(sIdx);
        return DataVO.success(null, "PDF 생성 완료");
    }

    /**
     * PDF 다운로드
     * - 생성하지 않음 (active 없으면 실패)
     */
    @GetMapping("/download/{sIdx}")
    public ResponseEntity<byte[]> download(@PathVariable int sIdx) {
        log.error("🔥🔥🔥 DOWNLOAD CONTROLLER HIT sIdx={}", sIdx);
        return service.downloadPdfResponse(sIdx);
    }

    /**
     * PDF 메타 조회 (프론트 버튼 활성화 용)
     */
    @GetMapping("/info/{sIdx}")
    public DataVO<InterviewPdfVO> info(@PathVariable int sIdx) {
        InterviewPdfVO vo = service.getPdfInfo(sIdx);
        if (vo == null) {
            return DataVO.fail("PDF 없음");
        }
        return DataVO.success(vo, "SUCCESS");
    }
}
