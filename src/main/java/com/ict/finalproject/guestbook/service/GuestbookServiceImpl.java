package com.ict.finalproject.guestbook.service;

import com.ict.finalproject.common.paging.Paging;
import com.ict.finalproject.guestbook.mapper.GuestbookMapper;
import com.ict.finalproject.guestbook.vo.GuestbookVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class GuestbookServiceImpl implements GuestbookService{

    private final GuestbookMapper guestbookMapper;

    @Override
    public List<GuestbookVO> getList() {
        return guestbookMapper.getList();
    }

    @Override
    public Map<String, Object> getPageList(int currentPage) {
        try {
            // 리스트 구하기전에 미리 전체 레코드 구한다.
            int total = guestbookMapper.getTotalRecord();
            // 페이징 기법
            Paging paging = new Paging(currentPage, total, 5, 3);
            Map<String, Object> map = new HashMap<>();
            map.put("start", paging.getStartIndex());
            map.put("limit", paging.getNumPerPage());

            List<GuestbookVO> guestlist = guestbookMapper.getPageList(map);

            Map<String, Object> result = new HashMap<>();
            result.put("paging", paging);
            result.put("guestlist", guestlist );

            return result;
        } catch (Exception e) {
            log.error(e.getMessage());

            return null;
        }
    }

    @Override
    public GuestbookVO getDetail(String g_idx) {
        return guestbookMapper.getDetail(g_idx);
    }

    @Override
    public int getInsert(GuestbookVO gvo) {
        gvo.setG_writedate(LocalDateTime.now().toString());
        return guestbookMapper.getInsert(gvo);
    }

    @Override
    public int getDelete(String g_idx) {
        return guestbookMapper.getDelete(g_idx);
    }
}
