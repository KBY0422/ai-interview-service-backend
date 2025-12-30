package com.ict.finalproject.chatbot.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatBotVO {
    private Integer tIdx;          // PK (자동증가)
    private Integer tMIdx;          // member.m_idx
    private Integer tTotal;         // total_tokens
    private LocalDateTime tCreatedAt; // 생성시간
    private String tPurpose;        // "chatbot"
}
