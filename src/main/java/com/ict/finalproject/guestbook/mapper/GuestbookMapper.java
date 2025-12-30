package com.ict.finalproject.guestbook.mapper;

import com.ict.finalproject.guestbook.vo.GuestbookVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface GuestbookMapper {
    List<GuestbookVO> getList();
    GuestbookVO getDetail(String g_idx);
    int getInsert(GuestbookVO gvo);
    int getDelete(String g_idx);

    int getTotalRecord();
    List<GuestbookVO> getPageList(Map<String, Object> map);
}
