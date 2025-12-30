package com.ict.finalproject.dashboard.adminTokenStat.controller;

import com.ict.finalproject.common.vo.DataVO;
import com.ict.finalproject.dashboard.adminTokenStat.service.AdminTokenStatService;
import com.ict.finalproject.dashboard.adminTokenStat.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminTokenStatController {

    private final AdminTokenStatService adminTokenStatService;

    @PostMapping("/tokenstat")
    public DataVO<AdminTokenStatResponseVO> getTokenStat(){
        AdminTokenStatResponseVO response = new AdminTokenStatResponseVO();
        try {

            /** 상단 KPI 카드 */
            AdminRevenueVO card =adminTokenStatService.getRevenueCard();
            /** 이번달 서비스별 매출 / 추정 이익 */
            List<TotalServiceRevenueVO> totalServiceRevenueVO = adminTokenStatService.totalServiceRevenueVO();
            /** 서비스 사용 비율 (누적 GPT 비용 기준) */
            List<ServiceUsageRatioVO> serviceUsageRatioVO = adminTokenStatService.getServiceUsageRatio();
            /** 최근 7일 GPT 비용 (서비스별) */
            List<DailyServiceGptCostVO>   dailyServiceGptCostVO = adminTokenStatService.getDailySerivceGpt();
            /** 월별 GPT 비용 (서비스별) */
            List<MonthlyServiceGptCostVO>  monthlyServiceGptCostVO =adminTokenStatService.getMonthlySerivceGpt();
            /** 최근 7일 매출 / 추정 이익 */
            List<DailyRevenueProfitVO> dailyRevenueProfitVO =adminTokenStatService.getDailyRevenue();
            /** 월별 매출 / 추정 이익 */
            List<MonthlyRevenueProfitVO> monthlyRevenueProfitVO =adminTokenStatService.getMonthlyRevenue();
            /** 누적 운영 결과 */
            CumulativeFinanceVO cumulativeFinanceVO = adminTokenStatService.getCumulativeFinance();

            response.setCard(card);
            response.setMonthlyrevenue(totalServiceRevenueVO);
            response.setServiceUsageRatioList(serviceUsageRatioVO);
            response.setDailyServiceGptCostList(dailyServiceGptCostVO);
            response.setMonthlyServiceGptCostList(monthlyServiceGptCostVO);
            response.setDailyRevenueProfitList(dailyRevenueProfitVO);
            response.setMonthlyRevenueProfitList(monthlyRevenueProfitVO);
            response.setCumulativeFinance(cumulativeFinanceVO);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        return new DataVO<>(true,response,"대시보드 로딩성공");
    }
}
