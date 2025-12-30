package com.ict.finalproject.analysis.list.service;

import com.ict.finalproject.analysis.list.mapper.InterviewAnalysisListMapper;
import com.ict.finalproject.analysis.list.vo.InterviewAnalysisListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewAnalysisListServiceImpl implements InterviewAnalysisListService {

    private final InterviewAnalysisListMapper mapper;

    @Override
    public List<InterviewAnalysisListVO> getAnalysisList(int mIdx) {
        return mapper.getAnalysisListByMember(mIdx);
    }
}
