package com.ict.finalproject.dashboard.adminTokenStat.service;

import com.ict.finalproject.dashboard.adminTokenStat.mapper.AdminTokenStatMapper;
import com.ict.finalproject.dashboard.adminTokenStat.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminTokenStatServiceImpl implements AdminTokenStatService {

    private static final float TOKEN_PRICE_PER_1K = 1.01f;
    private static final int SESSION_PRICE = 800; // 면접 면접 비용
    private static final int RESUME_PRICE = 300; // 이력서 분석 비용
    private final AdminTokenStatMapper adminTokenStatMapper;

    // 실제 매출 계산을 위해서 매서드 생성. - > 이건 첫 카드에서 한번쓰이는 매서드
    private RevenueCalcResult calculateRevenue(List<TokenUsageAggregateVO> logs) {
        int interviewCount = 0;
        int resumeCount = 0;
        int totalTokens = 0;

        for (TokenUsageAggregateVO log : logs) {
            totalTokens += log.getTotalTokens();

            switch (log.getPurpose()) {
                case "SESSION":
                    interviewCount += log.getUsageCount();
                    break;
                case "RESUME":
                    resumeCount += log.getUsageCount();
                    break;
                // CHATBOT → 매출 제외
            }
        }

        int revenue =
                interviewCount * SESSION_PRICE
                        + resumeCount * RESUME_PRICE;



        int gptCost =
                Math.round((int)((totalTokens / 1000f) * TOKEN_PRICE_PER_1K));

        int estimatedProfit = revenue - gptCost;

        return new RevenueCalcResult(revenue, gptCost, estimatedProfit);
    }


    /** 상단 KPI 카드 를 위한 VO 반환하기  */
    @Override
    public AdminRevenueVO getRevenueCard() {

    try {
        List<TokenUsageAggregateVO> todayLogs =
                adminTokenStatMapper.getTodayTokenUsage();

        List<TokenUsageAggregateVO> monthLogs =
                adminTokenStatMapper.getMonthlyTokenUsage();

        RevenueCalcResult today = calculateRevenue(todayLogs);
        RevenueCalcResult month = calculateRevenue(monthLogs);

        AdminRevenueVO vo = new AdminRevenueVO();
        vo.setTodayRevenue(today.getRevenue());
        vo.setTodayGptCost(today.getGptCost());
        vo.setTodayEstimatedProfit(today.getEstimatedProfit());

        vo.setMonthRevenue(month.getRevenue());
        vo.setMonthGptCost(month.getGptCost());
        vo.setMonthEstimatedProfit(month.getEstimatedProfit());

            return vo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 이번달 서비스별 매출 / 추정 이익 */
    @Override
    public List<TotalServiceRevenueVO> totalServiceRevenueVO() {


        List<TokenUsageAggregateVO> rawList =
                adminTokenStatMapper.getTotalServiceUsage();

        List<TotalServiceRevenueVO> result = new ArrayList<>();

        for (TokenUsageAggregateVO raw : rawList) {

            // 챗봇은 서비스별 매출/이익에서 제외
            if ("CHATBOT".equals(raw.getPurpose())) {
                continue;
            }

            int revenue = 0;

            switch (raw.getPurpose()) {
                case "SESSION":
                    revenue = raw.getUsageCount() * SESSION_PRICE;
                    break;
                case "RESUME":
                    revenue = raw.getUsageCount() * RESUME_PRICE;
                    break;
            }

            int gptCost = Math.round(
                    (raw.getTotalTokens() / 1000f) * TOKEN_PRICE_PER_1K
            );

            int profit = revenue - gptCost;

            TotalServiceRevenueVO vo = new TotalServiceRevenueVO();
            vo.setService(raw.getPurpose());
            vo.setRevenue(revenue);
            vo.setEstimatedProfit(profit);

            result.add(vo);
        }

        return result;
    }

    /** 서비스 사용 비율 (누적 GPT 비용 기준) */
    @Override
    public List<ServiceUsageRatioVO> getServiceUsageRatio() {

        List<TokenUsageAggregateVO> rawList =
                adminTokenStatMapper.getServiceUsageRatio();

        List<ServiceUsageRatioVO> result = new ArrayList<>();

        for (TokenUsageAggregateVO raw : rawList) {

            int gptCost = Math.round(
                    (raw.getTotalTokens() / 1000f) * TOKEN_PRICE_PER_1K
            );

            ServiceUsageRatioVO vo = new ServiceUsageRatioVO();
            vo.setService(raw.getPurpose());
            vo.setGptCost(gptCost);

            result.add(vo);
        }

        return result;
    }
    /** 최근 7일 GPT 비용 (서비스별) */
    @Override
    public List<DailyServiceGptCostVO> getDailySerivceGpt() {



        List<DailyTokenUsageRawVO> rawList =
                adminTokenStatMapper.getDailySerivceGpt();

        // 1️⃣ DB 결과를 Map으로 변환
        Map<String, DailyServiceGptCostVO> dataMap = new HashMap<>();

        for (DailyTokenUsageRawVO raw : rawList) {
            String date = raw.getDate();

            DailyServiceGptCostVO vo = dataMap.computeIfAbsent(
                    date,
                    d -> new DailyServiceGptCostVO(d, 0, 0, 0)
            );

            int gptCost = Math.round(
                    (raw.getTotalTokens() / 1000f) * TOKEN_PRICE_PER_1K
            );

            switch (raw.getPurpose()) {
                case "SESSION" -> vo.setInterviewCost(gptCost);
                case "RESUME" -> vo.setResumeCost(gptCost);
                case "CHATBOT" -> vo.setChatbotCost(gptCost);
            }
        }

        // 2️⃣ 최근 7일 날짜 생성 (오늘 포함)
        List<DailyServiceGptCostVO> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String key = date.toString(); // yyyy-MM-dd

            result.add(
                    dataMap.getOrDefault(
                            key,
                            new DailyServiceGptCostVO(key, 0, 0, 0)
                    )
            );
        }

        return result;
    }

    /** 월별 GPT 비용 (서비스별) */
    @Override
    public List<MonthlyServiceGptCostVO> getMonthlySerivceGpt() {


        List<DailyTokenUsageRawVO> rawList =
                adminTokenStatMapper.getMonthlySerivceGpt();

        Map<String, MonthlyServiceGptCostVO> map = new LinkedHashMap<>();


        for (DailyTokenUsageRawVO raw : rawList) {

            String monthKey = raw.getDate(); // yyyy-MM

            MonthlyServiceGptCostVO vo =
                    map.computeIfAbsent(monthKey, k -> {
                        MonthlyServiceGptCostVO m = new MonthlyServiceGptCostVO();
                        m.setMonth(k.substring(5) + "월"); // "01" → "1월"
                        return m;
                    });

            int gptCost = Math.round(
                    (raw.getTotalTokens() / 1000f) * TOKEN_PRICE_PER_1K
            );

            switch (raw.getPurpose()) {
                case "SESSION":
                    vo.setInterviewCost(vo.getInterviewCost() + gptCost);
                    break;
                case "RESUME":
                    vo.setResumeCost(vo.getResumeCost() + gptCost);
                    break;
                case "CHATBOT":
                    vo.setChatbotCost(vo.getChatbotCost() + gptCost);
                    break;
            }
        }

        return new ArrayList<>(map.values());
    }

    static class RevenueBucket {
        int sessionCount = 0;
        int resumeCount = 0;
        int totalTokens = 0;
    }

    private RevenueCalcResult calculateRevenue(
            int sessionCount,
            int resumeCount,
            int totalTokens
    ) {


        int revenue =
                sessionCount * SESSION_PRICE
                        + resumeCount * RESUME_PRICE;

        int gptCost =
                Math.round(((totalTokens / 1000f) * TOKEN_PRICE_PER_1K));

        int estimatedProfit = revenue - gptCost;

        return new RevenueCalcResult(revenue, gptCost, estimatedProfit);
    }


    /** 최근 7일 매출 / 추정 이익 */
    @Override
    public List<DailyRevenueProfitVO> getDailyRevenue() {


        List<DailyTokenUsageRawVO> rawList =
                adminTokenStatMapper.getDailyRevenue();

        // 1️⃣ 날짜별 매출 누적용 Map
        Map<String, RevenueBucket> dataMap = new HashMap<>();

        for (DailyTokenUsageRawVO raw : rawList) {

            RevenueBucket bucket =
                    dataMap.computeIfAbsent(raw.getDate(), k -> new RevenueBucket());

            bucket.totalTokens += raw.getTotalTokens();

            switch (raw.getPurpose()) {
                case "SESSION":
                    bucket.sessionCount += raw.getUsageCount();
                    break;
                case "RESUME":
                    bucket.resumeCount += raw.getUsageCount();
                    break;
                // CHATBOT → 매출 제외
            }
        }

        // 2️⃣ 최근 7일(오늘 포함) 강제 생성
        List<DailyRevenueProfitVO> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String key = date.toString(); // yyyy-MM-dd

            RevenueBucket bucket = dataMap.getOrDefault(
                    key,
                    new RevenueBucket() // 전부 0
            );

            RevenueCalcResult calc =
                    calculateRevenue(
                            bucket.sessionCount,
                            bucket.resumeCount,
                            bucket.totalTokens
                    );

            result.add(
                    new DailyRevenueProfitVO(
                            key,
                            calc.getRevenue(),
                            calc.getEstimatedProfit()
                    )
            );
        }

        return result;
    }
    /** 월별 매출 / 추정 이익 */
    @Override
    public List<MonthlyRevenueProfitVO> getMonthlyRevenue() {

        List<DailyTokenUsageRawVO> rawList =
                adminTokenStatMapper.getMonthlyRevenue();

        Map<String, RevenueBucket> map = new LinkedHashMap<>();

        for (DailyTokenUsageRawVO raw : rawList) {

            RevenueBucket bucket =
                    map.computeIfAbsent(raw.getDate(), k -> new RevenueBucket());

            bucket.totalTokens += raw.getTotalTokens();

            switch (raw.getPurpose()) {
                case "SESSION":
                    bucket.sessionCount += raw.getUsageCount();
                    break;
                case "RESUME":
                    bucket.resumeCount += raw.getUsageCount();
                    break;
            }
        }

        List<MonthlyRevenueProfitVO> result = new ArrayList<>();

        for (Map.Entry<String, RevenueBucket> e : map.entrySet()) {

            RevenueCalcResult calc =
                    calculateRevenue(
                            e.getValue().sessionCount,
                            e.getValue().resumeCount,
                            e.getValue().totalTokens
                    );

            result.add(new MonthlyRevenueProfitVO(
                    e.getKey().substring(5) + "월",
                    calc.getRevenue(),
                    calc.getEstimatedProfit()
            ));
        }

        return result;
    }
    /** 누적 운영 결과 */
    @Override
    public CumulativeFinanceVO getCumulativeFinance() {

        List<TokenUsageAggregateVO> allLogs =
                adminTokenStatMapper.getCumulativeFinance();

        RevenueCalcResult calc = calculateRevenue(allLogs);

        CumulativeFinanceVO vo = new CumulativeFinanceVO();
        vo.setTotalRevenue(calc.getRevenue());
        vo.setTotalGptCost(calc.getGptCost());
        vo.setTotalEstimatedProfit(calc.getEstimatedProfit());

        return vo;
    }
}
