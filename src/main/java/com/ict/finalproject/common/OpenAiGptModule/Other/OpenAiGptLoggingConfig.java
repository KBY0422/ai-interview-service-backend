package com.ict.finalproject.common.OpenAiGptModule.Other;


import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiGptLoggingConfig {

    // gpt builder 내용 로그 확인 클래스 (개발단계에서만 사용)
//    @Bean
    public RestClientCustomizer logRequests()
    {
        return builder -> builder.requestInterceptor((request, body, execution) -> {

            System.out.println("===== OPENAI REQUEST =====");
            System.out.println("URL: " + request.getURI());
            System.out.println("Method: " + request.getMethod());
            System.out.println("Headers: " + request.getHeaders());
            System.out.println("Body: " + new String(body));
            System.out.println("==========================");

            return execution.execute(request, body);
        });
    }
}
