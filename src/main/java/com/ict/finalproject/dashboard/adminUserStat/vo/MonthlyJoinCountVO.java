package com.ict.finalproject.dashboard.adminUserStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyJoinCountVO {

    private String month;     // yyyy-MM

    /** 해당 월에 새로 가입한 사용자 수 */
    private int joinCount;
}
