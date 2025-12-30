package com.ict.finalproject.dashboard.userDashBoard.service;
import com.ict.finalproject.dashboard.userDashBoard.mapper.UserDashBoardMapper;
import com.ict.finalproject.dashboard.userDashBoard.vo.UserDashBoardSummaryVO;
import com.ict.finalproject.dashboard.userDashBoard.vo.UserInterviewSkillRadarVO;
import com.ict.finalproject.dashboard.userDashBoard.vo.UserRecentInterviewScoreTrendVO;
import com.ict.finalproject.dashboard.userDashBoard.vo.UserRecentInterviewSummaryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDashBoardServiceImpl implements UserDashBoardService {

    private final UserDashBoardMapper userDashBoardMapper;

    // 맨위 4개 카드에 들어가는 값들
    @Override
    public UserDashBoardSummaryVO getBoardSummary(int m_idx) {
        UserDashBoardSummaryVO  userDashBoardSummaryVO = new UserDashBoardSummaryVO();

        // 카드 4개에 들어가는 값 가져오기.
        int totalInterviewCount = userDashBoardMapper.getTotalInterviewCount(m_idx);
        int totalResumeCount = userDashBoardMapper.getTotalResumeCount(m_idx);
        Integer averageScore = userDashBoardMapper.getAverageScore(m_idx);
        Integer bestScore = userDashBoardMapper.getBestScore(m_idx);
        // 하나의 VO에 넣어서 넘겨주기.
        userDashBoardSummaryVO.setTotalInterviewCount(totalInterviewCount);
        userDashBoardSummaryVO.setTotalResumeCount(totalResumeCount);
        userDashBoardSummaryVO.setAverageScore(averageScore == null ? 0 : averageScore);
        userDashBoardSummaryVO.setBestScore(bestScore == null ? 0 : bestScore);

        return userDashBoardSummaryVO;
    }

    // Radar 표에 들어가는 5개의 값들
    @Override
    public UserInterviewSkillRadarVO getInterviewSkillRadar(int m_idx) {

        UserInterviewSkillRadarVO userInterviewSkillRadarVO = userDashBoardMapper.getInterviewSkillRadar(m_idx);

        if(userInterviewSkillRadarVO == null){
            userInterviewSkillRadarVO =  new UserInterviewSkillRadarVO();
            userInterviewSkillRadarVO.setTechScore(0);
            userInterviewSkillRadarVO.setLogicScore(0);
            userInterviewSkillRadarVO.setSoftScore(0);
            userInterviewSkillRadarVO.setLevelAvg(0);
            userInterviewSkillRadarVO.setAccuracyRate(0);
        }else {
            // 난이도와 오답률은 0~100점 단위로 다시 바꿔서 넘겨주기.
            double LevelAvg = userInterviewSkillRadarVO.getLevelAvg();
            double wrongRate = userInterviewSkillRadarVO.getAccuracyRate();

            // 난이도 1.00 ~ 5.00 → 0 ~ 100
            double levelScore = ((LevelAvg - 1.0) / 4.0) * 100.0;
            levelScore = Math.max(0, Math.min(100, levelScore));
            // 정확도 (오답률에서 계산)
            double accuracy = 100.0 - wrongRate;
            accuracy = Math.max(0, Math.min(100, accuracy));
            // 값들 변환해주고 다시 VO에 저장해주기
            userInterviewSkillRadarVO.setLevelAvg((int) Math.round(levelScore));
            userInterviewSkillRadarVO.setAccuracyRate((int) Math.round(accuracy));
        }

        return userInterviewSkillRadarVO;
    }

    @Override
    public List<UserRecentInterviewScoreTrendVO> getRecentInterviewScore(int m_idx) {
        List<UserRecentInterviewScoreTrendVO> userRecentInterviewScoreTrendList = userDashBoardMapper.getRecentInterviewScore(m_idx);

        return userRecentInterviewScoreTrendList;
    }

    @Override
    public List<UserRecentInterviewSummaryVO> getRecentInterviewSummary(int m_idx) {
        List<UserRecentInterviewSummaryVO> userRecentInterviewSummaryList = userDashBoardMapper.getRecentInterviewSummary(m_idx);

        //Date 양식 고쳐주기 년.월.일 만 나오게

        return userRecentInterviewSummaryList;
    }
}
