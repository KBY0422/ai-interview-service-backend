package com.ict.finalproject.OpenAIGPT.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItvQnaVO {
    private String q_idx;
    private String q_s_idx;
    private String q_content;
    private String q_type;
}
