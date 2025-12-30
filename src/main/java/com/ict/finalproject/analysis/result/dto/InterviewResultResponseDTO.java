package com.ict.finalproject.analysis.result.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InterviewResultResponseDTO {

    @JsonProperty("sIdx")
    private int sIdx;

    @JsonIgnore   // getter를 통한 중복 직렬화 차단
    public int getSIdx() {
        return sIdx;
    }

    private Scores scores;
    private Metrics metrics;

    private String summary;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> feedback;

    private LocalDateTime createdAt;

    @Data
    public static class Scores {
        private int total;
        private int tech;
        private int logic;
        private int soft;
    }

    @Data
    public static class Metrics {
        private double levelAvg;
        private double wrongAnswerRate;
    }
}
