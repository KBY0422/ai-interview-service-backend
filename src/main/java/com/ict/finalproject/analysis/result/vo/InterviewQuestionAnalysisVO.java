package com.ict.finalproject.analysis.result.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewQuestionAnalysisVO {

    private int qaIdx;
    private int qaQIdx;

    private Integer qaTotalScore;
    private Integer qaTechScore;
    private Integer qaLogicScore;
    private Integer qaSoftScore;

    /*
     GPT 분석 과정에서 추출된 핵심 키워드
     실제 구현 단계에서 JSON 문자열 형태로 저장할 수 있다
     */
    private String qaMissKeywords;

    /*
     질문 단위 요약 피드백
     */
    private String qaFeedbackSummary;

    /*
     분석 후 난이도 또는 레벨 변화
     */
    private Integer qaAfterLevel;

    private LocalDateTime qaCreateAt;
}
