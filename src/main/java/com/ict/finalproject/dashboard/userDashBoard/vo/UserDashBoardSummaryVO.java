package com.ict.finalproject.dashboard.userDashBoard.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDashBoardSummaryVO {

    private int totalInterviewCount ;
    private int totalResumeCount;
    private int averageScore;
    private int bestScore;
    // private int token; 향후 비용적인것까지 추가한다면.

}
