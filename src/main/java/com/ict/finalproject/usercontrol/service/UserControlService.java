package com.ict.finalproject.usercontrol.service;

import java.util.Map;

public interface UserControlService {

    Map<String, Object> getMembers(int page, int size,String keyword);
    void updateMemberStatus(int mIdx, int mActive);
}
