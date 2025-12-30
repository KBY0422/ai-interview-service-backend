package com.ict.finalproject.common.OpenAiGptModule.Other;


import lombok.Data;

import java.util.List;


// 프론트에서 넘어온 객체에서 사용자의 답변과 프롬프트 설정을 받아와 값을 저장시키는 클래스
@Data
public class OpenAiGptObject {
    private String msg;
    private List<String> prompts;
}
