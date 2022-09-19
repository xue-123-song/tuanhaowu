package com.example.tuanhaowu.Entity.Response;

public class SecKillPollingResponse {
    private Boolean isSuccess;
    //请求响应码，成功时为0
    private int responseCode;
    //请求响应码对应描述
    private String responseMsg;

    private int refreshTime;   //下一次请求刷新时间
    private long orderId;     //订单ID
    private String orderCode;  //订单编码
    private String orderQualificationCode;  //下单资格码


    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderQualificationCode() {
        return orderQualificationCode;
    }

    public void setOrderQualificationCode(String orderQualificationCode) {
        this.orderQualificationCode = orderQualificationCode;
    }
}
