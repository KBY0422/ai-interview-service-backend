package com.ict.finalproject.news.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ict.finalproject.news.service.NewsService;
import com.ict.finalproject.news.vo.NewsVO;
import com.ict.finalproject.resume.vo.ResumeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

    @RestController
    @RequestMapping("/news")
    @RequiredArgsConstructor
    public class NewsController {

        private final NewsService newsService;

    @Value("${api.naver.client-id}")
    private String clientId;

    @Value("${api.naver.client-secret}")
    private String clientSecret;

        @GetMapping("/keywords")
        public List<NewsVO> getKeywordList() {
            return newsService.getKeywordList();
        }

        private String stripHtml(String text) {
            if (text == null) return "";
            return text.replaceAll("<[^>]*>", "");
        }

        private boolean matchTitleAndDescription(Map<String, Object> item, String kw) {
            String title = stripHtml((String) item.get("title")).toLowerCase();
            String description = stripHtml((String) item.get("description")).toLowerCase();

            return title.contains(kw) && description.contains(kw);
        }

        private boolean matchTitleOnly(Map<String, Object> item, String kw) {
            String title = stripHtml((String) item.get("title")).toLowerCase();
            return title.contains(kw);
        }



        private List<Map<String, Object>> filterItemsByKeyword(
                List<Map<String, Object>> items,
                String keyword
        ) {
            if (items == null || items.isEmpty()) {
                return List.of();
            }

            if (keyword == null || keyword.isBlank()) {
                return items;
            }

            String kw = keyword.toLowerCase();

            // title OR description
            List<Map<String, Object>> result = items.stream()
                    .filter(item -> {
                        String title = stripHtml((String) item.get("title")).toLowerCase();
                        String desc = stripHtml((String) item.get("description")).toLowerCase();
                        return title.contains(kw) && desc.contains(kw);
                    })
                    .toList();

            // 🔥 결과가 없으면 원본 그대로 반환
            return result.isEmpty() ? items : result;
        }




        // 관리자 키워드 추가
        @PostMapping("/admin/keyword")
        public void insertKeyword(@RequestParam("keyword") String keyword) {
            newsService.insertKeyword(keyword);
        }

        // 관리자 키워드 삭제
        @PostMapping("/admin/delete_keyword")
        public void deleteKeyword(@RequestParam("k_content") String k_content) {
            newsService.deleteKeyword(k_content);
        }





    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam("recommendedKeywords") String keyword, @RequestParam("display") int display,@RequestParam(defaultValue = "1") int start) {

        try {
            String text = URLEncoder.encode(keyword, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/news.json?query="
                    +text
                    + "&display=" + display+"&start=" + start;

            HttpURLConnection con = (HttpURLConnection) new URL(apiURL).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> result = mapper.readValue(response.toString(), Map.class);

            // ==============================
            // 🔥 여기부터 핵심 로직
            // ==============================
            List<Map<String, Object>> items =
                    (List<Map<String, Object>>) result.getOrDefault("items", List.of());

            List<Map<String, Object>> filteredItems =
                    filterItemsByKeyword(items, keyword);

            result.put("items", filteredItems);
            result.put("filteredCount", filteredItems.size());
            System.out.println("NAVER ITEMS SIZE = " + items.size());
            System.out.println("FILTERED ITEMS SIZE = " + filteredItems.size());



            return result;


        } catch (Exception e) {
            e.printStackTrace();
            return Map.of(
                    "error", true,
                    "message", e.getMessage()
            );
        }
    }

        @PostMapping("/keyword/click")
        public void increaseKeyword(@RequestParam String keyword) {
            NewsVO newsVO = new NewsVO();
            newsVO.setKContent(keyword);
            newsService.increaseKeywordCount(newsVO);
        }



    }


