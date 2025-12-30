package com.ict.finalproject.dashboard.adminUserStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailySignupTrendVO {

    private String signupDate;
    private int signupCount;
}
