package com.ict.finalproject.dashboard.adminInterviewStat.service;


import com.ict.finalproject.dashboard.adminInterviewStat.vo.*;

import java.util.List;

public interface AdminInterviewStatService  {

    //면접 통계 카드 4개
    InterviewCardStatVO getInterviewCard();
    //직종+기술스택 선택비율 (최근 3개월)
    List<JobRatioVO> getJobRatio();
    List<JobSkillRatioVO> getSkillRatio();
    //면접 유형 선택 비율 (최근 3개월)
    List<InterviewTypeRatioVO> getInterviewTypeRatio();
    // 최근 7일간 면접 수 변화
    List<DailyInterviewCountVO> getDailyInterviewCount();
    // 월별 면접 수 변화
    List<MonthlyInterviewTrendVO> getMonthlyInterviewCount();
    // 평균 질무 난이도 변화 (최근 7일)
    List<DailyAvgDifficultyVO> getDailyAvgDifficuilty();
    // 평균점수 변화 (최근 7일)
    List<DailyAvgScoreVO> getDailyAvgScore();


}
