package com.ict.finalproject.analysis.result.service;

import com.ict.finalproject.analysis.result.dto.InterviewResultResponseDTO;
import com.ict.finalproject.analysis.result.vo.InterviewSessionAnalysisVO;

public interface InterviewSessionAnalysisService {

    /*
     세션 분석 원본 데이터를 직접 조회할 때 사용한다.
     PDF 생성 등에서 재사용할 수 있다.
     */
    InterviewSessionAnalysisVO getSessionAnalysis(int sIdx, int mIdx);

    /*
     세션 기준으로 질문과 답변을 조회하고
     AI 분석이나 테스트용 더미 분석을 수행하고
     분석 결과를 DB에 저장한 뒤
     최종 결과를 JSON 형태로 반환한다.
     프론트와 PDF에서 공통으로 사용하는 진입점이다.
     */
    InterviewResultResponseDTO getInterviewResult(int sIdx, int mIdx);
}
