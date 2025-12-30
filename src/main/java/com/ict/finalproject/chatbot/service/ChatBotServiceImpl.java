package com.ict.finalproject.chatbot.service;

import com.ict.finalproject.chatbot.mapper.ChatBotMapper;
import com.ict.finalproject.chatbot.vo.ChatBotResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {

    private final ChatBotMapper chatBotMapper;


    @Override
    public ChatBotResponseVO chat(String text, Integer mIdx) {
        return chatBotMapper.chat(text,mIdx);
    }
}
