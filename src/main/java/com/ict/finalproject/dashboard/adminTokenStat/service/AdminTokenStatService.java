package com.ict.finalproject.dashboard.adminTokenStat.service;


import com.ict.finalproject.dashboard.adminTokenStat.vo.*;

import java.util.List;

public interface AdminTokenStatService {

    /** 상단 KPI 카드 */
    AdminRevenueVO getRevenueCard();
    /** 이번달 서비스별 매출 / 추정 이익 */
    List<TotalServiceRevenueVO> totalServiceRevenueVO();
    /** 서비스 사용 비율 (누적 GPT 비용 기준) */
    List<ServiceUsageRatioVO> getServiceUsageRatio();
    /** 최근 7일 GPT 비용 (서비스별) */
    List<DailyServiceGptCostVO>  getDailySerivceGpt();
    /** 월별 GPT 비용 (서비스별) */
    List<MonthlyServiceGptCostVO> getMonthlySerivceGpt();

    /** 최근 7일 매출 / 추정 이익 */
    List<DailyRevenueProfitVO> getDailyRevenue();
    /** 월별 매출 / 추정 이익 */
    List<MonthlyRevenueProfitVO> getMonthlyRevenue();
    /** 누적 운영 결과 */
    CumulativeFinanceVO getCumulativeFinance();
}
