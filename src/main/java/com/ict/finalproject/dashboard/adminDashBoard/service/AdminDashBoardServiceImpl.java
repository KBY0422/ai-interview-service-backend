package com.ict.finalproject.dashboard.adminDashBoard.service;

import com.ict.finalproject.dashboard.adminDashBoard.mapper.AdminDashBoardMapper;
import com.ict.finalproject.dashboard.adminDashBoard.vo.*;
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
public class AdminDashBoardServiceImpl implements AdminDashBoardService {

    private static final int SESSION_PRICE = 800;
    private static final int RESUME_PRICE = 300;
    private final AdminDashBoardMapper adminDashBoardMapper;

    @Override
    public AdminDashBoardCardVO getDashBoardCard() {
        AdminDashBoardCardVO  adminDashBoardCardVO = new AdminDashBoardCardVO();

        Integer totalUserCount = adminDashBoardMapper.getTotalUserCount();
        Integer todayInterview = adminDashBoardMapper.getTodayInterview();
        Integer todayResume = adminDashBoardMapper.getTodayResume();
        List<AdminRevenueTrendRawVO> raw = adminDashBoardMapper.getTodayRevenue();

        int totalRevenue = 0;

        for (AdminRevenueTrendRawVO r : raw) {
            switch (r.getPurpose()) {
                case "SESSION" ->
                        totalRevenue += r.getUsageCount() * SESSION_PRICE;
                case "RESUME" ->
                        totalRevenue += r.getUsageCount() * RESUME_PRICE;
            }
        }


        adminDashBoardCardVO.setTotalUserCount(totalUserCount);
        adminDashBoardCardVO.setTodayInterview(todayInterview);
        adminDashBoardCardVO.setTodayResume(todayResume);
        adminDashBoardCardVO.setTodayRevenue(totalRevenue);

        return adminDashBoardCardVO;
    }

    @Override
    public List<AdminRevenueTrendVO> getRevenueTrend() {
        List<AdminRevenueTrendRawVO> raw = adminDashBoardMapper.getRevenueTrend();

        Map<String, List<AdminRevenueTrendRawVO>> byDate =
                raw.stream().collect(Collectors.groupingBy(AdminRevenueTrendRawVO::getDate));

        List<AdminRevenueTrendVO> result = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            String date = LocalDate.now().minusDays(i).toString();

            int interviewRevenue = 0;
            int resumeRevenue = 0;

            for (AdminRevenueTrendRawVO r : byDate.getOrDefault(date, List.of())) {
                if ("SESSION".equals(r.getPurpose())) {
                    interviewRevenue += r.getUsageCount() * SESSION_PRICE;
                } else if ("RESUME".equals(r.getPurpose())) {
                    resumeRevenue += r.getUsageCount() * RESUME_PRICE;
                }
            }

            int totalRevenue = interviewRevenue + resumeRevenue;

            result.add(new AdminRevenueTrendVO(date, interviewRevenue, resumeRevenue, totalRevenue));
        }

        return result;
    }

    @Override
    public List<AdminTopJobVO> getTopjob() {

        return adminDashBoardMapper.getTopjob();

    }

    @Override
    public List<AdminTopSkillVO> getTopSkill() {

        return adminDashBoardMapper.getTopSkill();
    }

    @Override
    public List<AdminTopKeywordVO> getTopKeyword() {

        return adminDashBoardMapper.getTopKeyword();
    }
}
