package com.wufuqiang.entity;

/**
 * @ author wufuqiang
 * @ date 2019/4/4/004 - 14:36
 **/
public class YearBase {
    private String yeartype ;
    private Long count ;
    private String groupfield ;


    public YearBase() {
    }

    public YearBase(String yeartype, Long count, String groupfield) {
        this.yeartype = yeartype;
        this.count = count;
        this.groupfield = groupfield;
    }

    public String getYeartype() {
        return yeartype;
    }

    public void setYeartype(String yeartype) {
        this.yeartype = yeartype;
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

    @Override
    public String toString() {
        return "YearBase{" +
                "yeartype='" + yeartype + '\'' +
                ", count=" + count +
                ", groupfield='" + groupfield + '\'' +
                '}';
    }
}
