package com.ict.finalproject.analysis.pdf.service;

import com.ict.finalproject.analysis.pdf.vo.InterviewPdfVO;
import org.springframework.http.ResponseEntity;

public interface InterviewPdfService {

    /**
     * PDF 생성
     * - 호출 시점에 항상 "새 PDF"를 생성하고, 기존 active는 비활성화 처리한다.
     */
    void createPdf(int sIdx);

    /**
     * PDF 다운로드 응답
     * - "다운로드"는 생성하지 않는다.
     * - active PDF가 없으면 예외.
     */
    ResponseEntity<byte[]> downloadPdfResponse(int sIdx);

    /**
     * 현재 세션 기준 활성 PDF 메타 정보 조회
     * - 없으면 null
     */
    InterviewPdfVO getPdfInfo(int sIdx);
}
