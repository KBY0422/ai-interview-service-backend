package com.ict.finalproject.member.controller;

import com.ict.finalproject.analysis.list.service.InterviewAnalysisListService;
import com.ict.finalproject.analysis.list.vo.InterviewAnalysisListVO;
import com.ict.finalproject.common.jwt.JwtUtil;
import com.ict.finalproject.common.security.CustomUserDetails;
import com.ict.finalproject.common.vo.DataVO;
import com.ict.finalproject.guestbook.service.GuestbookService;
import com.ict.finalproject.guestbook.vo.GuestbookVO;
import com.ict.finalproject.inquery.service.InqueryService;
import com.ict.finalproject.inquery.vo.InqueryVO;
import com.ict.finalproject.member.service.MemberService;
import com.ict.finalproject.member.vo.MemberVO;
import com.ict.finalproject.member.vo.RefreshVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final GuestbookService guestbookService;
    private final InqueryService inqueryService;
    private final InterviewAnalysisListService  interviewAnalysisListService;



    @GetMapping("hi")
    public String gethi() {
        return "it's start";
    }

    @PostMapping("/idCheck")
    public Map<String, Object> idCheck(@RequestBody Map<String, String> request){
        String m_id = request.get("m_id");

        boolean exists = memberService.idCheck(m_id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", !exists);
        return response;
    }

    @PostMapping("/sendCode")
    public Map<String, Object> sendCode(@RequestBody Map<String, String> request, HttpSession session){
        String m_email = request.get("m_email");
        MemberVO member = memberService.findByEmail(m_email);
        Map<String, Object> response = new HashMap<>();
        if(member != null) {
            response.put("success", false);
            response.put("message", "이미 사용중인 이메일 입니다.");
        }else {
            boolean success = memberService.sendCode(m_email, session);
            response.put("success", success);
        }
        return response;
    }

    @PostMapping("/verifyCode")
    public Map<String, Object> verifyCode(@RequestBody Map<String, String> request, HttpSession session){
        try {
            String authCode = (String)session.getAttribute("authCode");
            Long saveTime = (Long)session.getAttribute("authTime");
            Map<String, Object> response = new HashMap<>();
            // 5분 유효 시간
            if((System.currentTimeMillis() - saveTime) > (60 * 5 * 1000)){
                response.put("expired", true);
                return response;
            }
            if(authCode.equals(request.get("code"))){
                session.removeAttribute("authCode");
                response.put("success", true);
                return response;
            }
            else{
                response.put("fail", true);
                return response;
            }
        } catch (Exception e) {
            log.info("오류 발생 : {}",  e.getMessage());
            return null;
        }
    }

    @PostMapping("/register")
    public DataVO<Integer> register(@RequestBody MemberVO mvo){
        log.info("회원 정보 : {}", mvo);
        try {
            mvo.setM_pwd(passwordEncoder.encode(mvo.getM_pwd()));

            // 🔥 핵심 추가
            if (mvo.getM_addr1() != null && mvo.getM_addr1().isBlank()) {
                mvo.setM_addr1(null);
            }
            if (mvo.getM_addr2() != null && mvo.getM_addr2().isBlank()) {
                mvo.setM_addr2(null);
            }

            int result = memberService.register(mvo);
            if (result > 0) {
                return DataVO.success(result, "회원가입 성공");
            } else {
                return DataVO.fail("회원가입 실패");
            }
        } catch (Exception e) {
            return DataVO.fail(e.getMessage());
        }
    }

    @PostMapping("/login")
    public DataVO<Map<String, Object>> login(@RequestBody MemberVO mvo){
        // 기존 아이디 가져오기
        MemberVO memberVO = memberService.findById(mvo.getM_id());
        // 아이디 가져오기를 실행한 결과 등록된 회원이 아닐 경우
        if(memberVO == null){
            return DataVO.success(null, "등록된 회원이 아닙니다. 회원가입해주세요");
        }

        // 로그인 성공 ( 비밀번호 일치 && m_active 값이 1 인 경우)
        if (passwordEncoder.matches(mvo.getM_pwd(), memberVO.getM_pwd()) && memberVO.getM_active().equals("1")) {
            String accessToken =jwtUtil.generateAccessToken(String.valueOf(memberVO.getM_id()));
            String refreshToken =jwtUtil.generateRefreshToken(String.valueOf(memberVO.getM_id()));

            // Map에 회원정보, accessToken, refreshToken담기
            Map<String, Object> map = new HashMap<>();
            map.put("memberVO", memberVO);
            map.put("accessToken", accessToken);
            map.put("refreshToken", refreshToken);

            return DataVO.success(map, "로그인 성공");
        } else {
            return DataVO.fail("로그인에 실패했습니다.");
        }
    }

    // accessToken 재발급하기
    @PostMapping("/refresh")
    public DataVO<String> refresh(HttpServletRequest request, HttpServletResponse response){
        try {
            // 쿠키에 있는 refreshToken 가져오기
            String refreshToken = jwtUtil.extractRefreshTokenFromCookie(request);
            if (refreshToken == null) {
                return DataVO.fail("refreshToken 쿠키에 없음");
            } ;

            // refreshToken 만료여부 확인
            if (jwtUtil.isTokenExpired(refreshToken)) {
                return DataVO.fail("refreshToken 만료");
            }
            // refreshToken으로 m_id 추출
            String m_id = jwtUtil.validateAndExtractuserId(refreshToken);
            // refreshToken에 있는 m_id와 DB의 refreshToken 대조하기
            RefreshVO refreshVO = memberService.getRefreshToken(m_id);
            // DB의 refreshToken과 cookie에서 가져온 refreshToken을 비교
            if (refreshVO != null || !refreshToken.equals(refreshVO.getRefreshToken())) {
                return DataVO.fail("refreshToken 없거나 불일치");
            }
            // 일치하는 경우 새로운 accessToken과 refreshToken 발급
            String newAccessToken = jwtUtil.generateAccessToken(String.valueOf(m_id));
            String newRefreshToken = jwtUtil.generateRefreshToken(String.valueOf(m_id));

            // refreshToken을 이용해서 DB 갱신
            memberService.saveRefreshToken(m_id, newRefreshToken, jwtUtil.extractExpiration(refreshToken));

            // refreshToken 쿠키에 다시 넣기
            jwtUtil.addRefreshTokenToCookie(response, newAccessToken);

            return DataVO.success(newAccessToken, "재발급 성공");
        } catch (Exception e) {
            return DataVO.fail("재발급실패");
        }
    }

    @PostMapping("/logout")
    public DataVO<Void>
    logout(HttpServletRequest request, HttpServletResponse response){
        try{
            // 쿠키에서 refreshToken 추출하기
            String refreshToken =jwtUtil.extractRefreshTokenFromCookie(request);
            if(refreshToken != null) {
                // refreshToken으로 유효 여부와 m_id 추출
                String m_id =jwtUtil.validateAndExtractuserId(refreshToken);
            }
            jwtUtil.deleteRefreshTokenCookie(response);
            return DataVO.success(null, "로그아웃 성공");
        } catch (Exception e) {
            return DataVO.fail("로그아웃 실패 : " + e.getMessage());
        }
    }

    @PostMapping("/findId")
    public DataVO<MemberVO> findId(@RequestBody Map<String, String> request){

        Map<String, String> map = new HashMap<>();
        map.put("m_name", request.get("m_name"));
        map.put("m_email", request.get("m_email"));
        MemberVO memberVO =  memberService.findId(map);

        if(memberVO != null && "1".equals(memberVO.getM_active())){
            return DataVO.success(memberVO, "아이디 조회 성공");
        }else if(memberVO != null && "0".equals(memberVO.getM_active())){
            return DataVO.fail("탈퇴한 회원입니다.");
        }else {
            return DataVO.fail("일치하는 회원정보가 없습니다.");
        }
    }

    @PostMapping("/sendPasswordResetCode")
    public Map<String, Object> sendPasswordResetCode(@RequestBody Map<String, String> request, HttpSession session){
        String m_id = request.get("m_id");
        String m_email = request.get("m_email");
        MemberVO memberVO = memberService.findById(m_id);
        Map<String, Object> response = new HashMap<>();

        // 회원이 없거나 비활성 상태이면 바로 반환
        if(memberVO == null || "0".equals(memberVO.getM_active())){
            response.put("success", false);
            response.put("message", "일치하는 회원이 없습니다");
            return response; // 여기서 바로 반환
        }

        // 이메일 일치 여부 확인
        if(m_email != null && m_email.equals(memberVO.getM_email())){
            // 인증번호 발송
            boolean success = memberService.sendPasswordResetCode(m_email, session);
            response.put("success", success);
            if(!success){
                response.put("message", "인증번호 발송 실패");
            }
        } else {
            // 이메일 불일치
            response.put("success", false);
            response.put("message", "회원 정보가 존재하지 않습니다");
        }

        return response;
    }

    @PostMapping("/verifyPasswordResetCode")
    public Map<String, Object> verifyPasswordResetCode(@RequestBody Map<String, String> request, HttpSession session){
        try {
            String ResetCode = (String)session.getAttribute("ResetCode");
            Long saveTime = (Long)session.getAttribute("authTime");
            Map<String, Object> response = new HashMap<>();
            // 5분 유효 시간
            if((System.currentTimeMillis() - saveTime) > (60 * 5 * 1000)){
                response.put("expired", true);
                return response;
            }
            if(ResetCode.equals(request.get("code"))){
                session.removeAttribute("ResetCode");
                response.put("success", true);
                return response;
            }
            else{
                response.put("fail", true);
                return response;
            }
        } catch (Exception e) {
            log.info("오류 발생 : {}",  e.getMessage());
            return null;
        }
    }

    @PostMapping("/newPassword")
    public Map<String, Object> resetPassword(@RequestBody Map<String, String> request) {
        String m_id = request.get("m_id");
        String m_email = request.get("m_email");
        String newPassword = request.get("newPassword");
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        Map<String, Object> response = new HashMap<>();

        MemberVO member = memberService.findById(m_id);

        // 회원정보가 없거나, 활동회원이 아니거나, 이메일이 아이디에 연결된 이메일이 아닌경우 실패
        if (member == null || !"1".equals(member.getM_active()) || !m_email.equals(member.getM_email())) {
            response.put("success", false);
            response.put("message", "회원 정보가 존재하지 않거나 인증되지 않았습니다.");
            return response;
        }
        member.setM_pwd(encodedNewPassword);
        boolean updated = memberService.newPassword(member, encodedNewPassword);

        response.put("success", updated);
        if (!updated) {
            response.put("message", "비밀번호 변경 실패");
        }

        return response;
    }

    @PostMapping("/myPage")

    public DataVO<MemberVO> myPage(@AuthenticationPrincipal CustomUserDetails user){
        if (user == null) {
            return new DataVO(false, "인증 정보 없음(로그인 필요)", null);
        }

        Integer mIdx = user.getMIdx();
        MemberVO member = memberService.findByIdx(mIdx);

        if (member == null) {
            return DataVO.fail("회원 정보를 찾을 수 없습니다.");
        }

        return DataVO.success(member, "개인정보 조회 성공");
    }

    @PostMapping("/updateMyInfo")
    public DataVO<Void> updateMyInfo(@RequestBody MemberVO mvo){
        int result = memberService.updateMyInfo(mvo);
        try{
            if(result > 0){
                return DataVO.success(null, "개인정보 변경 성공");
            }else {
                return DataVO.fail("개인정보 변경 실패");
            }
        } catch (Exception e) {
            return DataVO.fail(e.getMessage());
        }
    }

    @GetMapping("/me")
    public DataVO<MemberVO> me(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        if (user == null) {
            return DataVO.fail("인증 정보 없음");
        }

        // JWT에서 인증된 사용자 ID
        String mId = user.getUsername();

        // DB에서 최신 회원 정보 조회
        MemberVO member = memberService.findById(mId);

        if (member == null) {
            return DataVO.fail("회원 정보 없음");
        }
        return DataVO.success(member, "개인정보 변경 성공");
    }

    @GetMapping("/inquiries")
    public ResponseEntity<DataVO> getMyInquiries(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        log.info("user : {}", user);
        MemberVO member = memberService.findByIdx(user.getMIdx());
        List<InqueryVO> list = inqueryService.getList();

        List<InqueryVO> myInquiries = list.stream()
                .filter(i -> i.getI_m_idx() != null)
                .filter(i -> Integer.parseInt(i.getI_m_idx()) == member.getM_idx())
                .toList();

        return ResponseEntity.ok(
                DataVO.success(myInquiries)
        );
    }

    @GetMapping("/guestbooks")
    public ResponseEntity<DataVO> getMyGuestbooks(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        MemberVO member = memberService.findByIdx(user.getMIdx());
        List<GuestbookVO> list = guestbookService.getList();

        List<GuestbookVO> myGuestbooks = list.stream()
                .filter(g -> Objects.equals(g.getG_writer(), member.getM_id()))
                .toList();


        list.forEach(g ->
                log.info("guestbook writer = [{}]", g.getG_writer())
        );
        return ResponseEntity.ok(
                DataVO.success(myGuestbooks)
        );
    }

    @PostMapping("/quitMail")
    public Map<String, Object> sendQuitMail(
            @AuthenticationPrincipal CustomUserDetails user,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();

        if (user == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        MemberVO member = memberService.findByIdx(user.getMIdx());

        if (member == null || !"1".equals(member.getM_active())) {
            response.put("success", false);
            response.put("message", "유효하지 않은 회원입니다.");
            return response;
        }

        // 🔥 회원 탈퇴 전용 메일 발송
        boolean success = memberService.sendQuitCode(member.getM_email(), session);

        response.put("success", success);
        if (!success) {
            response.put("message", "탈퇴 인증 메일 발송 실패");
        }

        return response;
    }


    @PostMapping("/quitVerify")
    public Map<String, Object> verifyQuitCode(
            @RequestBody Map<String, String> request,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
            String authCode = (String) session.getAttribute("authCode");
            Long saveTime = (Long) session.getAttribute("authTime");

            if (authCode == null || saveTime == null) {
                response.put("success", false);
                response.put("message", "인증 요청이 없습니다.");
                return response;
            }

            // 5분 유효
            if ((System.currentTimeMillis() - saveTime) > (60 * 5 * 1000)) {
                response.put("expired", true);
                return response;
            }

            if (authCode.equals(request.get("code"))) {
                session.removeAttribute("authCode");
                session.setAttribute("quitVerified", true); // 🔥 핵심
                response.put("success", true);
            } else {
                response.put("fail", true);
            }

            return response;
        } catch (Exception e) {
            log.error("탈퇴 인증 오류", e);
            response.put("success", false);
            return response;
        }
    }

    @PostMapping("/quit")
    public DataVO<Void> quitMember(
            @AuthenticationPrincipal CustomUserDetails user,
            HttpSession session,
            HttpServletResponse response
    ) {
        if (user == null) {
            return DataVO.fail("로그인이 필요합니다.");
        }

        Boolean verified = (Boolean) session.getAttribute("quitVerified");
        if (verified == null || !verified) {
            return DataVO.fail("이메일 인증이 필요합니다.");
        }

        MemberVO member = memberService.findByIdx(user.getMIdx());

        if (member == null || !"1".equals(member.getM_active())) {
            return DataVO.fail("이미 탈퇴했거나 존재하지 않는 회원입니다.");
        }

        // 🔥 탈퇴 처리 (soft delete)
        memberService.deactivateMember(member.getM_idx());

        // 🔥 토큰 정리
        jwtUtil.deleteRefreshTokenCookie(response);
        session.invalidate();

        return DataVO.success(null, "회원 탈퇴가 완료되었습니다.");
    }


}
