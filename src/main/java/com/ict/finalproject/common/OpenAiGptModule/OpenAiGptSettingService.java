package com.ict.finalproject.common.OpenAiGptModule;


import com.ict.finalproject.OpenAIGPT.vo.OpenAiGptResponse;
import com.ict.finalproject.common.OpenAiGptModule.Properties.OpenAiGptOptions;
import com.ict.finalproject.common.OpenAiGptModule.Properties.OpenAiGptPrompt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiGptSettingService {

    private final ChatClient chatClient;
    private final OpenAiGptPrompt Prompt;
    private final OpenAiGptOptions options;



    public OpenAiGptResponse talk(String msg, List<String> prompts)
    {
        StringBuilder sys = new StringBuilder();    // StringBuilder = String 문자열 연결시 용이
        sys.append(Prompt.getSystemPrompt());       // yaml 파일에 적은 프롬프트 가져와서 추가

        prompts.stream()
                .filter(Objects::nonNull)                          // 배열중에 null값 있는지 filer
                .filter(p -> !p.isBlank())                  // "" 빈값이 있는지 filer
                .forEach(p -> sys.append("\n").append(p));  // 배열의 개수(동적프롬프트의 수) 만큼 sys에 추가

        Message systemMsg = new SystemMessage(sys.toString());

        /*return chatClient
                .prompt()
                .options(OpenAiChatOptions.builder()
                        .withModel(options.getModel())              // yaml 파일에서 가져온 gpt 모델
                        .withTemperature(options.getTemperature())  // yaml 파일에서 가져온 Temperature
                        .build()
                ).messages(systemMsg)
                .user(msg)  // 사용자 대답
                .call()     // gpt에게 질문
                .content(); // gpt 답변*/


        ChatResponse response = chatClient
                .prompt()
                .options(OpenAiChatOptions.builder()
                        .withModel(options.getModel())              // yaml 파일에서 가져온 gpt 모델
                        .withTemperature(options.getTemperature())  // yaml 파일에서 가져온 Temperature
                        .build()                                    // 옵션 빌드
                ).messages(systemMsg)                               // 질문리스트 포함
                .user(msg)                                          // 사용자 답변 포함
                .call()                                             // gpt에게 모든 값 전달
                .chatResponse();                                    // gpt 응답 값 & 토큰값 받기위해 ChatResponse형으로 리턴

        Usage usage = response.getMetadata().getUsage();            // ChatResponse 객체 안에 있는 토큰사용량 받아오기


        return OpenAiGptResponse.builder()                              // 토큰값과 gpt 값을 같이 리턴하기위해 만든 임의의 OpenAiGptResponse 라는 @Builder 클래스
                .answer(response.getResult().getOutput().getContent())  // gpt 답변 포함
                .promptTokens(usage.getPromptTokens())                  // 답변토큰값 포함
                .completionTokens(usage.getGenerationTokens())          // 질문 토큰값 포함
                .totalTokens(usage.getTotalTokens())                    // 토탈 토큰값 포함
                .build();                                               // 값을 합쳐서 build 하고 호출한 곳으로 리턴
    }
}
