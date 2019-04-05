package com.wufuqiang.entity;

/**
 * @ author wufuqiang
 * @ date 2019/4/5/005 - 16:11
 **/
public class ResultMessage {
    private String status ; //状态fail/success
    private String message ;//消息内容

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
