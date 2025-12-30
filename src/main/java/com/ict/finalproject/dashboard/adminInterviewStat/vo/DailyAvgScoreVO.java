package com.ict.finalproject.dashboard.adminInterviewStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyAvgScoreVO {
    private String date;
    private int avgScore; // 0~100
}
