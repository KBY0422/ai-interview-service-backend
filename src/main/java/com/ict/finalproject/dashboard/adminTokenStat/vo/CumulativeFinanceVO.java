package com.ict.finalproject.dashboard.adminTokenStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CumulativeFinanceVO {

    private int totalRevenue;
    private int totalGptCost;
    private int totalEstimatedProfit;
}
