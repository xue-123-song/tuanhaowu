package com.example.tuanhaowu.DaoImp;

import com.example.tuanhaowu.Dao.UserDao;
import com.example.tuanhaowu.Entity.User;
import com.example.tuanhaowu.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImp implements UserDao {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
   public User checkUser(String account){
        return userRepository.checkUser(account);
    }

    @Override
    public String getLeaderName(String userId) {
        try {

            return userRepository.getById(userId).getName();
        }catch (Exception e)
        {
            System.out.println(userId);
            System.out.println("get LeaderName failed");
            return null;
        }

    }

    @Override
    public User getUserByUserId(String userId) {
        return userRepository.getUserByUserid(userId);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserByUserName(String userName) {

        return userRepository.getUserByName(userName);
    }

    public User saveOneUser(User newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public int checkRepeated(String name, String account) {
        if (name == null)
        {
            return -1;
        }
        if (account == null)
        {
            return -2;
        }
        List<User> user1 = userRepository.checkNameRepeated(name);
        List<User> user2 = userRepository.checkAccountRepeated(account);
        if (!user1.isEmpty() && !user2.isEmpty()) {
            System.out.println("name和 account 都重复了！"+user1.get(0).toString());
            return 3;
        }
        else if(!user1.isEmpty())
        {
            System.out.println("name重复了！"+user1.get(0).toString());
            return 2;
        }
        else if (!user2.isEmpty())
        {
            System.out.println("account重复了！"+user2.get(0).toString());
            return 1;
        }
        else {
            System.out.println("没有重复");
            return 0;
        }
    }

    //ToDo:finish function
    @Override
    public List<String> getCidByGroupId(Integer groupId) {

        return null;
    }

    @Override
    public List<User> getAll() {
        return  userRepository.findAll();
    }
}
