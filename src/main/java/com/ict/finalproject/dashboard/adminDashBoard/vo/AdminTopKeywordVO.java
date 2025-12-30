package com.ict.finalproject.dashboard.adminDashBoard.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminTopKeywordVO {


    /** 검색 키워드 */
    private String keyword;

    /** 검색 횟수 */
    private int count;

}
