package com.ict.finalproject.dashboard.adminDashBoard.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminTopSkillVO {

    /** 기술 스택명 */
    private String skill;

    /** 선택 횟수 */
    private int count;

}
