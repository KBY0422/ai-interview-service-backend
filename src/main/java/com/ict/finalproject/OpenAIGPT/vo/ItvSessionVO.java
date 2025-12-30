package com.ict.finalproject.OpenAIGPT.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItvSessionVO {
    private String s_idx;
    private String s_m_idx;
    private String s_title;
    private String s_job;
    private String s_type;
    private String s_skill;
    private String s_level;
    private String s_date;
    private String s_active;
}
