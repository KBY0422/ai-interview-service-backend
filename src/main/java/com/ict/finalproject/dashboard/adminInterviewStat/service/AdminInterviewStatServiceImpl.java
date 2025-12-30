package com.ict.finalproject.dashboard.adminInterviewStat.service;

import com.ict.finalproject.dashboard.adminInterviewStat.mapper.AdminInterviewStatMapper;
import com.ict.finalproject.dashboard.adminInterviewStat.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminInterviewStatServiceImpl implements AdminInterviewStatService {

    private final AdminInterviewStatMapper adminInterviewStatMapper;

    // 위의 카드4개.
    @Override
    public InterviewCardStatVO getInterviewCard() {
        InterviewCardStatVO  interviewCardStatVO = new InterviewCardStatVO();

        Integer todayInterviewCount = adminInterviewStatMapper.getTodayInterviewCount();
        Integer weekInterviewCount = adminInterviewStatMapper.getWeekInterviewCount();
        Integer monthInterviewCount = adminInterviewStatMapper.getMonthInterviewCount();
        // 사용자 전체 평균점수 내기
        Integer averageScore = adminInterviewStatMapper.getAverageScore();

        interviewCardStatVO.setTodayInterviewCount(todayInterviewCount  == null ? 0 : todayInterviewCount);
        interviewCardStatVO.setWeekInterviewCount(weekInterviewCount == null ? 0 : weekInterviewCount);
        interviewCardStatVO.setMonthInterviewCount(monthInterviewCount == null ? 0 : monthInterviewCount);
        interviewCardStatVO.setAverageScore(averageScore == null ? 0 : averageScore);

        return interviewCardStatVO;
    }
    // 3개월 이내 직종 과 직종선택 횟수 가져오기
    @Override
    public List<JobRatioVO> getJobRatio() {
        List<JobRatioVO> list = adminInterviewStatMapper.getJobRatio();
        return list;
    }
    //기술스택 가져오기
    @Override
    public List<JobSkillRatioVO> getSkillRatio() {


        List<JobSkillRatioVO> list = adminInterviewStatMapper.getJobSkillRatio();

        Map<String, List<JobSkillRatioVO>> grouped =
                list.stream().collect(Collectors.groupingBy(JobSkillRatioVO::getJob));

        List<JobSkillRatioVO> result = new ArrayList<>();

        for (Map.Entry<String, List<JobSkillRatioVO>> entry : grouped.entrySet()) {
            List<JobSkillRatioVO> sorted =
                    entry.getValue().stream()
                            .sorted(Comparator.comparingInt(JobSkillRatioVO::getCount).reversed())
                            .toList();

            int etcCount = 0;

            for (int i = 0; i < sorted.size(); i++) {
                if (i < 5) {
                    result.add(sorted.get(i));
                } else {
                    etcCount += sorted.get(i).getCount();
                }
            }

            if (etcCount > 0) {
                result.add(new JobSkillRatioVO(entry.getKey(), "기타", etcCount));
            }
        }

        return result;

    }
    //면접유형 선택 비율
    @Override
    public List<InterviewTypeRatioVO> getInterviewTypeRatio() {
        return adminInterviewStatMapper.getInterviewTypeRatio();
    }

    //최근 7일간 면접수 변화
    @Override
    public List<DailyInterviewCountVO> getDailyInterviewCount() {
        // 날짜 정제해서 보내주기.
        List<DailyInterviewCountVO> raw = adminInterviewStatMapper.getDailyInterviewCount();

        Map<String,Integer> map = raw.stream()
                .collect(Collectors.toMap(
                        DailyInterviewCountVO::getDate,
                        DailyInterviewCountVO::getCount
                ));

        List<DailyInterviewCountVO> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            String key = d.toString();

            DailyInterviewCountVO vo = new DailyInterviewCountVO();
            vo.setDate(key);
            vo.setCount(map.getOrDefault(key, 0));
            result.add(vo);
        }

        return result;
    }

    //월별 면접수 변화 // 최근 1년
    @Override
    public List<MonthlyInterviewTrendVO> getMonthlyInterviewCount() {

        return   adminInterviewStatMapper.getMonthlyInterviewCount();
    }

    //최근 7일간 평균 난이도 변화
    @Override
    public List<DailyAvgDifficultyVO> getDailyAvgDifficuilty() {

        List<DailyAvgDifficultyVO> raw =
                adminInterviewStatMapper.getDailyAvgDifficuilty();

        Map<String, Double> map = raw.stream()
                .collect(Collectors.toMap(
                        DailyAvgDifficultyVO::getDate,
                        DailyAvgDifficultyVO::getAvgDifficulty
                ));

        List<DailyAvgDifficultyVO> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            String key = d.toString();

            double value = map.getOrDefault(key, 0.0);
            double rounded = Math.round(value * 100) / 100.0;

            DailyAvgDifficultyVO vo = new DailyAvgDifficultyVO();
            vo.setDate(key);
            vo.setAvgDifficulty(rounded);
            result.add(vo);
        }

        return result;
    }

    //최근 7일 간 평균점수 변화
    @Override
    public List<DailyAvgScoreVO> getDailyAvgScore() {
        // 날짜 정제해서 보내주기.
        List<DailyAvgScoreVO> raw = adminInterviewStatMapper.getDailyAvgScore();

        Map<String,Integer> map = raw.stream()
                .collect(Collectors.toMap(
                        DailyAvgScoreVO::getDate,
                        DailyAvgScoreVO::getAvgScore
                ));

        List<DailyAvgScoreVO> result = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            String key = d.toString();

            DailyAvgScoreVO vo = new DailyAvgScoreVO();
            vo.setDate(key);
            vo.setAvgScore(map.getOrDefault(key, 0));
            result.add(vo);
        }

        return result;
    }
}
