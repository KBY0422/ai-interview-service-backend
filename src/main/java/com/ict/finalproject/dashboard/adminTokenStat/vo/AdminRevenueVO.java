package com.ict.finalproject.dashboard.adminTokenStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRevenueVO {

    private int todayRevenue;
    private int todayGptCost;
    private int todayEstimatedProfit;

    private int monthRevenue;
    private int monthGptCost;
    private int monthEstimatedProfit;
}
