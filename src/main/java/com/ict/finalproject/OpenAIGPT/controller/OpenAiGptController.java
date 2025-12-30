package com.ict.finalproject.OpenAIGPT.controller;


import com.ict.finalproject.OpenAIGPT.service.OpenAiGptService;
import com.ict.finalproject.OpenAIGPT.vo.*;
import com.ict.finalproject.common.OpenAiGptModule.Other.OpenAiGptObject;
import com.ict.finalproject.common.OpenAiGptModule.OpenAiGptSettingService;
import com.ict.finalproject.common.security.CustomUserDetails;
import com.ict.finalproject.common.vo.DataVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


// Spring AI 사용해서 구현
/*Spring AI는 Java/Spring 환경에서도 AI를 바로 연결하고 싶은데,
Python 환경을 따로 띄우거나 API를 직접 다루는 일을 하지 않기 위해 생긴 프레임 워크*/


@Slf4j
@RestController
@RequestMapping("/gpt")
@RequiredArgsConstructor
public class OpenAiGptController {

    private final OpenAiGptSettingService openAiGptSettingService;
    private final OpenAiGptService  openAiGptService;

    @PostMapping("/ask")
    public String ask(@RequestBody OpenAiGptObject request, @AuthenticationPrincipal CustomUserDetails user){

        OpenAiGptResponse value = openAiGptSettingService.talk(request.getMsg(), request.getPrompts());
        String s_m_idx = user.getMIdx().toString();
        log.info("s_m_id값 {}",  s_m_idx);
        String t_s_idx = openAiGptService.getLastSessionIdx(s_m_idx);

        TokenUsageVO TuVO = new TokenUsageVO();
        TuVO.setT_prompt(value.getPromptTokens().toString());
        TuVO.setT_completion(value.getCompletionTokens().toString());
        TuVO.setT_total(value.getTotalTokens().toString());
        TuVO.setT_m_idx(s_m_idx);
        TuVO.setT_s_idx(t_s_idx);

        int result = openAiGptService.tokenUsageUpdate(TuVO);

        return value.getAnswer();
    }

    @PostMapping("/interviewJobList")
    public DataVO<List<EnvJobVO>> getJobList(){

        try {
            List<EnvJobVO> envJobList = openAiGptService.getJobList();

            if(envJobList == null){
                return DataVO.fail("Job list 가져오기 실패");
            }
            else
            {
                return DataVO.success(envJobList, "Job list 가져오기 성공");
            }

        } catch (Exception e) {
            return DataVO.fail("DB접속 에러1: " + e.getMessage());
        }
    }

    @PostMapping("/interviewJobSkill")
    public DataVO<List<EnvSkillVO>> getJobSkillList(@RequestBody EnvJobVO EnvVO){

        try {
            List<EnvSkillVO> envJobSkillList = openAiGptService.getJobSkillList(EnvVO.getE_job());

            if(envJobSkillList == null){
                return DataVO.fail("Skill list 가져오기 실패");
            }
            else
            {
                return DataVO.success(envJobSkillList, "Skill list 가져오기 성공");
            }
        } catch (Exception e) {
            return DataVO.fail("DB접속 에러2: " + e.getMessage());
        }
    }

    @PostMapping("/writeSession")
    public DataVO<Void> writeSession(@RequestBody ItvSessionVO ItvVO, @AuthenticationPrincipal CustomUserDetails user){

        //================================ 면접세션DB 생성 부분 ==============================
        StringBuilder s_title = new StringBuilder();

        if(ItvVO.getS_type().contains("인성"))
        {
            s_title.append(ItvVO.getS_type().replace(" ", ""))
                    .append("_난이도")
                    .append(ItvVO.getS_level());

            ItvVO.setS_job("인성면접");
        }
        else {
            s_title.append(ItvVO.getS_job())
                    .append("_")
                    .append(ItvVO.getS_type().replace(" ", ""))
                    .append("_난이도")
                    .append(ItvVO.getS_level());
        }

        try {
            // 사용자 번호 및 세션 저장 (중첩 try-catch 기능 유지)
            String s_m_idx = user.getMIdx().toString();
            ItvVO.setS_m_idx(s_m_idx);
            ItvVO.setS_title(s_title.toString());
            openAiGptService.writeSession(ItvVO);

            // 토큰 통계 저장 로직
            String t_s_idx = openAiGptService.getLastSessionIdx(s_m_idx);
            TokenUsageVO TuVO = new TokenUsageVO();
            TuVO.setT_m_idx(s_m_idx);
            TuVO.setT_s_idx(t_s_idx);
            TuVO.setT_prompt("0");
            TuVO.setT_completion("0");
            TuVO.setT_total("0");
            TuVO.setT_purpose("SESSION");

            int result = openAiGptService.writeTokenUsageSession(TuVO);

            if (result > 0) {
                return DataVO.success(null, "면접정보 & 토큰통계 저장 성공");
            } else {
                return DataVO.fail("면접정보 & 토큰통계 쓰기 실패");
            }
        } catch (Exception e) {
            return DataVO.fail("면접세션 저장 중 오류 발생: " + e.getMessage());
        }
    }


