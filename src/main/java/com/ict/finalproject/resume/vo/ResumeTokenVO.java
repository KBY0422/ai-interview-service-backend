package com.ict.finalproject.resume.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeTokenVO {

    private Integer tIdx;          // PK (auto increment)
    private Integer mIdx;        // 회원 고유 번호
    private Integer rIdx;       // 🔥 추가
    private Integer tTotal;        // 총 사용 토큰 수 (핵심)

    private String tPurpose;       // 호출 목적 (resume_analyze, resume_create)

    private LocalDateTime tCreatedAt; // 호출 시각 (DB에서 자동)
}