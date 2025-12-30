package com.ict.finalproject.dashboard.adminTokenStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyServiceGptCostVO {

    private String date; // yyyy-MM-dd

    private int interviewCost;
    private int resumeCost;
    private int chatbotCost;
}
