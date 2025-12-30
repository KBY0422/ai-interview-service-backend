package com.ict.finalproject.usercontrol.service;

import com.ict.finalproject.usercontrol.mapper.UserControlMapper;
import com.ict.finalproject.usercontrol.vo.UsercontrolVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserControlServiceImpl implements UserControlService {

    private final UserControlMapper userControlMapper;

    @Override
    public Map<String, Object> getMembers(int page, int size,String keyword) {

        int offset = (page - 1) * size;

        List<UsercontrolVO> list;
        int totalCount;

        if (keyword == null || keyword.trim().isEmpty()) {
            list = userControlMapper.selectMembersPaged(offset, size);
            totalCount = userControlMapper.selectMemberCount();
        } else {
            list = userControlMapper.searchMembersPaged(keyword, offset, size);
            totalCount = userControlMapper.searchMemberCount(keyword);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", totalCount);
        result.put("page", page);
        result.put("size", size);

        return result;
    }


    @Override
    public void updateMemberStatus(int mIdx, int mActive) {
        userControlMapper.updateMemberActive(mIdx, mActive);
    }
}
