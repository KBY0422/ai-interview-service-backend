package com.ict.finalproject.analysis.result.vo;

import lombok.Data;

@Data
public class InterviewQnaVO {

    private int qIdx;       // interview_qna.q_idx
    private int qSIdx;      // interview_qna.q_s_idx
    private String qContent; // 질문 내용
    private Integer qType;   // 질문 유형

    /*
     답변 내용
     현재 테이블에는 존재하지 않지만
     추후 답변 컬럼 또는 별도 테이블이 추가될 것을 가정한다
     GPT 분석 단계에서 이 값이 사용된다
     */
    private String answerText;
}
