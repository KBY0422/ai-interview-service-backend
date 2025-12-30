package com.ict.finalproject.inquery.service;

import com.ict.finalproject.common.security.CustomUserDetails;
import com.ict.finalproject.inquery.vo.InqueryVO;

import java.util.List;
import java.util.Map;

public interface InqueryService {
    List<InqueryVO> getList();
    Map<String, Object> getPageList(int currentPage, int i_m_idx);
    InqueryVO getDetail( String i_idx );
    int getInsert(InqueryVO ivo);
    int getDelete(String i_idx);
    int getUpdate(InqueryVO ivo);
    int getUpdateResponse(InqueryVO ivo);
}
