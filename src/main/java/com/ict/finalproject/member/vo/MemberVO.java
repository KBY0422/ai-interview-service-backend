package com.ict.finalproject.member.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {
    private Integer m_idx;

    private String m_name;
    private String m_id;
    private String m_pwd;
    private String m_email;
    private String m_addr1;
    private String m_addr2;
    private String m_active;
    private String m_admin;
    private String m_regdate;

}
