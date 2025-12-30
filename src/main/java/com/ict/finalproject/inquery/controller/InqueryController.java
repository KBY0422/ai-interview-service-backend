package com.ict.finalproject.inquery.controller;

import com.ict.finalproject.common.security.CustomUserDetails;
import com.ict.finalproject.common.vo.DataVO;
import com.ict.finalproject.inquery.service.InqueryService;
import com.ict.finalproject.inquery.vo.InqueryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/inquery")
public class InqueryController {

    private final InqueryService inqueryService;

    @GetMapping("/list")
    public ResponseEntity<DataVO> getInqueryList(@RequestParam(defaultValue = "1") int currentPage
            , @AuthenticationPrincipal CustomUserDetails user) {
        try {
            if (user == null) {
                //return new DashDataVO<>(false, "로그인이 필요합니다.", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DataVO.fail("로그인이 필요합니다."));
            }
            int i_m_idx = user.getMIdx(); // ★ 여기서 m_idx 확보

            log.info("m_idx : " + i_m_idx);

            Map<String, Object> list = inqueryService.getPageList(currentPage, i_m_idx);
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
    public ResponseEntity<DataVO> getInqueryDetail(@RequestBody String i_idx){
        try{
            InqueryVO gvo = inqueryService.getDetail(i_idx);

            if(gvo == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DataVO.fail("데이터 없음"));
            }else{
                return ResponseEntity.ok(DataVO.success(gvo, "성공"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataVO.fail(e.getMessage()));
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<DataVO> getInqueryInsert(InqueryVO ivo,
                                                   @AuthenticationPrincipal CustomUserDetails user){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.isAuthenticated()
                    && !(auth instanceof AnonymousAuthenticationToken)) {
                ivo.setI_writer(auth.getName());

                int m_idx = user.getMIdx(); // ★ 여기서 m_idx 확보
                ivo.setI_m_idx(String.valueOf(m_idx));
            }
            int result = inqueryService.getInsert(ivo);
            if(result > 0){
                return ResponseEntity.ok(DataVO.success(result, "성공"));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DataVO.fail("데이터 없음"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataVO.fail(e.getMessage()));
        }
    }

    @Transactional
    @PostMapping("/delete")
    public ResponseEntity<DataVO> getInqueryDelete(
            @RequestBody InqueryVO ivo,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        try {
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(DataVO.fail("로그인이 필요합니다."));
            }

            InqueryVO vo = inqueryService.getDetail(ivo.getI_idx());
            if (vo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DataVO.fail("문의사항 없음"));
            }

            // 🔥 작성자 검증 (핵심)
            int loginMIdx = user.getMIdx();
            if (!String.valueOf(loginMIdx).equals(vo.getI_m_idx())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(DataVO.fail("본인 문의만 삭제할 수 있습니다."));
            }

            // 비밀번호 검증
            if (!vo.getI_pwd().equals(ivo.getI_pwd())) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                        .body(DataVO.fail("비밀번호가 일치하지 않습니다."));
            }

            inqueryService.getDelete(vo.getI_idx());
            return ResponseEntity.ok(DataVO.success(null, "삭제 성공"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataVO.fail(e.getMessage()));
        }
    }


    @Transactional
    @PostMapping("/update")
    public ResponseEntity<DataVO> getInqueryUpdate(
            InqueryVO ivo,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        try {
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(DataVO.fail("로그인이 필요합니다."));
            }

            InqueryVO vo = inqueryService.getDetail(ivo.getI_idx());
            if (vo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DataVO.fail("문의사항 없음"));
            }

            // 🔥 작성자 검증
            if (!String.valueOf(user.getMIdx()).equals(vo.getI_m_idx())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(DataVO.fail("본인 문의만 수정할 수 있습니다."));
            }

            if (!vo.getI_pwd().equals(ivo.getI_pwd())) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                        .body(DataVO.fail("비밀번호가 일치하지 않습니다."));
            }

            inqueryService.getUpdate(ivo);
            return ResponseEntity.ok(DataVO.success(null, "수정 성공"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataVO.fail(e.getMessage()));
        }
    }


    @Transactional
    @PostMapping("/response")
    public ResponseEntity<DataVO> getInqueryResponse(@RequestBody InqueryVO ivo){
        try{
            InqueryVO vo = inqueryService.getDetail(ivo.getI_idx());
            vo.setI_response(ivo.getI_response());

            int result = inqueryService.getUpdateResponse(vo);
            if(result > 0){
                List<InqueryVO> list = inqueryService.getList();
                if(list == null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(DataVO.fail("데이터 없음"));
                }else{
                    return ResponseEntity.ok(DataVO.success(list, "성공"));
                }
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DataVO.fail("데이터 없음"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataVO.fail(e.getMessage()));
        }
    }
}
