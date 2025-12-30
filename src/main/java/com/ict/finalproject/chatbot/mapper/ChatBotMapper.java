package com.ict.finalproject.chatbot.mapper;

import com.ict.finalproject.chatbot.vo.ChatBotResponseVO;
import com.ict.finalproject.chatbot.vo.ChatBotVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatBotMapper {
    int insertTokenUsage(ChatBotVO vo);

    ChatBotResponseVO chat(String text, Integer mIdx);
}
