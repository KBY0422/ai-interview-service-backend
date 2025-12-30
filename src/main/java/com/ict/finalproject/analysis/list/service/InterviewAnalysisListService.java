package com.ict.finalproject.analysis.list.service;

import com.ict.finalproject.analysis.list.vo.InterviewAnalysisListVO;

import java.util.List;

public interface InterviewAnalysisListService {

    /**
     * 회원별 면접 분석 목록 조회
     */
    List<InterviewAnalysisListVO> getAnalysisList(int mIdx);
}
