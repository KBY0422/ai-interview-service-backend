package com.ict.finalproject.common.OpenAiGptModule.Properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


// Prompt 설정값을 불러오기 위한 클래스

// 보안상 @Data보다 따로 쓰는게 안전
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.ai.custom") // application.yaml의 spring.ai.custom의 하위 내용을 모두 읽어옴
public class OpenAiGptPrompt {

    private String systemPrompt;
}
