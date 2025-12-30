package com.ict.finalproject.usercontrol.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsercontrolVO {

    private int mIdx;
    private String mId;
    private String mName;
    private String mEmail;
    private int mActive;        // 1: 활성, 0: 정지
    private int mAdmin;         // 1: 관리자
    private LocalDateTime mRegdate;
    private LocalDateTime mDRegdate;
}
