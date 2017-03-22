package com.wzdx.competionmanagesystem.JavaBean;

import java.util.ArrayList;

/**
 * Created by sjk on 2016/12/25.
 */

public class CompetitionDetail extends General {

    private ArrayList<Major> limitedMajor;
    private int compID;
    private String comp_name;
    private String explained;
    private int max_num;
    private int group_num;
    private String comp_time;
    private int s_time;
    private int f_time;
    private int comp_type;
    private int state;
    private String major_limited;
    private String grade_limited;
    private int isproof;
    private int isadviser;
    private int isIDcard;
    private int paymoney;
    private int level_id;
    private String years_y;
    private String years_t;
    private String r_name;

    public String getTypeName() {
        if (comp_type == 2) {
            return "小組赛";
        } else if (comp_type == 1) {
            return "个人赛";
        }
        System.out.println("comp_type" + comp_type);
        return "个人";
    }

    public ArrayList<Major> getLimitedMajor() {
        return limitedMajor;
    }

    public void setLimitedMajor(ArrayList<Major> limitedMajor) {
        this.limitedMajor = limitedMajor;
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

    public String getExplained() {
        return explained;
    }

    public void setExplained(String explained) {
        this.explained = explained;
    }

    public int getMax_num() {
        return max_num;
    }

    public void setMax_num(int max_num) {
        this.max_num = max_num;
    }

    public int getGroup_num() {
        return group_num;
    }

    public void setGroup_num(int group_num) {
        this.group_num = group_num;
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

    public int getComp_type() {
        return comp_type;
    }

    public void setComp_type(int comp_type) {
        this.comp_type = comp_type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMajor_limited() {
        return major_limited;
    }

    public void setMajor_limited(String major_limited) {
        this.major_limited = major_limited;
    }

    public String getGrade_limited() {
        return grade_limited;
    }

    public void setGrade_limited(String grade_limited) {
        this.grade_limited = grade_limited;
    }

    public int getIsproof() {
        return isproof;
    }

    public void setIsproof(int isproof) {
        this.isproof = isproof;
    }

    public int getIsadviser() {
        return isadviser;
    }

    public void setIsadviser(int isadviser) {
        this.isadviser = isadviser;
    }

    public int getIsIDcard() {
        return isIDcard;
    }

    public void setIsIDcard(int isIDcard) {
        this.isIDcard = isIDcard;
    }

    public int getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(int paymoney) {
        this.paymoney = paymoney;
    }

    public int getLevel_id() {
        return level_id;
    }

    public void setLevel_id(int level_id) {
        this.level_id = level_id;
    }

    public String getYears_y() {
        return years_y;
    }

    public void setYears_y(String years_y) {
        this.years_y = years_y;
    }

    public String getYears_t() {
        return years_t;
    }

    public void setYears_t(String years_t) {
        this.years_t = years_t;
    }

    public String getR_name() {
        return r_name;
    }

    public void setR_name(String r_name) {
        this.r_name = r_name;
    }

    public String getStateName() {
        String name = "未开始报名";
        switch (state) {
            case 1:
                name = "未开始报名";
                break;
            case 2:
                name = "报名进行中";
                break;
            case 3:
                name = "报名已结束";
                break;
        }
        return name;
    }
}
