package com.ict.finalproject.analysis.list.controller;

import com.ict.finalproject.analysis.list.service.InterviewAnalysisListService;
import com.ict.finalproject.analysis.list.vo.InterviewAnalysisListVO;
import com.ict.finalproject.common.security.CustomUserDetails;
import com.ict.finalproject.common.vo.DataVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analysis/list")
@RequiredArgsConstructor
public class InterviewAnalysisListController {

    private final InterviewAnalysisListService interviewAnalysisListService;

    /**
     * 면접 분석 목록 조회
     * GET /analysis/list
     */
    @GetMapping
    public DataVO<List<InterviewAnalysisListVO>> getAnalysisList(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        System.out.println("user : " + user);

        if (user == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        Integer mIdx = user.getMIdx();
        if (mIdx == null) {
            throw new IllegalStateException("로그인 사용자 정보가 올바르지 않습니다.");
        }
        System.out.println("mIdx: " + mIdx);
        List<InterviewAnalysisListVO> list = interviewAnalysisListService.getAnalysisList(mIdx);
        System.out.println("list: " + list);
        return DataVO.success(list);
    }
}
