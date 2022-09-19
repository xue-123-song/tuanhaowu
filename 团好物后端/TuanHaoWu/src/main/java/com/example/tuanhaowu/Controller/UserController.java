package com.example.tuanhaowu.Controller;

import com.example.tuanhaowu.Entity.User;
import com.example.tuanhaowu.Service.UserService;
import com.example.tuanhaowu.util.AuthUtil;
import com.example.tuanhaowu.util.MsgUtil.Msg;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

;
@RestController
public class UserController {
@Autowired
    UserService userService;
    @Autowired
    private StringEncryptor stringEncryptor;

/**
* 7/20 更新，修改返回值，增加密码加密
* ！！！！！！！！！
* important note:因为修改了密码的匹配机制，所以之前注册的密码没有进行加密的用户的登录将无法成功，只有新注册的用户，密码经过加密
*                存储的用户才能正常登录
 *                by Xu
* */
//    @CrossOrigin
//    @RequestMapping("/loginCheck")
//    public User check(@RequestBody Map<String,String> params) throws Exception {
//        String account = params.get("account");
//        String password = params.get("password");
//        return userService.checkUser(account,password);
//
//    }
    @RequestMapping("/queryUserInfo/{userid}")
    public User queryUserInfo(@PathVariable("userid") String userid) {
        System.out.println("enter into queryUserInfo api");
        System.out.println("userId = "+AuthUtil.getAuthUserId());
        /*用户只能获取自己的信息，所以如果访问其它人的信息会失败*/
        if (!AuthUtil.getAuthUserId().equals(userid))
        {
            System.out.println("您没有权限获取其它用户的个人信息");
            return null;
        }
        else {
            return userService.getUserInfo(userid);
        }
    }

    /**
    * 实现注册功能
    * 传入参数的形式例子： {
    "name": "周翔",
    "account":"Mr Zhou",
    "password":"123456",
    "picture": "http://zhou.com",
    "tel":"18290207267",
    "address": "东川路800号",
        }
    *
    * 返回一个msg，其中status = 0，account和name都没有重复，可以正常注册成功
    * status = 1， 2 ， 3 分别代表account和name有一个或者两个重复
    * 经过postman测试可以正常返回
    * By Xu
    *
    * ！！！！新增： status = -1， -2 的时候表示name和account为空的异常情况，发回的msg如下：
    * 虽然好像没啥用 QAQ
    * {
	"status": -1,
	"msg": "name不能为空",
	"data": null
}
    * */
    @RequestMapping("/tourist/register")
    public Msg register(@RequestBody Map<String,String> userInfo)
    {
        /*解析发过来的参数*/
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String name = userInfo.get("name");
        String account = userInfo.get("account");
        String password = userInfo.get("password");
        String storePwd = passwordEncoder.encode(password);
        /*如果name或者account重复了*/
        Msg msg = userService.checkRepeated(name,account);
        /*下面是可选填的信息，如果前端没有发回相应的字段，回默认设置为null，无需担心*/
        String picture,tel,address;
        picture = userInfo.get("picture");
        tel = userInfo.get("tel");
        address = userInfo.get("address");
        if (msg.getStatus() == 0)
        {
         userService.saveOneUser(name,account,storePwd,picture,stringEncryptor.encrypt(tel),stringEncryptor.encrypt(address));
        }
        return msg;
    }
}
