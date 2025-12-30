package com.ict.finalproject.resume.service;

import com.ict.finalproject.resume.vo.ResumeTokenVO;
import com.ict.finalproject.resume.vo.ResumeVO;

public interface ResumeService {
    int plusCount(ResumeVO resumeVO);
    int insertResumeToken(ResumeTokenVO resumeTokenVO);
    int updateResumeToken(ResumeTokenVO resumeTokenVO);
    Integer findLatestResumeIdx(Integer mIdx);
}