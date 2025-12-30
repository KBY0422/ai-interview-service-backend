package com.ict.finalproject.dashboard.adminInterviewStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyInterviewTrendVO {


    /** 월 (YYYY-MM) */
    private String monthLabel;

    /** 면접 수 */
    private int interviewCount;
}