    @PostMapping("/writeInterview")
    public DataVO<String> writeInterview(@RequestBody  List<ItvQnaVO> requestList, @AuthenticationPrincipal CustomUserDetails user) {

        try {
            String m_idx =  user.getMIdx().toString();
            String q_s_idx = openAiGptService.getLastSessionIdx(m_idx);

            requestList.forEach(item -> item.setQ_s_idx(q_s_idx));

            int result = openAiGptService.writeInterview(requestList);

            if (result > 0) {
                // 기존에 data에 q_s_idx를 담아주던 기능을 success(data, message)로 구현
                return DataVO.success(q_s_idx, "면접기록 쓰기 성공");
            } else {
                return DataVO.fail("면접기록 쓰기 실패");
            }
        } catch (Exception e) {
            return DataVO.fail("DB접속 에러3: " + e.getMessage());
        }
    }


    @PostMapping("/adminWriteJobList")
    public DataVO<Integer> setAdminInterviewSessionJob(@RequestBody List<EnvJobVO> envJobList){

        try {
            int result = openAiGptService.setAdminInterviewSessionJob(envJobList);
            if (result > 0) {
                return DataVO.success(result, "직업 등록 저장 성공");
            } else {
                return DataVO.fail("직업 등록 저장 실패");
            }
        } catch (Exception e) {
            return DataVO.fail("DB 연결 실패: " + e.getMessage());
        }
    }




    @PostMapping("/adminWriteSkillList")
    public DataVO<Integer> setAdminInterviewSessionSkill(@RequestBody List<EnvSkillVO> envSkillList){

        try {
            int result = openAiGptService.setAdminInterviewSessionSkill(envSkillList);

            if (result > 0) {
                return DataVO.success(result, "기술스택 등록 저장 성공");
            }
            else
            {
                return DataVO.fail("직업 등록 저장 실패");
            }
        } catch (Exception e) {
            return DataVO.fail("DB 연결 실패: " + e.getMessage());
        }
    }


    @PostMapping("/adminSkillList")
    public DataVO<List<EnvSkillVO>> getSkillList(){

        List<EnvSkillVO> envSkillList= openAiGptService.getSkillList();

        try {
            if(envSkillList == null){
                return DataVO.fail("skill list 가져오기 실패");
            }
            else
            {
                return DataVO.success(envSkillList, "skill list 가져오기 성공");
            }
        } catch (Exception e) {
            return DataVO.fail("DB접속 에러4: " + e.getMessage());
        }
    }


    @PostMapping("/adminDeleteJob")
    public DataVO<Integer> adminDeleteJob(@RequestBody Map<String, String> params){

        try {
            int result = openAiGptService.adminDeleteJob(params.get("e_job_idx"));

            if (result > 0) {
                return DataVO.success(result, "직군 삭제 성공");
            }
            else
            {
                return DataVO.fail("직군 삭제 실패");
            }
        } catch (Exception e) {
            return DataVO.fail("DB접속 에러5: " + e.getMessage());
        }
    }



    @PostMapping("/adminDeleteSkill")
    public DataVO<Integer> adminDeleteSkill(@RequestBody Map<String, String> params){

        try {
            int result = openAiGptService.adminDeleteSkill(params.get("e_skill_idx"));

            if (result > 0) {
                return DataVO.success(result, "기술 스택 삭제 성공");
            }
            else
            {
                return DataVO.fail("직군 삭제 실패");
            }
        } catch (Exception e) {
            return DataVO.fail("DB접속 에러6: " + e.getMessage());
        }
    }
}
