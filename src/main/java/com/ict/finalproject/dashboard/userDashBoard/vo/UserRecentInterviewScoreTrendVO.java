package com.ict.finalproject.dashboard.userDashBoard.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRecentInterviewScoreTrendVO {

    private int techScore;
    private int logicScore;
    private int softScore;
    private int averageScore;

}
