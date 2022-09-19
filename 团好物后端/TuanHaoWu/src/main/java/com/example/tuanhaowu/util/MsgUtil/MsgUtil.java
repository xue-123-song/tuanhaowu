package com.example.tuanhaowu.util.MsgUtil;
import net.sf.json.JSONObject;

public class MsgUtil {
    public static final int SUCCESS = 0;
    public static final int ERROR = -1;
    public static final int LOGIN_USER_ERROR = -100;
    public static final int NOT_LOGGED_IN_ERROR = -101;

    public static final String SUCCESS_MSG = "成功！";
    public static final String REGISTER_SUCCESS = "注册成功！";
    public static final String CHANGE_GROUP_INFO_SUCCESS_MSG = "修改团购信息成功！";
    public static final String CHANGE_GROUP_INFO_FAILED_MSG = "修改团购信息失败！";
    public static final String CREATE_GROUP_SUCCESS_MSG = "创建团购成功！";
    public static final String DELETE_GROUP_SUCCESS_MSG = "删除团购成功！";
    public static final String CANCEL_GROUP_SUCCESS_MSG = "取消团购成功！";
    public static final String LOGIN_SUCCESS_MSG = "登录成功！";
    public static final String LOGOUT_SUCCESS_MSG = "登出成功！";
    public static final String LOGOUT_ERR_MSG = "登出异常！";
    public static final String ERROR_MSG = "错误！";
    public static final String LOGIN_USER_ERROR_MSG = "用户名或密码错误，请重新输入！";
    public static final String NOT_LOGGED_IN_ERROR_MSG = "登录失效，请重新登录！";



    public static Msg makeMsg(MsgCode code, String msg, JSONObject data){
        return new Msg(code, msg, data);
    }

    public static Msg makeMsg(MsgCode code){
        return new Msg(code);
    }

    public static Msg makeMsg(MsgCode code, String msg){
        return new Msg(code, msg);
    }

    public static Msg makeMsg(int status, String msg, JSONObject data){
        return new Msg(status, msg,data);
    }

    public static Msg makeMsg(int status, String msg){
        return new Msg(status, msg);
    }

}
