package com.ict.finalproject.analysis.list.mapper;

import com.ict.finalproject.analysis.list.vo.InterviewAnalysisListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InterviewAnalysisListMapper {
    List<InterviewAnalysisListVO> getAnalysisListByMember(int mIdx);
}
