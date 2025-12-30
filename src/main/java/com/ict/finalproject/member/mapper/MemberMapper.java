package com.ict.finalproject.member.mapper;

import com.ict.finalproject.member.vo.MemberVO;
import com.ict.finalproject.member.vo.RefreshVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface MemberMapper {
    int idCheck(@Param("m_id") String m_id);
    int register(MemberVO mvo);
    MemberVO findById(String m_id);

    MemberVO findByIdx(Integer mIdx);   // ★ 추가
    MemberVO findByEmail(String m_email);
    void saveRefreshToken(RefreshVO refreshVO);
    RefreshVO getRefreshToken(String m_id);
    MemberVO findId(Map<String,String> map);
    boolean newPassword(MemberVO member);
    int updateMyInfo(MemberVO mvo);
    void deactivateMember(int m_idx);
}
