package com.ict.finalproject.guestbook.controller;

import com.ict.finalproject.common.vo.DataVO;
import com.ict.finalproject.guestbook.service.GuestbookService;
import com.ict.finalproject.guestbook.vo.GuestbookVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/guestbook")
public class GuestbookController {

    private final GuestbookService guestbookService;

    @GetMapping("/list")
    public ResponseEntity<DataVO> getGuestbookList(@RequestParam(defaultValue = "1") int currentPage) {
        try {
            Map<String, Object> list = guestbookService.getPageList(currentPage);
            if (list == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DataVO.fail("데이터 없음"));
            }
            return ResponseEntity.ok(DataVO.success(list, "성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataVO.fail(e.getMessage()));
        }
    }

    @PostMapping("/detail")
    public ResponseEntity<DataVO> getGuestbookDetail(@RequestBody String g_idx) {
        try {
            GuestbookVO gvo = guestbookService.getDetail(g_idx);
            if (gvo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DataVO.fail("데이터 없음"));
            }
            return ResponseEntity.ok(DataVO.success(gvo, "성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataVO.fail(e.getMessage()));
        }
    }

    @Transactional
    @PostMapping("/insert")
    public ResponseEntity<DataVO> getGuestbookInsert(GuestbookVO gvo) {
        try {
            // 핵심 로직
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.isAuthenticated()
                    && !(auth instanceof AnonymousAuthenticationToken)) {
                gvo.setG_writer(auth.getName());   // 로그인 사용자
            } else {
                gvo.setG_writer("GUEST");          // 비회원
            }

            int result = guestbookService.getInsert(gvo);

            if (result > 0) {
                return ResponseEntity.ok(DataVO.success(result, "성공"));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataVO.fail("DB 저장 실패"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataVO.fail(e.getMessage()));
        }
    }

    @Transactional
    @PostMapping("/delete")
    public ResponseEntity<DataVO> getGuestbookDelete(@RequestParam String g_idx) {
        try {
            int result = guestbookService.getDelete(g_idx);
            if (result > 0) {
                return ResponseEntity.ok(DataVO.success(result, "성공"));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataVO.fail("DB 삭제 실패"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataVO.fail(e.getMessage()));
        }
    }
}