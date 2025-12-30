package com.ict.finalproject.dashboard.adminDashBoard.vo;

import lombok.Data;

import java.util.List;
@Data
public class AdminDashBoardResponseVO {

    /** 상단 카드 4개 */
    private AdminDashBoardCardVO card;

    /** 최근 7일 매출 추이 */
    private List<AdminRevenueTrendVO> revenueTrend7Days;

    /** 직무 TOP 3 */
    private List<AdminTopJobVO> topJobs;

    /** 기술 스택 TOP 3 */
    private List<AdminTopSkillVO> topSkills;

    /** 검색 키워드 TOP 3 */
    private List<AdminTopKeywordVO> topKeywords;
}
