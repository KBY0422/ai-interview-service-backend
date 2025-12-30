package com.ict.finalproject.member.service;

import com.ict.finalproject.member.vo.MemberVO;
import com.ict.finalproject.member.vo.RefreshVO;
import jakarta.servlet.http.HttpSession;

import java.util.Date;
import java.util.Map;

public interface MemberService {
    boolean idCheck(String m_id);
    boolean sendCode(String m_email, HttpSession session);
    int register(MemberVO mvo);
    MemberVO findById(String m_id);

    MemberVO findByIdx(Integer mIdx);   // ★ 추가
    MemberVO findByEmail(String m_email);
    void saveRefreshToken(String m_id, String refreshToken, Date expiration);
    RefreshVO getRefreshToken(String m_id);
    MemberVO findId(Map<String,String> map);

    boolean sendPasswordResetCode(String m_email, HttpSession session);
    boolean newPassword(MemberVO member,String encodedNewPassword);
    int updateMyInfo(MemberVO mvo);

    boolean sendQuitCode(String m_email, HttpSession session);
    void deactivateMember(int m_idx);
}
