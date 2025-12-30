package com.ict.finalproject.analysis.list.vo;

import lombok.Data;

@Data
public class InterviewAnalysisListVO {
    private Integer sIdx;       // 세션 PK
    private String date;        // YYYY-MM-DD
    private String job;         // 직무
    private String type;        // 유형
    private Integer totalScore; // 총점
    private Integer hasPdf;     // PDF 존재 여부 (0/1)
    private String title;       // 리스트 제목
}
