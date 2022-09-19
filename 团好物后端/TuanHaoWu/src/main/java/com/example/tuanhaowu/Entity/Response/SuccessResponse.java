package com.example.tuanhaowu.Entity.Response;

public class SuccessResponse {
    //0代表出错，1代表成功
    int success;
    //信息
    String meg;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMeg() {
        return meg;
    }

    public void setMeg(String meg) {
        this.meg = meg;
    }
}
