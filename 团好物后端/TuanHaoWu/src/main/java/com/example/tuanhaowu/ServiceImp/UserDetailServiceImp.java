package com.example.tuanhaowu.ServiceImp;

import com.example.tuanhaowu.Dao.UserDao;
import com.example.tuanhaowu.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * step5：这里实现了UserDetailsService接口，重写了loadUserByUsername方法，我这里的逻辑是调用userDao，可以用于验证
 * 这里其实涉及user类的重写，所以想更加了解可以转到Entity中的user类看step6
 */
@Service
@CrossOrigin
public class UserDetailServiceImp implements UserDetailsService {
    @Autowired
    UserDao userDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String [] info = username.split("&");
        String uname = info[0];
        String cid = info[1];
        User user = null;
        try {
            user = userDao.checkUser(uname);
            user.setUserCid(cid);
            userDao.saveUser(user);
        }catch (Exception e)
        {
            System.out.println("loadByUserName异常");
        }

        System.out.println("uname = "+uname);
        System.out.println("cid = "+cid);
        return user;

    }
}
