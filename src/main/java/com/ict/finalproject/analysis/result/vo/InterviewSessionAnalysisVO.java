package com.ict.finalproject.analysis.result.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InterviewSessionAnalysisVO {

    private Integer saIdx;          // PK
    private Integer saSIdx;          // 세션 ID

    // ✅ 반드시 추가해야 하는 필드
    private Integer saMIdx;          // 회원 ID (m_idx)

    private Integer saTotalScore;
    private Integer saTechScore;
    private Integer saLogicScore;
    private Integer saSoftScore;

    private BigDecimal saLevelAvg;
    private BigDecimal saWrongAnswerRate;

    private String saSummary;
    private String saStrengths;
    private String saWeaknesses;
    private String saFeedback;

    private LocalDateTime saCreateAt;
}
