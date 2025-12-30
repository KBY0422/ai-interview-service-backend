package com.ict.finalproject.OpenAIGPT.service;


import com.ict.finalproject.OpenAIGPT.mapper.OpenAiGptMapper;
import com.ict.finalproject.OpenAIGPT.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAiGptServiceImpl implements OpenAiGptService {

    private final OpenAiGptMapper openAiGptMapper;

    @Override
    public int tokenUsageUpdate(TokenUsageVO TuVO) {
        return openAiGptMapper.tokenUsageUpdate(TuVO);
    }

    @Override
    public List<EnvJobVO> getJobList() {
        return openAiGptMapper.getJobList();
    }

    @Override
    public List<EnvSkillVO> getJobSkillList(String e_job) {
        return openAiGptMapper.getJobSkillList(e_job);
    }

    @Override
    public int writeInterview(List<ItvQnaVO> requestList) {
        return  openAiGptMapper.writeInterview(requestList);
    }

    @Override
    public String getMemberIdx(String m_name) {
        return openAiGptMapper.getMemberIdx(m_name);
    }

    @Override
    public int writeSession(ItvSessionVO ItvVO) {
        return openAiGptMapper.writeSession(ItvVO);
    }

    @Override
    public String getLastSessionIdx(String m_idx) {
        return openAiGptMapper.getLastSessionIdx(m_idx);
    }

    @Override
    public int setAdminInterviewSessionJob(List<EnvJobVO> envJobList) {
        return openAiGptMapper.setAdminInterviewSessionJob(envJobList);
    }

    @Override
    public int setAdminInterviewSessionSkill(List<EnvSkillVO> envSkillList) {
        return openAiGptMapper.setAdminInterviewSessionSkill((envSkillList));
    }

    @Override
    public List<EnvSkillVO> getSkillList() {
        return openAiGptMapper.getSkillList();
    }

    @Override
    public int adminDeleteJob(String e_job_idx) {
        return openAiGptMapper.adminDeleteJob(e_job_idx);
    }

    @Override
    public int adminDeleteSkill(String e_skill_idx) {
        return openAiGptMapper.adminDeleteSkill(e_skill_idx);
    }

    @Override
    public int writeTokenUsageSession(TokenUsageVO TuVO) {
        return openAiGptMapper.writeTokenUsageSession(TuVO);
    }
}
