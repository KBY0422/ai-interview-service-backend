package com.ict.finalproject.dashboard.userDashBoard.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInterviewSkillRadarVO {

    private int techScore;
    private int logicScore;
    private int softScore;
    private int levelAvg;
    private int accuracyRate;        //오답률을 정확도로 바꾸기
}
