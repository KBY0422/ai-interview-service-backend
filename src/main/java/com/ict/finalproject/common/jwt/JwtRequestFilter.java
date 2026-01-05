package com.ict.finalproject.common.jwt;

import com.ict.finalproject.member.service.MemberService;
import com.ict.finalproject.member.vo.MemberVO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final MemberService memberService;


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // 1️⃣ Preflight 요청
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        // 2️⃣ 인증 없이 허용할 API
        return path.startsWith("/member/login")
                || path.startsWith("/member/register")
                || path.startsWith("/member/idCheck")
                || path.startsWith("/member/sendCode")
                || path.startsWith("/member/verifyCode")
                || path.startsWith("/member/findId")
                || path.startsWith("/member/sendPasswordResetCode")
                || path.startsWith("/member/verifyPasswordResetCode")
                || path.startsWith("/member/newPassword")
                || path.startsWith("/member/refresh")
                || path.startsWith("/guestbook/")
                || path.startsWith("/notice/")
                || path.equals("/error");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // OPTIONS 요청은 통과
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        // 토큰 없으면 비회원 → 그냥 통과
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            // 만료 체크
            if (jwtUtil.isTokenExpired(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String userId = jwtUtil.validateAndExtractuserId(token);

            // 인증 정보 없을 때만 세팅
            if (userId != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                MemberVO member = memberService.findById(userId);
                if (member == null || !"1".equals(member.getM_active())) {
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(userId);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.warn("JWT 처리 중 오류", e);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // 오류 나도 비회원으로 통과
        }

    }
}
