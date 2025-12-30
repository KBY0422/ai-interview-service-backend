package com.ict.finalproject.common.OpenAiGptModule.Properties;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


// yaml파일의 properties들을 활성화 하기 위해 만든 클래스
// 아무내용 안써도 동작
@Configuration
@EnableConfigurationProperties({OpenAiGptPrompt.class, OpenAiGptOptions.class})
public class OpenAiGptPropertiesConfig {
}
