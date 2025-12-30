package com.ict.finalproject.resume.mapper;

import com.ict.finalproject.resume.vo.ResumeTokenVO;
import com.ict.finalproject.resume.vo.ResumeVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResumeMapper {
    int plusCount(ResumeVO resumeVO);

    Integer selectLatestResumeIdx(Integer mIdx);
}