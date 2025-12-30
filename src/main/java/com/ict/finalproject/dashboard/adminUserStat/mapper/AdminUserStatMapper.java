package com.ict.finalproject.dashboard.adminUserStat.mapper;
import com.ict.finalproject.dashboard.adminUserStat.vo.DailySignupTrendVO;
import com.ict.finalproject.dashboard.adminUserStat.vo.MonthlyInactiveUserStatVO;
import com.ict.finalproject.dashboard.adminUserStat.vo.MonthlyJoinCountVO;
import com.ict.finalproject.dashboard.adminUserStat.vo.MonthlyUserCountVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminUserStatMapper {

    // 상위4개 카드
    Integer getTotalUser();
    Integer getTodaySignup() ;
    Integer getWeekSignup() ;
    Integer getActiveUserCount();


    //최근 7일가입수 동향
    List<DailySignupTrendVO> getDailySignup();

    //월별 전체 회원가입수 동향
    List<MonthlyUserCountVO> getMonthlyUser();

    //해당월의 탈퇴회원수 가져오기
    List<MonthlyInactiveUserStatVO> getActiveUser();

    // 해당 월의 회원 가입수 가져오기 
     List<MonthlyJoinCountVO> getJoinCountVO();

}
