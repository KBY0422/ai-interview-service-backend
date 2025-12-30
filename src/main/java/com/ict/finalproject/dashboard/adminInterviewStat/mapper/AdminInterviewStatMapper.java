package com.ict.finalproject.dashboard.adminInterviewStat.mapper;


import com.ict.finalproject.dashboard.adminInterviewStat.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminInterviewStatMapper {

    Integer getTodayInterviewCount();
    Integer getWeekInterviewCount();
    Integer getMonthInterviewCount();
    // 사용자 전체 평균점수 내기
    Integer getAverageScore();

    // 3개월 이내 직종 과 직종선택 횟수 가져오기
    List<JobRatioVO> getJobRatio();

    //기술스택 가져오기
    List<JobSkillRatioVO> getJobSkillRatio();

    //면접유형 선택 비율
    List<InterviewTypeRatioVO> getInterviewTypeRatio();

    //최근 7일간 면접수 변화
    List<DailyInterviewCountVO> getDailyInterviewCount();

    //월별 면접수 변화
    List<MonthlyInterviewTrendVO> getMonthlyInterviewCount();
    //최근 7일간 평균 난이도 변화
    List<DailyAvgDifficultyVO> getDailyAvgDifficuilty();
    //최근 7일 간 평균점수 변화
    List<DailyAvgScoreVO> getDailyAvgScore();
}
