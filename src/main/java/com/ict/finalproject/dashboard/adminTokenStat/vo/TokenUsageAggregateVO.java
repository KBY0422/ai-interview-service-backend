package com.ict.finalproject.dashboard.adminTokenStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenUsageAggregateVO {

    //“서비스별 전체/기간 집계”용  purpose를 축으로 통계 구축

    private String purpose;     // INTERVIEW / RESUME / CHATBOT / SESSION
    private int usageCount;     // 이용 횟수
    private int totalTokens;    // 토큰 합계

}
