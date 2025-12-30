package com.ict.finalproject.common.OpenAiGptModule.Properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.ai.openai.chat.options")
public class OpenAiGptOptions {

    private String model;
    private Float temperature;
}
