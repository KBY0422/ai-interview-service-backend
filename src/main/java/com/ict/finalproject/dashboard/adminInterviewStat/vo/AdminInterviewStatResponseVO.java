package com.ict.finalproject.dashboard.adminInterviewStat.vo;

import lombok.Data;

import java.util.List;

@Data
public class AdminInterviewStatResponseVO {

    // 상위 4개 카드
    private InterviewCardStatVO cardStat;

    // 직종 선택 비율 (Inner Pie)
    private List<JobRatioVO> jobRatioList;

    // 직종 + 기술 선택 비율 (Outer Pie)
    private List<JobSkillRatioVO> jobSkillRatioList;

    //  면접 유형 분포
    private List<InterviewTypeRatioVO> interviewTypeRatioList;

    //  월별 면접 수
    private List<MonthlyInterviewTrendVO> monthlyTrendList;

    // 최근 7일 면접수 변화
    private List<DailyInterviewCountVO> dailyInterviewCountList;
    //  최근 7일 평균 난이도 변화
    private List<DailyAvgDifficultyVO> dailyAvgDifficultyList;
    //  최근 7일 평균 점수 변화
    private List<DailyAvgScoreVO> dailyAvgScoreList;



}
