package com.wzdx.competionmanagesystem.JavaBean;

import java.util.ArrayList;

/**
 * Created by sjk on 2016/11/20.
 */

public class StudentList extends General {
    private ArrayList<Student> data;
    private int capationID;

    public int getCapationID() {
        return capationID;
    }

    public void setCapationID(int capationID) {
        this.capationID = capationID;
    }

    public ArrayList<Student> getData() {
        return data;
    }

    public void setData(ArrayList<Student> data) {
        this.data = data;
    }

}
