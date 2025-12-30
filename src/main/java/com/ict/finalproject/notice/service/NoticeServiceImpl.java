package com.ict.finalproject.notice.service;

import com.ict.finalproject.common.paging.Paging;
import com.ict.finalproject.notice.mapper.NoticeMapper;
import com.ict.finalproject.notice.vo.NoticeVO;
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
public class NoticeServiceImpl implements NoticeService{

    private final NoticeMapper noticeMapper;
    @Override
    public List<NoticeVO> getList() {
        return noticeMapper.getList();
    }

    @Override
    public Map<String, Object> getPageList(int currentPage, int admin) {
        try {
            // 리스트 구하기전에 미리 전체 레코드 구한다.
            int total = 0;
            if(admin == 1)
                total = noticeMapper.getTotalRecordAdmin();
            else
                total = noticeMapper.getTotalRecord();
            // 페이징 기법
            Paging paging = new Paging(currentPage, total, 5, 3);
            Map<String, Object> map = new HashMap<>();
            map.put("start", paging.getStartIndex());
            map.put("limit", paging.getNumPerPage());

            List<NoticeVO> noticelist = null;
            if(admin == 1)
                noticelist = noticeMapper.getPageListAdmin(map);
            else
                noticelist = noticeMapper.getPageList(map);
            Map<String, Object> result = new HashMap<>();
            result.put("paging", paging);
            result.put("noticelist", noticelist );

            return result;
        } catch (Exception e) {
            log.error(e.getMessage());

            return null;
        }
    }

    @Override
    public NoticeVO getDetail(String n_idx) {
        return noticeMapper.getDetail(n_idx);
    }

    @Override
    public int getInsert(NoticeVO nvo) {
        nvo.setN_writedate(LocalDateTime.now().toString());
        return noticeMapper.getInsert(nvo);
    }

    @Override
    public int getDelete(NoticeVO vo) {
        return noticeMapper.getDelete(vo);
    }

    @Override
    public int getUpdate(NoticeVO vo) {
        return noticeMapper.getUpdate(vo);
    }

    @Override
    public int getUpdateHit(NoticeVO nvo) {
        return noticeMapper.getUpdateHit(nvo);
    }

    @Override
    public int getPinnedCount() {
        return noticeMapper.getPinnedCount();
    }
    @Override
    public void clearPinnedNotice() {
        noticeMapper.clearPinnedNotice();
    }

}