package com.ict.finalproject.inquery.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InqueryVO {
    String i_idx;
    String i_m_idx;
    String i_category;
    String i_title;
    String i_content;
    String i_writer;
    String i_writedate;
    String i_pwd;
    String i_active;
    String i_reply;
    String i_check;
    String i_response;
}
