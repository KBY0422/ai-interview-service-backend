package com.ict.finalproject.dashboard.adminTokenStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyTokenUsageRawVO {
    //일별 GPT 비용 축이 date + purpose
    private String date;        // yyyy-MM-dd
    private String purpose;     // SESSION / RESUME / CHATBOT
    private int usageCount;     // COUNT(*)
    private int totalTokens;    // SUM(t_total)
}
