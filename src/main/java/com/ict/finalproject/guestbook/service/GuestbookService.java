package com.ict.finalproject.guestbook.service;

import com.ict.finalproject.guestbook.vo.GuestbookVO;

import java.util.List;
import java.util.Map;

public interface GuestbookService {
    List<GuestbookVO> getList();
    Map<String, Object> getPageList(int currentPage);
    GuestbookVO getDetail( String g_idx );
    int getInsert(GuestbookVO gvo);
    int getDelete(String g_idx);
}
