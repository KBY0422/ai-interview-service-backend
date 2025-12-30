package com.ict.finalproject.dashboard.adminDashBoard.controller;

import com.ict.finalproject.common.vo.DataVO;
import com.ict.finalproject.dashboard.adminDashBoard.service.AdminDashBoardService;
import com.ict.finalproject.dashboard.adminDashBoard.vo.*;
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
public class AdminDashBoardController {

    private final AdminDashBoardService adminDashBoardService;

    @PostMapping("/dashboard")
    public DataVO<AdminDashBoardResponseVO> getDashboard(){
        AdminDashBoardResponseVO response =  new AdminDashBoardResponseVO();
        try {

            // 상단 4개 카드
            AdminDashBoardCardVO card =  adminDashBoardService.getDashBoardCard();
            //  최근 일주일 매출추세(오늘 포함)
            List<AdminRevenueTrendVO> revenue = adminDashBoardService.getRevenueTrend();
            // 직무 top3
            List<AdminTopJobVO> topJob = adminDashBoardService.getTopjob();
            // 기술스택 top3
            List<AdminTopSkillVO> topSkill = adminDashBoardService.getTopSkill();
            // 검색 키워드 top3
            List<AdminTopKeywordVO> topKeyword = adminDashBoardService.getTopKeyword();

            response.setCard(card);
            response.setRevenueTrend7Days(revenue);
            response.setTopJobs(topJob);
            response.setTopSkills(topSkill);
            response.setTopKeywords(topKeyword);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new DataVO<>(true,  response,"대시보드 조회 성공");
    }
}
