package com.example.tuanhaowu.Service;

import com.alibaba.fastjson.JSON;
import com.example.tuanhaowu.Entity.User;
import com.example.tuanhaowu.util.MsgUtil.Msg;
import net.sf.json.JSONObject;
import org.springframework.security.core.Authentication;

public interface UserService {
    User checkUser(String account, String RawPassword) throws Exception;
    String getLeaderName(String userid);
    User getUserInfo(String userid);

    Msg checkRepeated(String name, String account);
    User saveOneUser(String name,String account, String password, String picture,String tel, String address );
    User getLeaderByGroupId(int groupId);
    String getAuthUserId(Authentication authentication);
}
