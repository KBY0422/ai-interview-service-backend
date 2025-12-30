package com.ict.finalproject.dashboard.adminUserStat.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserStatDashboardResponseVO {

    private UserStatSummaryVO summary;

    private List<DailySignupTrendVO> dailySignupTrend;     // 최근 7일
    private List<MonthlyUserCountVO> monthlyUserCount;     // 월별 전체 회원
    private List<MonthlyInactiveUserStatVO> monthlyInactiveUserStat; //해당월 탈퇴 회원수 집계
    private List<MonthlyJoinCountVO>  monthlyJoinCount; // 해당월 회원 가입수 집계 


}
