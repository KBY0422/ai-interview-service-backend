package com.ict.finalproject.notice.mapper;

import com.ict.finalproject.notice.vo.NoticeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeMapper {
    List<NoticeVO> getList();
    NoticeVO getDetail( String n_idx );
    int getInsert(NoticeVO nvo);
    int getDelete(NoticeVO vo);
    int getUpdate(NoticeVO vo);

    int getTotalRecord();
    int getTotalRecordAdmin();
    List<NoticeVO> getPageList(Map<String, Object> map);
    List<NoticeVO> getPageListAdmin(Map<String, Object> map);
    int getUpdateHit(NoticeVO nvo);

    int getPinnedCount();                       // 상단공지 개수 확인
    void clearPinnedNotice();                   // 기존 상단공지 해제
}