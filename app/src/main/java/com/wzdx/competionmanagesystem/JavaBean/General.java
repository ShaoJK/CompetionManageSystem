package com.wzdx.competionmanagesystem.JavaBean;

import java.io.Serializable;

/**
 * Created by sjk on 2016/11/26.
 */

public class General implements Serializable {
    private Boolean success;
    private String msg;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
