package com.wufuqiang.entity;

/**
 * @ author wufuqiang
 * @ date 2019/4/6/006 - 14:45
 **/
public class BrandLike {
    private String brand;
    private long count;
    private String groupbyfield;

    public BrandLike() {
    }

    public BrandLike(String brand, long count, String groupbyfield) {
        this.brand = brand;
        this.count = count;
        this.groupbyfield = groupbyfield;
    }

    public String getGroupbyfield() {
        return groupbyfield;
    }

    public void setGroupbyfield(String groupbyfield) {
        this.groupbyfield = groupbyfield;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
