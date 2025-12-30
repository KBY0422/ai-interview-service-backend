package com.ict.finalproject.notice.service;

import com.ict.finalproject.notice.vo.NoticeVO;

import java.util.List;
import java.util.Map;

public interface NoticeService {
    List<NoticeVO> getList();
    Map<String, Object> getPageList(int currentPage, int admin);
    NoticeVO getDetail( String n_idx );
    int getInsert(NoticeVO nvo);
    int getDelete(NoticeVO vo);
    int getUpdate(NoticeVO vo);
    int getUpdateHit(NoticeVO nvo);

    int getPinnedCount();                       // 상단공지 개수 확인
    void clearPinnedNotice();                   // 기존 상단공지 해제
}