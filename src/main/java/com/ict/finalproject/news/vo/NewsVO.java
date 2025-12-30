package com.ict.finalproject.news.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsVO {
    private int kIdx;
    private String kContent;
    private int kCount;
}
