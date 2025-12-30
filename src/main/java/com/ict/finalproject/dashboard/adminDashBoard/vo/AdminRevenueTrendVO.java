package com.ict.finalproject.dashboard.adminDashBoard.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRevenueTrendVO {

    /** 날짜 (YYYY-MM-DD) */
    private String date;

    /** 면접 매출 */
    private int interviewRevenue;

    /** 이력서 매출 */
    private int resumeRevenue;

    /** 총 매출 */
    private int totalRevenue;

}
