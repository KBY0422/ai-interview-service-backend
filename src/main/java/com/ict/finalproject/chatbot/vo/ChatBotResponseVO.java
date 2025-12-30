package com.ict.finalproject.chatbot.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatBotResponseVO {

    private String reply;
    private Integer totalTokens;
}
