package com.wufuqiang.logic;

/**
 * @ author wufuqiang
 * @ date 2019/4/12/012 - 10:22
 **/
public class LogicInfo {
    private String variable1 ;
    private String variable2 ;
    private String variable3 ;
    private String labase ;
    private String groupbyfield ;

    public LogicInfo() {
    }

    public LogicInfo(String variable1, String variable2, String variable3, String labase) {
        this.variable1 = variable1;
        this.variable2 = variable2;
        this.variable3 = variable3;
        this.labase = labase;
    }

    public LogicInfo(String variable1, String variable2, String variable3, String labase, String groupbyfield) {
        this.variable1 = variable1;
        this.variable2 = variable2;
        this.variable3 = variable3;
        this.labase = labase;
        this.groupbyfield = groupbyfield;
    }

    public String getVariable1() {
        return variable1;
    }

    public void setVariable1(String variable1) {
        this.variable1 = variable1;
    }

    public String getVariable2() {
        return variable2;
    }

    public void setVariable2(String variable2) {
        this.variable2 = variable2;
    }

    public String getVariable3() {
        return variable3;
    }

    public void setVariable3(String variable3) {
        this.variable3 = variable3;
    }

    public String getLabase() {
        return labase;
    }

    public void setLabase(String labase) {
        this.labase = labase;
    }

    public String getGroupbyfield() {
        return groupbyfield;
    }

    public void setGroupbyfield(String groupbyfield) {
        this.groupbyfield = groupbyfield;
    }
}
