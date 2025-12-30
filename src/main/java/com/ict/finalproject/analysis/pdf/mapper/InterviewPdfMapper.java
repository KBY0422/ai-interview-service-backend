package com.ict.finalproject.analysis.pdf.mapper;

import com.ict.finalproject.analysis.pdf.vo.InterviewPdfDataVO;
import com.ict.finalproject.analysis.pdf.vo.InterviewPdfVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InterviewPdfMapper {

    InterviewPdfDataVO selectPdfData(@Param("sIdx") int sIdx);

    InterviewPdfVO findActiveBySessionIdx(@Param("sIdx") int sIdx);

    int insertPdf(InterviewPdfVO vo);

    int increaseDownloadCount(@Param("sIdx") int sIdx);

    // 세션 기준 기존 active 전부 비활성화
    int deactivateActiveBySession(@Param("sIdx") int sIdx);
}
