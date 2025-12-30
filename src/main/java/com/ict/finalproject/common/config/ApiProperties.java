package com.ict.finalproject.common.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "api")
public class ApiProperties {

    private final Gpt gpt = new Gpt();
    private final Naver naver = new Naver();
    private final Kakao kakao = new Kakao();
    private final Mail mail = new Mail();

    @Getter
    public static class Gpt {
        private String key;
        private String model;

        public void setKey(String key) { this.key = key; }
        public void setModel(String model) { this.model = model; }
    }

    @Getter
    public static class Naver {
        private String clientId;
        private String clientSecret;

        public void setClientId(String clientId) { this.clientId = clientId; }
        public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }
    }

    @Getter
    public static class Kakao {
        private String clientId;
        private String redirectUri;

        public void setClientId(String clientId) { this.clientId = clientId; }
        public void setRedirectUri(String redirectUri) { this.redirectUri = redirectUri; }
    }

    @Getter
    public static class Mail {
        private String username;
        private String password;

        public void setUsername(String username) { this.username = username; }
        public void setPassword(String password) { this.password = password; }
    }
}