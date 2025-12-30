package com.ict.finalproject.guestbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestbookVO {
    String g_idx;
    String g_writer;
    String g_title;
    String g_content;
    String g_writedate;
    String g_active;
}
