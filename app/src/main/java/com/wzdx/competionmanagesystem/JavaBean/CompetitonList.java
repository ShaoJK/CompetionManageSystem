package com.wzdx.competionmanagesystem.JavaBean;

import java.util.ArrayList;

/**
 * Created by sjk on 2016/11/26.
 */

public class CompetitonList extends General {
    private ArrayList<SimpleCompetitonData> list;

    public ArrayList<SimpleCompetitonData> getList() {
        return list;
    }

    public void setList(ArrayList<SimpleCompetitonData> list) {
        this.list = list;
    }
}
