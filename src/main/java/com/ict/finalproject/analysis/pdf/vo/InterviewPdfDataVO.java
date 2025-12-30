package com.ict.finalproject.analysis.pdf.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * PDF 생성에 필요한 분석 데이터 VO
 * - member, session, session_analysis 조인 결과
 */
@Data
public class InterviewPdfDataVO {

    // 회원 / 세션 기본 정보
    private String mName;
    private String sJob;
    private String sType;

    // 세션 종합 점수
    private Integer saTotalScore;
    private Integer saTechScore;
    private Integer saLogicScore;
    private Integer saSoftScore;

    // 전체 난이도 평균
    private BigDecimal saLevelAvg;

    // 요약 / 강점 / 약점 / 종합 피드백
    private String saSummary;
    private String saStrengths;
    private String saWeaknesses;
    private String saFeedback;

    // 분석 생성일
    private LocalDateTime saCreateAt;
}
