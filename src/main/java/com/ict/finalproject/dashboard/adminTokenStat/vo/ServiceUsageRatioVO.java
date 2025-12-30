package com.ict.finalproject.dashboard.adminTokenStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceUsageRatioVO {

    /** 서비스명 */
    private String service;

    /** 누적 GPT 비용 */
    private int gptCost;


}
