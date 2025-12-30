package com.ict.finalproject.usercontrol.controller;

import com.ict.finalproject.usercontrol.service.UserControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserControlController {

    private final UserControlService userControlService;

    // 전체 조회
    @GetMapping("/usercontrol")
    public Map<String, Object> getMembers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        return userControlService.getMembers(page, size,keyword);
    }

    // 상태 변경 (정지/해제)
    @PatchMapping("/{mIdx}/status")
    public void updateStatus(@PathVariable int mIdx, @RequestParam int active) {
        userControlService.updateMemberStatus(mIdx, active);
    }
}
