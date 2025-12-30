package com.ict.finalproject.news.service;

import com.ict.finalproject.news.vo.NewsVO;

import java.util.List;

public interface NewsService {
    List<NewsVO> getKeywordList();

    int increaseKeywordCount(NewsVO newsVO);

    int insertKeyword(String keyword);

    int deleteKeyword(String k_content);
}
