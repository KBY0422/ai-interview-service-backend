package com.ict.finalproject.dashboard.adminDashBoard.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashBoardCardVO {

    // 전체 회원 수
    private int totalUserCount;

    // 오늘의 면접
    private int todayInterview;

    // 오늘의 이력서
    private int todayResume;

    // 오늘의 매출
    private int todayRevenue;





}
