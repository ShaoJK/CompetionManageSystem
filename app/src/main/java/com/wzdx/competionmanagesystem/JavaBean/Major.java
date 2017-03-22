package com.wzdx.competionmanagesystem.JavaBean;

import java.util.ArrayList;

/**
 * Created by sjk on 2016/11/26.
 */

public class Major extends General {
    private int mID;
    private String m_name;
    private int department_id;

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }
}
