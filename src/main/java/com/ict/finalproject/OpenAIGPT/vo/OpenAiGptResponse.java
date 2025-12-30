package com.ict.finalproject.OpenAIGPT.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OpenAiGptResponse {
    private String answer;
    private Long promptTokens;
    private Long completionTokens;
    private Long totalTokens;
}
