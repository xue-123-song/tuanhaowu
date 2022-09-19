package com.example.tuanhaowu.Dao;

import com.example.tuanhaowu.Entity.User;

import java.util.List;

public interface UserDao {
    User checkUser(String account) ;
    String getLeaderName(String userId);

    User getUserByUserId(String userId);
    List<User> getAll();
    void saveUser(User user);
    User getUserByUserName(String userName);
    User saveOneUser(User newUser);
    int  checkRepeated(String name,String account);
    List<String> getCidByGroupId(Integer groupId);
}
