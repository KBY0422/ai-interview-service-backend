package com.ict.finalproject.resume.mapper;

import com.ict.finalproject.resume.vo.ResumeTokenVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResumeTokenMapper {

    int insertResumeToken(ResumeTokenVO resumeTokenVO);
    int updateResumeToken(ResumeTokenVO resumeTokenVO);
}