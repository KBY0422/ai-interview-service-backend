package com.ict.finalproject.dashboard.userDashBoard.service;

import com.ict.finalproject.dashboard.userDashBoard.vo.UserDashBoardSummaryVO;
import com.ict.finalproject.dashboard.userDashBoard.vo.UserInterviewSkillRadarVO;
import com.ict.finalproject.dashboard.userDashBoard.vo.UserRecentInterviewScoreTrendVO;
import com.ict.finalproject.dashboard.userDashBoard.vo.UserRecentInterviewSummaryVO;

import java.util.List;

public interface UserDashBoardService {

    UserDashBoardSummaryVO getBoardSummary(int m_idx);
    UserInterviewSkillRadarVO getInterviewSkillRadar(int m_idx);
    List<UserRecentInterviewScoreTrendVO> getRecentInterviewScore(int m_idx);
    List<UserRecentInterviewSummaryVO> getRecentInterviewSummary(int m_idx);
}
