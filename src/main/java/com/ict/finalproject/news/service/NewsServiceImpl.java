package com.ict.finalproject.news.service;

import com.ict.finalproject.news.mapper.NewsMapper;
import com.ict.finalproject.news.vo.NewsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsMapper newsMapper;


    @Override
    public List<NewsVO> getKeywordList() {
        return newsMapper.getKeywordList();
    }

    @Override
    public int increaseKeywordCount(NewsVO newsVO) {
        return newsMapper.increaseKeywordCount(newsVO);
    }

    @Override
    public int insertKeyword(String keyword) {
        return newsMapper.insertKeyword(keyword);
    }

    @Override
    public int deleteKeyword(String k_content) {
        return newsMapper.deleteKeyword(k_content);
    }

//    private final NewsMapper newsMapper;
}
