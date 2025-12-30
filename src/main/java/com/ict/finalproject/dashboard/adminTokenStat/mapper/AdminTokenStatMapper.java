package com.ict.finalproject.dashboard.adminTokenStat.mapper;


import com.ict.finalproject.dashboard.adminTokenStat.vo.DailyTokenUsageRawVO;
import com.ict.finalproject.dashboard.adminTokenStat.vo.TokenUsageAggregateVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminTokenStatMapper {

    /** 상단 KPI 카드 */
    List<TokenUsageAggregateVO> getTodayTokenUsage();
    List<TokenUsageAggregateVO> getMonthlyTokenUsage();
    /** 누적 서비스별 매출 / 추정 이익 */
    List<TokenUsageAggregateVO> getTotalServiceUsage();
    /** 누적 서비스 사용 비율 (누적 GPT 비용 기준) */
    List<TokenUsageAggregateVO> getServiceUsageRatio();
    /** 최근 7일 GPT 비용 (서비스별) */
    List<DailyTokenUsageRawVO> getDailySerivceGpt();
    /** 월별 GPT 비용 (서비스별) */
    List<DailyTokenUsageRawVO> getMonthlySerivceGpt();
    /** 최근 7일 매출 / 추정 이익 */
    List<DailyTokenUsageRawVO> getDailyRevenue();
    /** 월별 매출 / 추정 이익 */
    List<DailyTokenUsageRawVO> getMonthlyRevenue();
    /** 누적 운영 결과 */
    List<TokenUsageAggregateVO>  getCumulativeFinance();

}
