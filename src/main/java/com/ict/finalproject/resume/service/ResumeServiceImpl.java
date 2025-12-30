package com.ict.finalproject.resume.service;

import com.ict.finalproject.resume.mapper.ResumeMapper;
import com.ict.finalproject.resume.mapper.ResumeTokenMapper;
import com.ict.finalproject.resume.vo.ResumeTokenVO;
import com.ict.finalproject.resume.vo.ResumeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeMapper resumeMapper;

    private final ResumeTokenMapper resumeTokenMapper;

    @Override
    public int plusCount(ResumeVO resumeVO) {
        return resumeMapper.plusCount(resumeVO);
    }

    @Override
    public Integer findLatestResumeIdx(Integer mIdx) {
        return resumeMapper.selectLatestResumeIdx(mIdx);
    }

    @Override
    public int insertResumeToken(ResumeTokenVO resumeTokenVO) {
        return resumeTokenMapper.insertResumeToken(resumeTokenVO);
    }

    @Override
    public int updateResumeToken(ResumeTokenVO resumeTokenVO) {
        return resumeTokenMapper.updateResumeToken(resumeTokenVO);
    }


}