package com.wzdx.competionmanagesystem.JavaBean;

import java.util.List;

/**
 * Created by sjk on 2017/2/8.
 */

public class ContestantList extends General {
    private List<Contestant> data;

    public List<Contestant> getData() {
        return data;
    }

    public void setData(List<Contestant> data) {
        this.data = data;
    }
}
