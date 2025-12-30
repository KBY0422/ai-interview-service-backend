package com.ict.finalproject.common.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * - Spring Security의 principal로 들어가는 사용자 객체
 * - 게스트 요청이면 컨트롤러에서 user가 null로 들어온다.
 * - 로그인 요청이면 Authentication.principal에 이 객체가 들어온다.
 */
@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    /**
     * member 테이블의 PK (m_idx)
     * token_usage_log.t_m_idx에 들어갈 값
     */
    private final Integer mIdx;

    /**
     * 로그인 아이디(또는 이메일 등)
     */
    private final String username;

    /**
     * 암호화된 비밀번호
     */
    private final String password;

    /**
     * 권한 목록
     */
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}