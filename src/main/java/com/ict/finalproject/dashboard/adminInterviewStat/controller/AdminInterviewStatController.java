package com.ict.finalproject.dashboard.adminInterviewStat.controller;


import com.ict.finalproject.common.vo.DataVO;
import com.ict.finalproject.dashboard.adminInterviewStat.service.AdminInterviewStatService;
import com.ict.finalproject.dashboard.adminInterviewStat.vo.*;
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
public class AdminInterviewStatController {

    private final AdminInterviewStatService adminInterviewStatService;

    @PostMapping("/interviewstat")
    public DataVO<AdminInterviewStatResponseVO> getInterviewStat(){
        AdminInterviewStatResponseVO response = new AdminInterviewStatResponseVO();
        try {
            //면접 통계 카드 4개
            InterviewCardStatVO card = adminInterviewStatService.getInterviewCard();
            //직종+기술스택 선택비율 (최근 3개월) !!
            List<JobRatioVO> jobRatio = adminInterviewStatService.getJobRatio();
            List<JobSkillRatioVO> skillRatio = adminInterviewStatService.getSkillRatio();
            //면접 유형 선택 비율 (최근 3개월)
            List<InterviewTypeRatioVO> ratio = adminInterviewStatService.getInterviewTypeRatio();

            // 최근 7일간 면접 수 변화
            List<DailyInterviewCountVO> dailyInterviewCount = adminInterviewStatService.getDailyInterviewCount();
            // 월별 면접 수 변화
            List<MonthlyInterviewTrendVO> monthlyInterview = adminInterviewStatService.getMonthlyInterviewCount();
            // 평균 질무 난이도 변화 (최근 7일)
            List<DailyAvgDifficultyVO> dailyAvgDifficulty = adminInterviewStatService.getDailyAvgDifficuilty();
            // 평균점수 변화 (최근 7일)
            List<DailyAvgScoreVO> dailyAvgScore = adminInterviewStatService.getDailyAvgScore();

            response.setCardStat(card);
            response.setJobRatioList(jobRatio);
            response.setJobSkillRatioList(skillRatio);
            response.setInterviewTypeRatioList(ratio);
            response.setDailyInterviewCountList(dailyInterviewCount);

            response.setMonthlyTrendList(monthlyInterview);
            response.setDailyAvgDifficultyList(dailyAvgDifficulty);
            response.setDailyAvgScoreList(dailyAvgScore);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new DataVO<>(true, response,"대시보드 조회 성공");

    }
}
