package com.ict.finalproject.inquery.service;
import com.ict.finalproject.common.paging.Paging;
import com.ict.finalproject.common.security.CustomUserDetails;
import com.ict.finalproject.inquery.mapper.InqueryMapper;
import com.ict.finalproject.inquery.vo.InqueryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class InqueryServiceImpl implements InqueryService{

    private final InqueryMapper inqueryMapper;

    @Override
    public List<InqueryVO> getList() {
        return inqueryMapper.getList();
    }

    @Override
    public Map<String, Object> getPageList(int currentPage, int i_m_idx) {
        try {
            // 리스트 구하기전에 미리 전체 레코드 구한다.
            int total = inqueryMapper.getTotalRecord();
            // 페이징 기법
            Paging paging = new Paging(currentPage, total, 5, 3);
            Map<String, Object> map = new HashMap<>();
            map.put("start", paging.getStartIndex());
            map.put("limit", paging.getNumPerPage());
            map.put("i_m_idx", i_m_idx);

            List<InqueryVO> inquerylist = inqueryMapper.getPageList(map);

            Map<String, Object> result = new HashMap<>();
            result.put("paging", paging);
            result.put("inquerylist", inquerylist );

            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public InqueryVO getDetail(String i_idx) {
        return inqueryMapper.getDetail(i_idx);
    }

    @Override
    public int getInsert(InqueryVO ivo) {
        Authentication authentication  =
                SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        if (user == null) {
            // 로그인 햇기 때문에 user은 있어야 힌다.
        }
        int i_m_idx = user.getMIdx(); // ★ 여기서 m_idx 확보
        //log.info("i_m_idx {}", i_m_idx);

        ivo.setI_writedate(LocalDateTime.now().toString());
        ivo.setI_m_idx(Integer.toString(i_m_idx));

        return inqueryMapper.getInsert(ivo);
    }

    @Override
    public int getDelete(String i_idx) {
        log.info("getUpdate i_idx {}", i_idx);
        return inqueryMapper.getDelete(i_idx);
    }

    @Override
    public int getUpdate(InqueryVO ivo) {
        return inqueryMapper.getUpdate(ivo);
    }

    @Override
    public int getUpdateResponse(InqueryVO ivo) {
        return inqueryMapper.getUpdateResponse(ivo);
    }
}
