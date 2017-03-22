package com.wzdx.competionmanagesystem.JavaBean;

/**
 * Created by sjk on 2017/2/8.
 */

public class Contestant {
    private int contID;
    private int apply_id;//报名表ID
    private int iscome;//参加：1  未参加：0
    private int level_id;//奖项等级的ID;
    private String  r_name;//奖项等级的ID;
    private String reward_name;//获奖名称
    private String certificate;//获奖证书地址
    private String reward_file;//获奖证书文件
    private float s_perpoint;//学业业绩分
    private int reward_priority;//优先级

    private String stuNumber;
    private String stuName;//获奖人名称
    private int compID;
    private String compName;//获奖的比赛名称
    private int compType;
    private float teachPoint;//老师的业绩分

    public float getTeachPoint() {
        return teachPoint;
    }

    public void setTeachPoint(float teachPoint) {
        this.teachPoint = teachPoint;
    }

    public String getStuNumber() {
        return stuNumber;
    }

    public void setStuNumber(String stuNumber) {
        this.stuNumber = stuNumber;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public int getCompID() {
        return compID;
    }

    public void setCompID(int compID) {
        this.compID = compID;
    }

    public int getCompType() {
        return compType;
    }

    public void setCompType(int compType) {
        this.compType = compType;
    }

    public int getContID() {
        return contID;
    }

    public void setContID(int contID) {
        this.contID = contID;
    }

    public int getApply_id() {
        return apply_id;
    }

    public void setApply_id(int apply_id) {
        this.apply_id = apply_id;
    }

    public int getIscome() {
        return iscome;
    }

    public void setIscome(int iscome) {
        this.iscome = iscome;
    }

    public int getLevel_id() {
        return level_id;
    }

    public void setLevel_id(int level_id) {
        this.level_id = level_id;
    }

    public String getR_name() {
        return r_name;
    }

    public void setR_name(String r_name) {
        this.r_name = r_name;
    }

    public String getReward_name() {
        return reward_name;
    }

    public void setReward_name(String reward_name) {
        this.reward_name = reward_name;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getReward_file() {
        return reward_file;
    }

    public void setReward_file(String reward_file) {
        this.reward_file = reward_file;
    }

    public float getS_perpoint() {
        return s_perpoint;
    }

    public void setS_perpoint(float s_perpoint) {
        this.s_perpoint = s_perpoint;
    }

    public int getReward_priority() {
        return reward_priority;
    }

    public void setReward_priority(int reward_priority) {
        this.reward_priority = reward_priority;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }
}
