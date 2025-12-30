package com.ict.finalproject.notice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeVO {
    String n_idx;
    String n_title;
    String n_content;
    String n_writedate;
    String n_hit;
    String n_active;
    String n_delete;
    String n_pin;     // 상단공지
}