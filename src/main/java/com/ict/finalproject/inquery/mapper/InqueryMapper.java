package com.ict.finalproject.inquery.mapper;

import com.ict.finalproject.inquery.vo.InqueryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface InqueryMapper {
    List<InqueryVO> getList();
    InqueryVO getDetail(String i_idx);
    int getInsert(InqueryVO ivo);
    int getDelete(String i_idx);

    int getTotalRecord();
    List<InqueryVO> getPageList(Map<String, Object> map);
    int getUpdate(InqueryVO ivo);
    int getUpdateResponse(InqueryVO ivo);
}
