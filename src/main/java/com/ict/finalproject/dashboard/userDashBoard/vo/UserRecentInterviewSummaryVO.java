package com.ict.finalproject.dashboard.userDashBoard.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRecentInterviewSummaryVO {

    private String title;
    private int score;
    private String interviewDate;
    private String goodContent;
    private String weakContent;
    private String feedback;


}
