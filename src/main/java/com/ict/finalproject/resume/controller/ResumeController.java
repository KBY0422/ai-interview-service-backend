package com.ict.finalproject.resume.controller;

import com.aspose.pdf.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ict.finalproject.common.security.CustomUserDetails;
import com.ict.finalproject.resume.service.ResumeService;
import com.ict.finalproject.resume.util.ResumeUtil;
import com.ict.finalproject.resume.vo.ResumeTokenVO;
import com.ict.finalproject.resume.vo.ResumeVO;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/resume") // ← 여기 경로 변경 완료
@RequiredArgsConstructor
public class ResumeController {

    @Value("${api.gpt.key}")
    private String apiKey;

    private final ResumeService resumeService;


    private String extractOutputText(Response gptResponse) {
        return gptResponse.output().stream()
                .filter(o -> o.message().isPresent()) // assistant 메시지 있는 것만
                .findFirst()
                .orElseThrow(() -> new RuntimeException("GPT 응답에서 message() 를 찾을 수 없습니다"))
                .message().get()
                .content().get(0)
                .outputText().orElseThrow(() -> new RuntimeException("GPT outputText 없음"))
                .text();
    }

    @PostMapping("/analyze")
    public Object analyzeResume(@RequestParam("file") MultipartFile file) throws Exception {


        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        Object principal = authentication.getPrincipal();


        // 🔥 핵심
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Integer mIdx = userDetails.getMIdx();   // ← 진짜 m_idx
        String mId = userDetails.getUsername(); // ← m_id (필요하면)





        // PDF 이미지 추출
        List<String> images = ResumeUtil.extractImage(file);

        // 1) PDF 텍스트 추출
        String text = ResumeUtil.extractPdfText(file);


        // 2) GPT 요청 프롬프트
        String prompt = """
                당신은 it 직종 채용 담당자입니다.
                
                아래 이력서를 기반으로 총 점수(전체 피드백,개선사항),항목별(문장구조,경력/프로젝트,기술스택,자기소개) 점수, 장점, 개선점을 JSON으로 평가하세요.
                총 점수에 대한 피드백은 큰 틀에서 중요한 사항만 넣고 나머지는 항목별 피드백에서 상세하게 다루세요.
                JSON 외에는 절대 출력하지마세요.
                
                출력 JSON 형식:
                [
                  {"title": "총 점수", "score": 0~100, "feedback": ["..."], "improvements": ["..."]},
                  {"title": "문장 구조", "score": 0~100, "feedback": ["..."], "improvements": ["..."]},
                  {"title": "경력/프로젝트", "score": 0~100, "feedback": ["..."], "improvements": ["..."]},
                  {"title": "기술 스택", "score": 0~100, "feedback": ["..."], "improvements": ["..."]},
                  {"title": "자기소개", "score": 0~100, "feedback": ["..."], "improvements": ["..."]},
                ]
                
                분석 대상 이력서:
                """ + text;


        // 3) OpenAI Client 생성
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();

        // 4) GPT 호출
        ResponseCreateParams params = ResponseCreateParams.builder()
                .model("gpt-5-nano")
                .input(prompt)
                .build();


        Response response = client.responses().create(params);

        Integer totalToken= Math.toIntExact(response.usage().get().totalTokens());


        // 5) 결과 JSON 그대로 반환 → React 직접 사용가능
        String outputText = response.output().get(1)
                .message().orElseThrow()
                .content().get(0)
                .outputText().orElseThrow()     // Optional unwrap
                .text();                // ← 최종 JSON!!             // ← 요게 정답!

        System.out.println(response.output());

        System.out.println("두번째 확인용@@@@@@@@@@@@@@" + response.output().get(1).message());


        // 6) JSON → Java List 변환 (React가 그대로 읽을 수 있는 형태)
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> analysis = mapper.readValue(outputText, List.class);
        ResumeVO resumeVO=new ResumeVO();
        resumeVO.setMIdx(mIdx);


        resumeService.plusCount(resumeVO);
        Integer rIdx = resumeVO.getRIdx(); // 🔥 여기서 바로 사용 가능

        ResumeTokenVO resumeTokenVO=new ResumeTokenVO();
        resumeTokenVO.setTTotal(totalToken);
        resumeTokenVO.setRIdx(rIdx);
        resumeTokenVO.setTPurpose("RESUME");
        resumeTokenVO.setMIdx(mIdx);

        int token = resumeService.insertResumeToken(resumeTokenVO);

        return Map.of("analysis", analysis,
                "images",images);
    }



    @PostMapping("/pdf_token")
    public void token(@RequestParam("t_total") int t_total) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Integer mIdx = userDetails.getMIdx(); // 🔥 여기서만 꺼낸다

        // 🔥 여기서 rIdx를 DB에서 조회
        Integer rIdx = resumeService.findLatestResumeIdx(mIdx);
        if (rIdx == null) {
            throw new IllegalStateException("최근 이력서 분석이 없습니다.");
        }


        ResumeTokenVO resumeTokenVO = new ResumeTokenVO();
        resumeTokenVO.setMIdx(mIdx);
        resumeTokenVO.setRIdx(rIdx);
        resumeTokenVO.setTTotal(t_total);
        resumeTokenVO.setTPurpose("RESUME"); // 구분용

        int updated = resumeService.updateResumeToken(resumeTokenVO);
        if (updated == 0) {
            throw new IllegalStateException("RESUME 토큰 행이 존재하지 않습니다.");
        }
    }


}