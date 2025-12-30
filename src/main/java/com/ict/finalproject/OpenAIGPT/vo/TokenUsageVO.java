package com.ict.finalproject.OpenAIGPT.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenUsageVO {
    private String t_idx;
    private String t_m_idx;
    private String t_s_idx;
    private String t_prompt;
    private String t_completion;
    private String t_total;
    private String t_created_at;
    private String t_purpose;
}
