package com.ict.finalproject.dashboard.adminTokenStat.vo;

import lombok.Data;

import java.util.List;

@Data
public class AdminTokenStatResponseVO {
    /** 상단 KPI 카드 */
    private AdminRevenueVO card;

    /** 이번달 서비스별 매출 / 추정 이익 */
    private List<TotalServiceRevenueVO> monthlyrevenue;

    /** 서비스 사용 비율 (누적 GPT 비용 기준) */
    private List<ServiceUsageRatioVO> serviceUsageRatioList;

    /** 최근 7일 GPT 비용 (서비스별) */
    private List<DailyServiceGptCostVO> dailyServiceGptCostList;

    /** 월별 GPT 비용 (서비스별) */
    private List<MonthlyServiceGptCostVO> monthlyServiceGptCostList;

    /** 최근 7일 매출 / 추정 이익 */
    private List<DailyRevenueProfitVO> dailyRevenueProfitList;

    /** 월별 매출 / 추정 이익 */
    private List<MonthlyRevenueProfitVO> monthlyRevenueProfitList;

    /** 누적 운영 결과 */
    private CumulativeFinanceVO cumulativeFinance;
}
