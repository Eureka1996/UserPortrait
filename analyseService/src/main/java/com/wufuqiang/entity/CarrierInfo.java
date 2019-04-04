package com.wufuqiang.entity;

/**
 * @ author wufuqiang
 * @ date 2019/4/4/004 - 19:47
 **/
public class CarrierInfo {
    private String carrier ;  //运营商
    private Long count ; //用户量
    private String groupfield ; //分组

    public CarrierInfo() {
    }

    public CarrierInfo(String carrier, Long count) {
        this.carrier = carrier;
        this.count = count;
    }

    public CarrierInfo(String carrier, Long count, String groupfield) {
        this.carrier = carrier;
        this.count = count;
        this.groupfield = groupfield;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
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
