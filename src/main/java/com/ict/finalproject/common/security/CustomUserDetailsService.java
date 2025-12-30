package com.ict.finalproject.common.security;

import com.ict.finalproject.member.service.MemberService;
import com.ict.finalproject.member.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String m_id) {
        MemberVO member = memberService.findById(m_id);
        if (member == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // ✅ String 기반 m_admin 처리
        String role = "1".equals(member.getM_admin()) ? "ADMIN" : "USER";

        GrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + role);

        return new CustomUserDetails(
                member.getM_idx(), // ⚠️ 여기 중요
                member.getM_id(),
                member.getM_pwd(),
                List.of(authority)
        );
    }
}
