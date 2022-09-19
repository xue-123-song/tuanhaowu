package com.example.tuanhaowu.util.MsgUtil;

public enum MsgCode {
    SUCCESS(MsgUtil.SUCCESS, MsgUtil.SUCCESS_MSG),
    /*对于团购的创建。修改和删除信息*/
    CHANGE_SUCCESS(MsgUtil.SUCCESS,MsgUtil.CHANGE_GROUP_INFO_SUCCESS_MSG),
    LOGIN_SUCCESS(MsgUtil.SUCCESS,MsgUtil.LOGIN_SUCCESS_MSG),
    CREATE_GROUP_SUCCESS(MsgUtil.SUCCESS,MsgUtil.CREATE_GROUP_SUCCESS_MSG),
    DELETE_GROUP_SUCCESS(MsgUtil.SUCCESS,MsgUtil.DELETE_GROUP_SUCCESS_MSG),
    CANCEL_GROUP_SUCCESS(MsgUtil.SUCCESS,MsgUtil.CANCEL_GROUP_SUCCESS_MSG),
    CHANGE_ERROR(MsgUtil.ERROR,MsgUtil.CHANGE_GROUP_INFO_FAILED_MSG),




    ERROR(MsgUtil.ERROR,MsgUtil.ERROR_MSG),
    LOGIN_USER_ERROR(MsgUtil.LOGIN_USER_ERROR,MsgUtil.LOGIN_USER_ERROR_MSG),
    NOT_LOGGED_IN_ERROR(MsgUtil.NOT_LOGGED_IN_ERROR,MsgUtil.NOT_LOGGED_IN_ERROR_MSG);

    private int status;
    private String msg;

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    private MsgCode(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
