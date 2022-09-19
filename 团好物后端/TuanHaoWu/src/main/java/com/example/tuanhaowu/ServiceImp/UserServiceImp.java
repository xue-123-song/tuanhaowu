package com.example.tuanhaowu.ServiceImp;

import com.alibaba.fastjson.JSON;
import com.example.tuanhaowu.Dao.GroupDao;
import com.example.tuanhaowu.Dao.UserDao;
import com.example.tuanhaowu.Entity.Group;
import com.example.tuanhaowu.Entity.User;
import com.example.tuanhaowu.Service.UserService;
import com.example.tuanhaowu.util.MsgUtil.Msg;
import com.example.tuanhaowu.util.MsgUtil.MsgUtil;
import com.example.tuanhaowu.util.SnowFlakeUtil;
import net.sf.json.JSONObject;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    GroupDao groupDao;
    @Autowired
    private StringEncryptor stringEncryptor;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public User checkUser(String account, String RawPassword) throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = userDao.checkUser(account);
            if(passwordEncoder.matches(RawPassword,user.getPassword())) {
                user.setTel(stringEncryptor.decrypt(user.getTel()));
                user.setAddress(stringEncryptor.decrypt(user.getAddress()));
                return user;
            }
            else
                return null;
    }

    @Override
    public String getLeaderName(String userid) {
        return userDao.getLeaderName(userid);
    }

    @Override
    public User getUserInfo(String userid) {
        User user = userDao.getUserByUserId(userid);
        user.setPassword("");
        return user;
    }

    @Override
    public Msg checkRepeated(String name, String account) {
        int code =  userDao.checkRepeated(name,account);
        System.out.println(code);
        switch (code){
            case -1:return MsgUtil.makeMsg(code,"name不能为空");
            case -2:return MsgUtil.makeMsg(code,"account不能为空");
            case 0:return MsgUtil.makeMsg(code,"注册成功");
            case 1:return MsgUtil.makeMsg(code,"该account已经被占用！");
            case 2:return MsgUtil.makeMsg(code,"该name已经被占用");
            case 3:return MsgUtil.makeMsg(code,"该account和name都已经被占用！");
            default:
                throw new IllegalStateException("Unexpected value: " + code);
        }
    }

    @Override
    public User saveOneUser(String name, String account, String password, String picture, String tel, String address) {
        User newUser = new User();
        newUser.setPassword(password);
        newUser.setAccount(account);
        newUser.setAddress(address);
        newUser.setName(name);
        newUser.setPicture(picture);
        newUser.setTel(tel);
        newUser.setMoney(10000);
        String userID = SnowFlakeUtil.nextId();
        System.out.println("雪花id"+userID);
         newUser.setUserid(userID);
         /*发布事件，新增用户*/
        return userDao.saveOneUser(newUser);
    }

    @Override
    public User getLeaderByGroupId(int groupId)
    {
        Group group = groupDao.getGroupByID(groupId);
        String leaderName = group.getGroupLeader();
        User leader = userDao.getUserByUserName(leaderName);
        return leader;
    }

    @Override
    public String getAuthUserId(Authentication authentication) {
        JSONObject map = JSONObject.fromObject(JSON.toJSONString(authentication.getPrincipal()));
        return (String) map.get("userid");
    }

}
