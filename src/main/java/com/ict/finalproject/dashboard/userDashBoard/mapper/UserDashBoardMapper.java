package com.ict.finalproject.dashboard.userDashBoard.mapper;

import com.ict.finalproject.dashboard.userDashBoard.vo.UserInterviewSkillRadarVO;
import com.ict.finalproject.dashboard.userDashBoard.vo.UserRecentInterviewScoreTrendVO;
import com.ict.finalproject.dashboard.userDashBoard.vo.UserRecentInterviewSummaryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDashBoardMapper {

    // 카드에 들어가는 4개의 값
    int getTotalInterviewCount(int m_idx);
    int getTotalResumeCount(int m_idx);
    Integer getAverageScore(int m_idx);
    Integer getBestScore(int m_idx);

    // Radaer에 들어가는 5개의 값 가져오기
    UserInterviewSkillRadarVO getInterviewSkillRadar(int m_idx);
    // 최근 5회 기술,논리,소프트 스킬 , 총점 점수 변화 추이
    List<UserRecentInterviewScoreTrendVO> getRecentInterviewScore(int m_idx);
    // 최근 2회의 인터뷰 요약.
    List<UserRecentInterviewSummaryVO> getRecentInterviewSummary(int m_idx);
}
