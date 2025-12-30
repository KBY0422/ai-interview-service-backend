package com.ict.finalproject.dashboard.adminUserStat.controller;


import com.ict.finalproject.common.vo.DataVO;
import com.ict.finalproject.dashboard.adminUserStat.service.AdminUserStatService;
import com.ict.finalproject.dashboard.adminUserStat.vo.*;
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
public class AdminUserStatController {

    private final AdminUserStatService adminUserStatService;

    @PostMapping("/userstat")
    public DataVO<UserStatDashboardResponseVO> getUserStat(){

        UserStatDashboardResponseVO  response = new  UserStatDashboardResponseVO();
        try {
            // 사용자 통계에 들어가는 값들 가져오기
            UserStatSummaryVO statSummaryVO = adminUserStatService.getStatSummary();

            //최근 일주일 (오늘포함)
            List<DailySignupTrendVO> dailySignupTrendVO = adminUserStatService.getDailySignup();
            // 월별 전체 사용자 비율
            List<MonthlyUserCountVO> monthlyUserCountVO = adminUserStatService.getMonthlyUser();
            // 해당월에 탈퇴한 회원수 가져오기.
            List<MonthlyInactiveUserStatVO> monthlyInactiveUserStatVO = adminUserStatService.getActiveUser();
            // 해당월에 회원가입한 회원수 가져오기.
            List<MonthlyJoinCountVO> joinCountVO = adminUserStatService.getJoinCountVO();

            //react로 보낼때 하나의 VO로 넘겨주기.
            response.setSummary(statSummaryVO);
            response.setDailySignupTrend(dailySignupTrendVO);
            response.setMonthlyUserCount(monthlyUserCountVO);
            response.setMonthlyInactiveUserStat(monthlyInactiveUserStatVO);
            response.setMonthlyJoinCount(joinCountVO);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new DataVO<>(true, response, "대시보드 조회 성공");

    }


}
