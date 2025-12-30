package com.ict.finalproject.analysis.result.controller;

import com.ict.finalproject.analysis.result.dto.InterviewResultResponseDTO;
import com.ict.finalproject.analysis.result.service.InterviewSessionAnalysisService;
import com.ict.finalproject.common.security.CustomUserDetails;
import com.ict.finalproject.common.vo.DataVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class InterviewSessionAnalysisController {

    private final InterviewSessionAnalysisService interviewSessionAnalysisService;

    /**
     * 면접 결과 분석 및 조회
     *
     * 사용 시나리오
     * - 최초 호출 시
     *   세션 기준 질문과 답변을 조회하고
     *   분석 결과를 생성하여 DB에 저장한 뒤 반환한다
     *
     * - 이미 분석된 세션일 경우
     *   DB에 저장된 분석 결과를 그대로 조회하여 반환한다
     *
     * @param sIdx 면접 세션 번호
     * @return InterviewResultResponseDTO
     */
    @GetMapping("/result/{sIdx}")
    public DataVO<InterviewResultResponseDTO> getInterviewResult(
            @PathVariable int sIdx,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        if (user == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        Integer mIdx = user.getMIdx();
        if (mIdx == null) {
            throw new IllegalStateException("로그인 사용자 정보가 올바르지 않습니다.");
        }

        InterviewResultResponseDTO result =
                interviewSessionAnalysisService.getInterviewResult(sIdx, mIdx);

        return DataVO.success(result);
    }
}
