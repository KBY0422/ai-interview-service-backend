package com.ict.finalproject.chatbot.controller;

import com.ict.finalproject.chatbot.mapper.ChatBotMapper;
import com.ict.finalproject.chatbot.vo.ChatBotResponseVO;
import com.ict.finalproject.chatbot.vo.ChatBotVO;
import com.ict.finalproject.common.security.CustomUserDetails;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
public class ChatBotController {

    @Value("${api.gpt.key}")
    private String apiKey;

    private final ChatBotMapper chatBotMapper;

    /**
     * 챗봇 대화 API
     */
    @PostMapping
    public ResponseEntity<ChatBotResponseVO> chat(
            @RequestParam String text,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        /* ===============================
           1. 사용자 식별 (게스트 허용)
        =============================== */
        Integer mIdx = (user != null) ? user.getMIdx() : null;

        /* ===============================
           2. 프롬프트 구성
        =============================== */
        String prompt = """
                당신은 AI 면접 도우미입니다.
                사용자의 질문에 대해 친절하고 명확하게 답변하세요.
                면접, 이력서, 커리어와 관련된 질문에는 구체적인 조언을 제공하세요.
                
                사용자 질문:
                """ + text;

        /* ===============================
           3. OpenAI Client 생성
        =============================== */
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();

        /* ===============================
           4. GPT 호출
        =============================== */
        ResponseCreateParams params = ResponseCreateParams.builder()
                .model("gpt-4.1-mini")
                .input(prompt)
                .build();

        Response response = client.responses().create(params);

        /* ===============================
           5. 응답 파싱
        =============================== */
        String reply = response.output().stream()
                .filter(o -> o.message().isPresent())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("GPT 응답 message 없음"))
                .message().get()
                .content().get(0)
                .outputText().orElseThrow()
                .text();

        Integer totalTokens = Math.toIntExact(response.usage().get().totalTokens());

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+totalTokens);

        /* ===============================
           6. 토큰 로그 DB 저장
        =============================== */
        ChatBotVO log = new ChatBotVO();
        log.setTMIdx(1);          // 로그인 사용자면 값, 아니면 null
        log.setTTotal(totalTokens);
        log.setTPurpose("CHATBOT");

        chatBotMapper.insertTokenUsage(log);

        /* ===============================
           7. 응답 반환
        =============================== */
        return ResponseEntity.ok(
                new ChatBotResponseVO(reply, totalTokens)
        );
    }
}
