package com.ict.finalproject.dashboard.adminUserStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyInactiveUserStatVO {

    private String month;        // yyyy-MM

    /** 해당 월 말 기준 비활성 사용자 수 */
    private int inactiveUserCount;
    

}
