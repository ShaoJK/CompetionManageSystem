package com.wzdx.competionmanagesystem.JavaBean;

import java.util.ArrayList;

/**
 * Created by sjk on 2017/2/20.
 */

public class AnnouncementList extends General{
    private ArrayList<Announcement> list;

    public ArrayList<Announcement> getList() {
        return list;
    }

    public void setList(ArrayList<Announcement> list) {
        this.list = list;
    }
}
