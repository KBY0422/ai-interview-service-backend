package com.ict.finalproject.dashboard.adminInterviewStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewCardStatVO {
    /** 오늘 면접 수 */
    private int todayInterviewCount;

    /** 이번 주 면접 수 */
    private int weekInterviewCount;

    /** 이번 달 면접 수 */
    private int monthInterviewCount;

    /** 전체 평균 점수 */
    private int averageScore;
}
