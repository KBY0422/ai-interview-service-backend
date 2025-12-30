package com.ict.finalproject.common.OpenAiGptModule.Other;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// Spring AI가 자동 생성해주는 OpenAiChatModel을 주입받아서 ChatClient Bean을 직접 생성
// 그래서 @Autowired 또는 @RequiredArgsConstructor로 어디서든 사용가능하게 함

@Configuration
public class OpenAiGptConfig {
    @Bean
    public ChatClient chatClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel).build();
    }
}
