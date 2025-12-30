package com.ict.finalproject.dashboard.adminInterviewStat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRatioVO {
    /** 직종명 (백엔드 / 프론트엔드 / 풀스택 등) */
    private String job;

    /** 선택 수 */
    private int count;
}
