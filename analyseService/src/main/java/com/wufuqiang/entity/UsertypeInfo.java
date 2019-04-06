package com.wufuqiang.entity;

/**
 * @ author wufuqiang
 * @ date 2019/4/6/006 - 16:31
 **/
public class UsertypeInfo {
    private String usertype ;
    private Long count ;
    private String groupbyfield ;

    public UsertypeInfo() {
    }

    public UsertypeInfo(String usertype, Long count, String groutbyfield) {
        this.usertype = usertype;
        this.count = count;
        this.groupbyfield = groutbyfield;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getGroupbyfield() {
        return groupbyfield;
    }

    public void setGroupbyfield(String groutbyfield) {
        this.groupbyfield = groutbyfield;
    }
}
