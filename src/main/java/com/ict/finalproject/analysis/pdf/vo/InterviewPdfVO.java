package com.ict.finalproject.analysis.pdf.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * interview_file 테이블과 1:1 매핑
 */
@Data
public class InterviewPdfVO {

    private int fIdx;
    private int fSIdx;
    private String fTitle;
    private String fPath;
    private LocalDateTime fRegdate;
    private LocalDateTime fExpireAt;
    private int fActive;
    private int fDownloadCount;
}
