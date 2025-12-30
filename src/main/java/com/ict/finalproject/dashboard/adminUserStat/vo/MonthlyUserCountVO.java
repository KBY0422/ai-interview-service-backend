package com.ict.finalproject.dashboard.adminUserStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyUserCountVO {

    private String month;      // yyyy-MM
    private int totalUserCount;


}
