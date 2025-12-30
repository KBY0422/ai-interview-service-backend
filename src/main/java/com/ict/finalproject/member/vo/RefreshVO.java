package com.ict.finalproject.member.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshVO {
    private String m_id;
    private String refreshToken;
    private Date expiration;
}
