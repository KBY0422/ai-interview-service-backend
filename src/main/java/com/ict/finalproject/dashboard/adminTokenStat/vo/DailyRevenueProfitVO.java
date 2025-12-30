package com.ict.finalproject.dashboard.adminTokenStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyRevenueProfitVO {

    private String date;

    private int revenue;
    private int estimatedProfit;
}
