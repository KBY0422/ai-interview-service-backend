package com.ict.finalproject.dashboard.adminDashBoard.mapper;


import com.ict.finalproject.dashboard.adminDashBoard.vo.AdminRevenueTrendRawVO;
import com.ict.finalproject.dashboard.adminDashBoard.vo.AdminTopJobVO;
import com.ict.finalproject.dashboard.adminDashBoard.vo.AdminTopKeywordVO;
import com.ict.finalproject.dashboard.adminDashBoard.vo.AdminTopSkillVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminDashBoardMapper {

    Integer getTotalUserCount();
    Integer getTodayInterview();
    Integer getTodayResume();
    List<AdminRevenueTrendRawVO> getTodayRevenue();

    //  최근 일주일 매출추세(오늘 포함)
    List<AdminRevenueTrendRawVO> getRevenueTrend();
    // 직무 top3
    List<AdminTopJobVO> getTopjob();
    // 기술스택 top3
    List<AdminTopSkillVO> getTopSkill();
    // 검색 키워드 top3
    List<AdminTopKeywordVO> getTopKeyword();

}
