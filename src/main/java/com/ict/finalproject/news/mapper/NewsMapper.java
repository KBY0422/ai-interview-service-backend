package com.ict.finalproject.news.mapper;

import com.ict.finalproject.news.vo.NewsVO;
import com.ict.finalproject.resume.vo.ResumeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NewsMapper {
    List<NewsVO> getKeywordList();

    int increaseKeywordCount(NewsVO newsVO);

    int insertKeyword(String keyword);

    int deleteKeyword(String k_content);
}
