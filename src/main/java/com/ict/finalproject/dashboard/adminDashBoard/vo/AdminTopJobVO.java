package com.ict.finalproject.dashboard.adminDashBoard.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminTopJobVO {

    /** 직무명 */
    private String job;

    /** 선택된 횟수 */
    private int count;

}
