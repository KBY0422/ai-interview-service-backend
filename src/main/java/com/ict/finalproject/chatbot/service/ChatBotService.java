package com.ict.finalproject.chatbot.service;

import com.ict.finalproject.chatbot.vo.ChatBotResponseVO;

public interface ChatBotService {
    public ChatBotResponseVO chat(String text, Integer mIdx);
}
