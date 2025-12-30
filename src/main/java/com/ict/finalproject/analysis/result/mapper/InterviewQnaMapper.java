package com.ict.finalproject.analysis.result.mapper;

import com.ict.finalproject.analysis.result.vo.InterviewQnaVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InterviewQnaMapper {

    /*
     세션 기준으로 질문 목록을 조회한다
     추후 답변 컬럼이 추가되면 함께 조회하도록 확장한다
     */
    List<InterviewQnaVO> selectBySessionIdx(int sIdx);
}
