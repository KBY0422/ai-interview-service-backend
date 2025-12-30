package com.ict.finalproject.dashboard.adminUserStat.service;


import com.ict.finalproject.dashboard.adminUserStat.vo.*;

import java.util.List;

public interface AdminUserStatService {

    UserStatSummaryVO getStatSummary();
    List<DailySignupTrendVO> getDailySignup();
    List<MonthlyUserCountVO> getMonthlyUser();
    List<MonthlyInactiveUserStatVO> getActiveUser();
    List<MonthlyJoinCountVO> getJoinCountVO();
}
