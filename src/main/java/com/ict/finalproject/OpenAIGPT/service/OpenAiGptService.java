package com.ict.finalproject.OpenAIGPT.service;

import com.ict.finalproject.OpenAIGPT.vo.*;

import java.util.List;

public interface OpenAiGptService {

    int tokenUsageUpdate(TokenUsageVO TuVO);
    List<EnvJobVO> getJobList();
    List<EnvSkillVO> getJobSkillList(String e_job);
    int writeInterview(List<ItvQnaVO> requestList);
    String getMemberIdx(String m_name);
    int writeSession(ItvSessionVO ItvVO);
    String getLastSessionIdx(String m_idx);
    int setAdminInterviewSessionJob(List<EnvJobVO> envJobList);
    int setAdminInterviewSessionSkill(List<EnvSkillVO> envSkillList);
    List<EnvSkillVO> getSkillList();
    int adminDeleteJob(String e_job_idx);
    int adminDeleteSkill(String e_skill_idx);
    int writeTokenUsageSession(TokenUsageVO TuVO);
}
