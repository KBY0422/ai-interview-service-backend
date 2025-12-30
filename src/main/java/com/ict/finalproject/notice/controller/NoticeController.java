package com.ict.finalproject.notice.controller;

import com.ict.finalproject.common.vo.DataVO;
import com.ict.finalproject.notice.service.NoticeService;
import com.ict.finalproject.notice.vo.NoticeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/list")
    public ResponseEntity<DataVO> getNoticeList(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "2") int type) {  // 1: 관리자, 2: 사용자)
        try {
            Map<String, Object> list = noticeService.getPageList(currentPage, type);
            if(list == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DataVO.fail("데이터 없음"));
            }else{
                return ResponseEntity.ok(DataVO.success(list, "성공"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataVO.fail(e.getMessage()));
        }
    }

    @PostMapping("/detail")
    public ResponseEntity<DataVO> getNoticeDetail(@RequestBody String n_idx){
        try{
            NoticeVO nvo = noticeService.getDetail(n_idx);

            int result = noticeService.getUpdateHit(nvo);
            if(nvo == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DataVO.fail("데이터 없음"));
            }else{
                return ResponseEntity.ok(DataVO.success(nvo, "성공"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataVO.fail(e.getMessage()));
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<DataVO> getNoticeInsert(NoticeVO nvo){
        try {
            log.info("nvo.getN_pin(): {}", nvo.getN_pin());
            // 1. 추가된 체크 로직: 고정글 설정(1)을 시도하는 경우
// 고정PIN 제한 없앰
//            if ("1".equals(nvo.getN_pin())) {
//                int pinnedCount = noticeService.getPinnedCount(); // 현재 고정된 개수 조회
//
//                log.info("pinnedCount: {}", pinnedCount);
//
//                if (pinnedCount >= 2) {
//                    noticeService.clearPinnedNotice(); // 2개 이상이면 기존 고정 초기화
//                }
//            }

            int result = noticeService.getInsert(nvo);
            if(result > 0){
                return ResponseEntity.ok(DataVO.success(result, "성공"));
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(DataVO.fail("DB 저장실패"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataVO.fail(e.getMessage()));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<DataVO> getNoticeDelete(NoticeVO nvo){
        try{
            int result = noticeService.getDelete(nvo);
            if(result > 0){
                List<NoticeVO> list = noticeService.getList();
                if(list == null){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(DataVO.fail("DB 저장실패"));
                }else{
                    return ResponseEntity.ok(DataVO.success(result, "성공"));
                }
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(DataVO.fail("DB 저장실패"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataVO.fail(e.getMessage()));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<DataVO> getNoticeUpdate(NoticeVO nvo){
        try{
            // 추가: 수정 시에도 상단 고정으로 상태를 변경하는 경우 개수 체크
// 고정PIN 제한 없앰
//            if ("1".equals(nvo.getN_pin())) {
//                if (noticeService.getPinnedCount() >= 2) {
//                    noticeService.clearPinnedNotice();
//                }
//            }

            int result = noticeService.getUpdate(nvo);
            if(result > 0){
                List<NoticeVO> list = noticeService.getList();
                if(list == null){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(DataVO.fail("DB 저장실패"));
                }else{
                    return ResponseEntity.ok(DataVO.success(result, "성공"));
                }
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(DataVO.fail("DB 저장실패"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataVO.fail(e.getMessage()));
        }
    }
}