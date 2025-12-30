package com.ict.finalproject.dashboard.userDashBoard.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserDashBoardResponseVO {

    private UserDashBoardSummaryVO userDashBoardSummaryVO;
    private UserInterviewSkillRadarVO userInterviewSkillRadarVO;
    private List<UserRecentInterviewScoreTrendVO> userRecentInterviewScoreTrendVO;
    private List<UserRecentInterviewSummaryVO> userRecentInterviewSummaryVO;
}
