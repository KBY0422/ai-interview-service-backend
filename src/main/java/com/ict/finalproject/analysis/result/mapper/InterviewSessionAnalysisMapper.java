package com.ict.finalproject.analysis.result.mapper;

import com.ict.finalproject.analysis.result.service.InterviewSessionAnalysisServiceImpl;
import com.ict.finalproject.analysis.result.vo.InterviewSessionAnalysisVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InterviewSessionAnalysisMapper {

    InterviewSessionAnalysisVO selectBySessionIdx(
            @Param("sIdx") int sIdx,
            @Param("mIdx") int mIdx
    );

    int insertSessionAnalysis(InterviewSessionAnalysisVO vo);

    String selectJobBySessionIdx(@Param("sIdx") int sIdx);

    // ✅ [추가] token_usage_log (SESSION) 저장
    int updateSessionTokenUsage(InterviewSessionAnalysisServiceImpl.TokenUsageLogVO vo);

}
