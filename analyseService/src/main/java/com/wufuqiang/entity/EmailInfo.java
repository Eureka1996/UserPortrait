package com.wufuqiang.entity;

/**
 * @ author wufuqiang
 * @ date 2019/4/4/004 - 19:47
 **/
public class EmailInfo {
    private String email ;  //邮箱运营商
    private Long count ; //用户量
    private String groupfield ; //分组

    public EmailInfo() {
    }

    public EmailInfo(String email, Long count) {
        this.email = email;
        this.count = count;
    }

    public EmailInfo(String email, Long count, String groupfield) {
        this.email = email;
        this.count = count;
        this.groupfield = groupfield;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getGroupfield() {
        return groupfield;
    }

    public void setGroupfield(String groupfield) {
        this.groupfield = groupfield;
    }
}
