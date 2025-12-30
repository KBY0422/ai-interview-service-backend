package com.ict.finalproject.usercontrol.mapper;

import com.ict.finalproject.usercontrol.vo.UsercontrolVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserControlMapper {

    // 전체 회원 조회
    List<UsercontrolVO> searchMembersPaged(
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("size") int size
    );
    // 페이징 기법을 위해서.
    int selectMemberCount();

    // 전체 조회 + 페이징
    List<UsercontrolVO> selectMembersPaged(
            @Param("offset") int offset,
            @Param("size") int size
    );
    //검색했을때도 페이징 기법 유지.
    int searchMemberCount(@Param("keyword") String keyword);
    // 검색


    // 회원 정지 / 활성
    int updateMemberActive(
            @Param("mIdx") int mIdx,
            @Param("mActive") int mActive
    );


}
