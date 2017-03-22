package com.wzdx.competionmanagesystem.JavaBean;

/**
 * Created by sjk on 2016/11/26.
 */

public class SimpleCompetitonData {
    private int s_time;
    private int f_time;
    private int compID;
    private String comp_name;
    private String comp_time;
    private int comp_type;
    private int state;
    private int stuID;//参加该比赛的学生ID;
    private String stuNum;
    private String stuName;

    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public int getStuID() {
        return stuID;
    }

    public void setStuID(int stuID) {
        this.stuID = stuID;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getComp_time() {
        return comp_time;
    }

    public void setComp_time(String comp_time) {
        this.comp_time = comp_time;
    }

    public int getS_time() {
        return s_time;
    }

    public void setS_time(int s_time) {
        this.s_time = s_time;
    }

    public int getF_time() {
        return f_time;
    }

    public void setF_time(int f_time) {
        this.f_time = f_time;
    }

    public int getCompID() {
        return compID;
    }

    public void setCompID(int compID) {
        this.compID = compID;
    }

    public String getComp_name() {
        return comp_name;
    }

    public void setComp_name(String comp_name) {
        this.comp_name = comp_name;
    }

    public int getComp_type() {
        return comp_type;
    }

    public void setComp_type(int comp_type) {
        this.comp_type = comp_type;
    }


    public String getTypeName() {
        if (comp_type == 2) {
            return "小組赛";
        } else if (comp_type == 1) {
            return "个人赛";
        }
        System.out.println("comp_type"+comp_type);
        return "个人";
    }
}
