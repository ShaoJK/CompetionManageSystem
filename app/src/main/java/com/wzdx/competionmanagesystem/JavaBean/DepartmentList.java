package com.wzdx.competionmanagesystem.JavaBean;

import java.util.ArrayList;

/**
 * Created by sjk on 2017/1/8.
 */

public class DepartmentList extends General {
    private ArrayList<Department> data;

    public ArrayList<Department> getData() {
        return data;
    }

    public void setData(ArrayList<Department> data) {
        this.data = data;
    }
}
