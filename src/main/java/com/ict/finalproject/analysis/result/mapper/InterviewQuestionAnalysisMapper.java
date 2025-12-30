package com.ict.finalproject.analysis.result.mapper;

import com.ict.finalproject.analysis.result.vo.InterviewQuestionAnalysisVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InterviewQuestionAnalysisMapper {

    /*
     질문 단위 AI 분석 결과를 저장한다
     */
    int insertQuestionAnalysis(InterviewQuestionAnalysisVO vo);

    /*
     세션 기준 질문 분석 결과를 조회한다
     PDF 생성이나 재분석 시 사용 가능하다
     */
    List<InterviewQuestionAnalysisVO> selectBySessionIdx(int sIdx);
}
