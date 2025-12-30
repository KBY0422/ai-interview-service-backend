package com.ict.finalproject.dashboard.adminDashBoard.service;

import com.ict.finalproject.dashboard.adminDashBoard.vo.*;

import java.util.List;

public interface AdminDashBoardService {

    // 상단 4개 카드
    AdminDashBoardCardVO getDashBoardCard();
    //  최근 일주일 매출추세(오늘 포함)
    List<AdminRevenueTrendVO> getRevenueTrend();
    // 직무 top3
    List<AdminTopJobVO> getTopjob();
    // 기술스택 top3
    List<AdminTopSkillVO> getTopSkill();
    // 검색 키워드 top3
    List<AdminTopKeywordVO> getTopKeyword();

}
