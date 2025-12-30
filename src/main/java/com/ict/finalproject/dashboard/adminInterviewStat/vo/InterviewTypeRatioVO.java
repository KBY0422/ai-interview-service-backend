package com.ict.finalproject.dashboard.adminInterviewStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewTypeRatioVO {
    /** 면접 유형 */
    private String interviewType;

    /** 건수 */
    private int count;
}
