package com.ict.finalproject.dashboard.adminTokenStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalServiceRevenueVO {

    /** 서비스명 (면접 / 이력서 / 챗봇) */
    private String service;

    /** 매출 */
    private int revenue;

    /** 추정 이익 */
    private int estimatedProfit;
}
