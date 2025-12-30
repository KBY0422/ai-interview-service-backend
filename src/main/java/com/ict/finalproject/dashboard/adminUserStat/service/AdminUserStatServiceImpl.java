package com.ict.finalproject.dashboard.adminUserStat.service;

import com.ict.finalproject.dashboard.adminUserStat.mapper.AdminUserStatMapper;
import com.ict.finalproject.dashboard.adminUserStat.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserStatServiceImpl implements AdminUserStatService {

    private final AdminUserStatMapper adminUserStatMapper;

    @Override
    public UserStatSummaryVO getStatSummary() {
        UserStatSummaryVO  userStatSummaryVO = new UserStatSummaryVO();

          // 상위 4개 카드에 들어가는 값들
        Integer totalUserCount = adminUserStatMapper.getTotalUser();
        Integer todaySignupCount =adminUserStatMapper.getTodaySignup() ;
        Integer weekSignupCount = adminUserStatMapper.getWeekSignup() ;
        Integer activeUserCount = adminUserStatMapper.getActiveUserCount();


        userStatSummaryVO.setTotalUserCount(totalUserCount == null ? 0 : totalUserCount);
        userStatSummaryVO.setTodaySignupCount(todaySignupCount == null ? 0 : todaySignupCount);
        userStatSummaryVO.setWeekSignupCount(weekSignupCount == null ? 0 : weekSignupCount);
        userStatSummaryVO.setActiveUserCount(activeUserCount == null ? 0 : activeUserCount);

        return userStatSummaryVO;
    }

    //최근 7일 간 회원가입수 변동
    @Override
    public List<DailySignupTrendVO> getDailySignup() {

        List<DailySignupTrendVO> raw = adminUserStatMapper.getDailySignup();

        // 정제해서 다시 담기 ?
        // Map으로 변환
        Map<String, Integer> map = raw.stream()
                .collect(Collectors.toMap(
                        DailySignupTrendVO::getSignupDate,
                        DailySignupTrendVO::getSignupCount
                ));

        List<DailySignupTrendVO> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            String key = d.toString();

            DailySignupTrendVO vo = new DailySignupTrendVO();
            vo.setSignupDate(key);
            vo.setSignupCount(map.getOrDefault(key, 0));
            result.add(vo);
        }

        return result;
    }

    @Override
    public List<MonthlyUserCountVO> getMonthlyUser() {


       return adminUserStatMapper.getMonthlyUser();
    }

    @Override
    public List<MonthlyInactiveUserStatVO> getActiveUser() {



        return adminUserStatMapper.getActiveUser();
    }

    @Override
    public List<MonthlyJoinCountVO> getJoinCountVO() {
        return adminUserStatMapper.getJoinCountVO();
    }


}
