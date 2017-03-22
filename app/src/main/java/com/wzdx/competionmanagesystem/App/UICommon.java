package com.wzdx.competionmanagesystem.App;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.wzdx.competionmanagesystem.Activity.LoginActivity;
import com.wzdx.competionmanagesystem.JavaBean.Student;
import com.wzdx.competionmanagesystem.JavaBean.Teacher;

import org.apache.commons.logging.Log;

/**
 * Created by sjk on 2016/11/20.
 */

public enum UICommon {
    INSTANCE;

    public static final int STUDENT = 0X01;
    public static final int TEACHER = 0X02;
    public static final int UNLOGIN = 0X00;
    RequestQueue mQueue;
    private int mIdentity;
    private Teacher mTeacher;
    private Student mStudent;

    public int getmIdentity() {
        return mIdentity;
    }

    public void setmIdentity(int mIdentity) {
        this.mIdentity = mIdentity;
    }

    public Object getUser() {
        switch (mIdentity) {
            case STUDENT:
                return mStudent;
            case TEACHER:
                return mTeacher;
        }
        return null;
    }

    public void setUser(Object user, int identity) {
        mIdentity = identity;
        switch (identity) {
            case STUDENT:
                mStudent = (Student) user;
                break;
            case TEACHER:
                mTeacher = (Teacher) user;
                break;
            case UNLOGIN:
                break;
        }
    }

    /**
     * 修改用户保存在内存中的登录密码
     *
     * @param pwd
     */
    public void setUserPwd(String pwd) {
        switch (mIdentity) {
            case STUDENT:
                mStudent.setPwd(pwd);
                break;
            case TEACHER:
                mTeacher.setPwd(pwd);
                break;
            case UNLOGIN:
                break;
        }
    }


    /**
     * 获得统一的全局的Volley的请求队列
     *
     * @param context
     * @return
     */
    public RequestQueue getRequestQueue(Context context) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(context);
        }
        return mQueue;
    }

    public void toLogin(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }
}
