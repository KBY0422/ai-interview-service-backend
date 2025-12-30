package com.ict.finalproject.dashboard.userDashBoard.controller;
import com.ict.finalproject.common.security.CustomUserDetails;
import com.ict.finalproject.common.vo.DataVO;
import com.ict.finalproject.dashboard.userDashBoard.service.UserDashBoardService;
import com.ict.finalproject.dashboard.userDashBoard.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserDashBoardController {

    private final UserDashBoardService userDashBoardService;

    //Spring Security + JWT 에 저장되어있는 사용자 m_idx 빼오기
    //혹은 session에 저장되어있는것을 가져온다거나?

    @PostMapping("/dashboard")
    public DataVO<UserDashBoardResponseVO> getDashboard(
            @AuthenticationPrincipal CustomUserDetails user){

        try {
            if (user == null) {
                return new DataVO<>(false, null,"로그인이 필요합니다.");
            }
            int m_idx = user.getMIdx(); // ★ 여기서 m_idx 확보

            UserDashBoardResponseVO response = new  UserDashBoardResponseVO();
            // 사용자 대시보드에 들어가는 각 데이터 값 가져오기
            UserDashBoardSummaryVO summaryVO= userDashBoardService.getBoardSummary(m_idx);
            UserInterviewSkillRadarVO interviewSkillRadarVO = userDashBoardService.getInterviewSkillRadar(m_idx);
            List<UserRecentInterviewScoreTrendVO> recentInterviewScoreTrendVO = userDashBoardService.getRecentInterviewScore(m_idx);
            List<UserRecentInterviewSummaryVO> recentInterviewSummaryVO = userDashBoardService.getRecentInterviewSummary(m_idx);


            // react로 보낼때 하나의 VO로 넘겨주기.
            response.setUserDashBoardSummaryVO(summaryVO);
            response.setUserInterviewSkillRadarVO(interviewSkillRadarVO);
            response.setUserRecentInterviewScoreTrendVO(recentInterviewScoreTrendVO);
            response.setUserRecentInterviewSummaryVO(recentInterviewSummaryVO);


            return new DataVO<>(true, response, "대시보드 조회 성공");
        } catch (Exception e) {
            log.error("User Dashboard 조회 실패", e);
            return new DataVO<>(false, null, "대시보드 조회 실패");
        }



    }





}
