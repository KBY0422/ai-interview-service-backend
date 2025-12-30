package com.ict.finalproject.dashboard.adminTokenStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyServiceGptCostVO {


    private String month; // yyyy-MM

    private int interviewCost;
    private int resumeCost;
    private int chatbotCost;
}
