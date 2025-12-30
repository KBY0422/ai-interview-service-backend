package com.ict.finalproject.dashboard.adminUserStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatSummaryVO {

    private int totalUserCount;        // 전체 회원 수
    private int todaySignupCount;      // 오늘 가입 수
    private int weekSignupCount;       // 이번 주 가입 수
    private int activeUserCount;       // 활성 사용자 수
    private int allUserCount;          // 전체 회원가입수 = 활성 사용 +// 비활성 사용자
}
