package com.ict.finalproject.analysis.result.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ict.finalproject.analysis.result.dto.InterviewResultResponseDTO;
import com.ict.finalproject.analysis.result.mapper.InterviewQnaMapper;
import com.ict.finalproject.analysis.result.mapper.InterviewQuestionAnalysisMapper;
import com.ict.finalproject.analysis.result.mapper.InterviewSessionAnalysisMapper;
import com.ict.finalproject.analysis.result.vo.InterviewQnaVO;
import com.ict.finalproject.analysis.result.vo.InterviewQuestionAnalysisVO;
import com.ict.finalproject.analysis.result.vo.InterviewSessionAnalysisVO;
import com.ict.finalproject.common.config.ApiProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewSessionAnalysisServiceImpl
        implements InterviewSessionAnalysisService {

    private final InterviewSessionAnalysisMapper sessionAnalysisMapper;
    private final InterviewQnaMapper interviewQnaMapper;
    private final InterviewQuestionAnalysisMapper questionAnalysisMapper;
    private final ApiProperties apiProperties;
    private final ObjectMapper mapper; // Bean 으로 주입

    /* 세션 분석 결과 단건 조회 */
    @Override
    public InterviewSessionAnalysisVO getSessionAnalysis(int sIdx, int mIdx) {

        InterviewSessionAnalysisVO vo =
                sessionAnalysisMapper.selectBySessionIdx(sIdx, mIdx);

        if (vo == null) {
            throw new IllegalStateException("면접 분석 결과가 존재하지 않습니다.");
        }
        return vo;
    }

    /* 세션 기준 분석 메인 처리 */
    @Override
    public InterviewResultResponseDTO getInterviewResult(int sIdx, int mIdx) {

        // 1. 이미 분석된 세션이면 DB 기반 결과만 반환 (사용자 기준)
        InterviewSessionAnalysisVO exist =
                sessionAnalysisMapper.selectBySessionIdx(sIdx, mIdx);

        if (exist != null) {
            InterviewAnalysisResult r =
                    buildAnalysisResultFromSession(exist);
            return buildResponseDto(sIdx, r, exist.getSaCreateAt());
        }

        // 2. 세션 직종 조회
        String job =
                sessionAnalysisMapper.selectJobBySessionIdx(sIdx);

        if (job == null || job.isBlank()) {
            throw new IllegalStateException("세션 직종 정보가 존재하지 않습니다.");
        }

        // 3. 질문/답변 목록 조회
        List<InterviewQnaVO> qnaList =
                interviewQnaMapper.selectBySessionIdx(sIdx);

        if (qnaList == null || qnaList.isEmpty()) {
            throw new IllegalStateException("질문 답변 데이터가 존재하지 않습니다.");
        }

        // 4. 질문과 답변을 쌍으로 구성
        List<QnaPair> qnaPairs = buildQnaPairs(qnaList);

        // 5. GPT 분석 수행
        InterviewAnalysisResult result =
                analyzeByGpt(job, qnaPairs);

        // 6. 세션 분석 결과 저장 (강점/약점/피드백 포함) + mIdx 포함
        saveSessionAnalysis(sIdx, mIdx, result);

        // 7. 질문 단위 분석 결과 저장
        saveQuestionAnalysis(qnaPairs, result);

        // ✅ [추가] 세션 단위 토큰 사용량 로그 저장 (DB: token_usage_log)
        insertSessionTokenUsage(mIdx, sIdx, result.getTotalTokens());

        // 8. 응답 DTO 반환
        return buildResponseDto(sIdx, result, LocalDateTime.now());
    }

    // ✅ [추가] token_usage_log 저장 전용 내부 VO
    @Data
    public static class TokenUsageLogVO {
        private Integer tMIdx;
        private Integer tSIdx;
        private Integer tTotal;
        private String tPurpose;
    }

    // ✅ [추가] token_usage_log INSERT 수행
    private void insertSessionTokenUsage(int mIdx, int sIdx, Integer totalTokens) {
        TokenUsageLogVO vo = new TokenUsageLogVO();
        vo.setTMIdx(mIdx);
        vo.setTSIdx(sIdx);
        vo.setTTotal(totalTokens);
        vo.setTPurpose("SESSION");
        sessionAnalysisMapper.updateSessionTokenUsage(vo);
    }

    /* 질문과 답변을 하나의 쌍으로 구성 */
    private List<QnaPair> buildQnaPairs(List<InterviewQnaVO> qnaList) {

        List<QnaPair> pairs = new ArrayList<>();
        String question = null;
        int qIdx = 0;

        for (InterviewQnaVO qna : qnaList) {

            if (qna.getQType() == 0) {
                question = qna.getQContent();
                qIdx = qna.getQIdx();
            } else if (qna.getQType() == 1 && question != null) {
                pairs.add(new QnaPair(question, qna.getQContent(), qIdx));
                question = null;
            }
        }
        return pairs;
    }

    /* GPT 분석 실행 */
    private InterviewAnalysisResult analyzeByGpt(
            String job,
            List<QnaPair> qnaPairs
    ) {

        String prompt = buildPrompt(job, qnaPairs);
        String response = callOpenAi(prompt);

        if (response == null || response.isBlank()) {
            throw new IllegalStateException("GPT 응답이 비어 있습니다.");
        }

        // ✅ [추가] OpenAI usage.total_tokens 추출
        int totalTokens = extractTotalTokens(response);

        InterviewAnalysisResult parsed = parseGptResponse(response);
        parsed.setTotalTokens(totalTokens);

        return parsed;
    }

    /* 프롬프트 생성 (DB 타입에 맞춘 규칙 포함) */
    private String buildPrompt(String job, List<QnaPair> qnaPairs) {

        StringBuilder sb = new StringBuilder();

        sb.append("당신은 ").append(job).append(" 직무 전문 면접관입니다.\n");
        sb.append("아래 면접 질문과 답변을 분석하여 정중한 존댓말로 평가해 주세요.\n");
        sb.append("응답은 반드시 JSON 형식으로만 출력해야 합니다.\n\n");

        sb.append("""
            출력 규칙
            1. totalScore, techScore, logicScore, softScore는 0~100 사이의 정수입니다.
            2. levelAvg는 1.00 ~ 5.00 범위의 소수점 둘째 자리까지 값입니다.
            3. wrongAnswerRate는 0.00 ~ 100.00 범위의 퍼센트 값입니다.
            4. strengths, weaknesses, feedback은 빈 배열이면 안 되며 최소 2개 이상 작성합니다.
            5. summary, strengths, weaknesses, feedback은 모두 존댓말로 작성합니다.
            6. feedback은 실제 개선 행동 기준으로 작성합니다.

            JSON 형식
            {
              "totalScore": number,
              "techScore": number,
              "logicScore": number,
              "softScore": number,
              "levelAvg": number,
              "wrongAnswerRate": number,
              "summary": string,
              "strengths": [string],
              "weaknesses": [string],
              "feedback": [string]
            }
            """);

        int i = 1;
        for (QnaPair p : qnaPairs) {
            sb.append(i++).append(". 질문: ").append(p.question).append("\n");
            sb.append("답변: ").append(p.answer).append("\n\n");
        }

        return sb.toString();
    }

    /* OpenAI API 호출 */
    private String callOpenAi(String prompt) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiProperties.getGpt().getKey());

            ObjectNode body = mapper.createObjectNode();
            body.put("model", apiProperties.getGpt().getModel());
            body.put("temperature", 0.3);

            var messages = mapper.createArrayNode();
            var user = mapper.createObjectNode();
            user.put("role", "user");
            user.put("content", prompt);
            messages.add(user);
            body.set("messages", messages);

            HttpEntity<String> request =
                    new HttpEntity<>(mapper.writeValueAsString(body), headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            "https://api.openai.com/v1/chat/completions",
                            request,
                            String.class
                    );

            return response.getBody();

        } catch (Exception e) {
            throw new IllegalStateException("OpenAI API 호출 실패", e);
        }
    }

    // ✅ [추가] OpenAI 응답 JSON에서 usage.total_tokens 추출
    private int extractTotalTokens(String responseJson) {
        try {
            JsonNode root = mapper.readTree(responseJson);
            JsonNode usage = root.path("usage");
            return usage.path("total_tokens").asInt(0);
        } catch (Exception e) {
            // 토큰 파싱 실패해도 분석 기능 자체는 깨지면 안 되므로 0 처리
            return 0;
        }
    }

    /* GPT 응답 JSON 파싱 */
    private InterviewAnalysisResult parseGptResponse(String response) {

        try {
            JsonNode root = mapper.readTree(response);
            String content =
                    root.at("/choices/0/message/content").asText();

            int s = content.indexOf("{");
            int e = content.lastIndexOf("}");

            if (s == -1 || e == -1) {
                throw new IllegalStateException("GPT 응답이 JSON 형식이 아닙니다.");
            }

            String json = content.substring(s, e + 1);

            return mapper.readValue(
                    json,
                    InterviewAnalysisResult.class
            );

        } catch (Exception e) {
            throw new IllegalStateException("GPT 응답 파싱 실패", e);
        }
    }

    /* 세션 분석 결과 저장 (강점/약점/피드백 포함) */
    private void saveSessionAnalysis(
            int sIdx,
            int mIdx,
            InterviewAnalysisResult r
    ) {

        try {
            InterviewSessionAnalysisVO vo =
                    new InterviewSessionAnalysisVO();

            vo.setSaSIdx(sIdx);
            vo.setSaMIdx(mIdx);

            vo.setSaTotalScore(r.getTotalScore());
            vo.setSaTechScore(r.getTechScore());
            vo.setSaLogicScore(r.getLogicScore());
            vo.setSaSoftScore(r.getSoftScore());
            vo.setSaLevelAvg(BigDecimal.valueOf(r.getLevelAvg()));
            vo.setSaWrongAnswerRate(
                    BigDecimal.valueOf(r.getWrongAnswerRate()));
            vo.setSaSummary(r.getSummary());

            // 배열을 JSON string 으로 저장
            vo.setSaStrengths(
                    mapper.writeValueAsString(
                            r.getStrengths() == null ? List.of() : r.getStrengths()
                    )
            );
            vo.setSaWeaknesses(
                    mapper.writeValueAsString(
                            r.getWeaknesses() == null ? List.of() : r.getWeaknesses()
                    )
            );
            vo.setSaFeedback(
                    mapper.writeValueAsString(
                            r.getFeedback() == null ? List.of() : r.getFeedback()
                    )
            );

            vo.setSaCreateAt(LocalDateTime.now());

            sessionAnalysisMapper.insertSessionAnalysis(vo);

        } catch (Exception e) {
            throw new IllegalStateException("세션 분석 저장 실패", e);
        }
    }

    /*  질문 단위 분석 결과 저장 (여기 시그니처가 호출부와 반드시 일치해야 함) */
    private void saveQuestionAnalysis(
            List<QnaPair> qnaPairs,
            InterviewAnalysisResult r
    ) {

        for (QnaPair p : qnaPairs) {

            InterviewQuestionAnalysisVO qa =
                    new InterviewQuestionAnalysisVO();

            qa.setQaQIdx(p.qIdx);
            qa.setQaTotalScore(r.getTotalScore());
            qa.setQaTechScore(r.getTechScore());
            qa.setQaLogicScore(r.getLogicScore());
            qa.setQaSoftScore(r.getSoftScore());
            qa.setQaFeedbackSummary(r.getSummary());
            qa.setQaCreateAt(LocalDateTime.now());

            questionAnalysisMapper.insertQuestionAnalysis(qa);
        }
    }

    /* 기존 세션 분석 결과 → 내부 모델 복원 */
    private InterviewAnalysisResult buildAnalysisResultFromSession(
            InterviewSessionAnalysisVO vo
    ) {

        InterviewAnalysisResult r =
                new InterviewAnalysisResult();

        r.setTotalScore(vo.getSaTotalScore());
        r.setTechScore(vo.getSaTechScore());
        r.setLogicScore(vo.getSaLogicScore());
        r.setSoftScore(vo.getSaSoftScore());
        r.setLevelAvg(vo.getSaLevelAvg().doubleValue());
        r.setWrongAnswerRate(
                vo.getSaWrongAnswerRate().doubleValue());
        r.setSummary(vo.getSaSummary());

        try {
            r.setStrengths(
                    vo.getSaStrengths() == null
                            ? List.of()
                            : mapper.readValue(
                            vo.getSaStrengths(),
                            new TypeReference<List<String>>() {}
                    )
            );
            r.setWeaknesses(
                    vo.getSaWeaknesses() == null
                            ? List.of()
                            : mapper.readValue(
                            vo.getSaWeaknesses(),
                            new TypeReference<List<String>>() {}
                    )
            );
            r.setFeedback(
                    vo.getSaFeedback() == null
                            ? List.of()
                            : mapper.readValue(
                            vo.getSaFeedback(),
                            new TypeReference<List<String>>() {}
                    )
            );

        } catch (Exception e) {
            throw new IllegalStateException("세션 분석 JSON 복원 실패", e);
        }

        return r;
    }

    /* 응답 DTO 생성 */
    private InterviewResultResponseDTO buildResponseDto(
            int sIdx,
            InterviewAnalysisResult r,
            LocalDateTime createdAt
    ) {

        InterviewResultResponseDTO dto =
                new InterviewResultResponseDTO();

        dto.setSIdx(sIdx);

        InterviewResultResponseDTO.Scores scores =
                new InterviewResultResponseDTO.Scores();
        scores.setTotal(r.getTotalScore());
        scores.setTech(r.getTechScore());
        scores.setLogic(r.getLogicScore());
        scores.setSoft(r.getSoftScore());

        InterviewResultResponseDTO.Metrics metrics =
                new InterviewResultResponseDTO.Metrics();
        metrics.setLevelAvg(r.getLevelAvg());
        metrics.setWrongAnswerRate(r.getWrongAnswerRate());

        dto.setScores(scores);
        dto.setMetrics(metrics);
        dto.setSummary(r.getSummary());
        dto.setStrengths(r.getStrengths());
        dto.setWeaknesses(r.getWeaknesses());
        dto.setFeedback(r.getFeedback());
        dto.setCreatedAt(createdAt);

        return dto;
    }

    /* Q&A 묶음 모델 */
    private static class QnaPair {
        public final String question;
        public final String answer;
        public final int qIdx;

        public QnaPair(String q, String a, int idx) {
            this.question = q;
            this.answer = a;
            this.qIdx = idx;
        }
    }


    /* 내부 분석 결과 모델 */
    @Data
    private static class InterviewAnalysisResult {

        private int totalScore;
        private int techScore;
        private int logicScore;
        private int softScore;

        private double levelAvg;
        private double wrongAnswerRate;

        private String summary;
        private List<String> strengths;
        private List<String> weaknesses;
        private List<String> feedback;

        private Integer totalTokens;
    }
}
