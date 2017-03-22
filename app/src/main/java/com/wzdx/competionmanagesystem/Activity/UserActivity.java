package com.wzdx.competionmanagesystem.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wzdx.competionmanagesystem.App.UICommon;
import com.wzdx.competionmanagesystem.App.MyApplication;
import com.wzdx.competionmanagesystem.JavaBean.Student;
import com.wzdx.competionmanagesystem.JavaBean.Teacher;
import com.wzdx.competionmanagesystem.R;
import com.wzdx.competionmanagesystem.Utils.SPUtil;

/**
 * Created by sjk on 2016/11/16.
 */

public class UserActivity extends Activity implements View.OnClickListener {
    private TextView tv_back, tv_username, tv_class, tv_competition_history,
            tv_user_info_detail, tv_alter_login_pwd;
    private Button btn_cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
    }

    private void initView() {
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_class = (TextView) findViewById(R.id.tv_class);
        tv_competition_history = (TextView) findViewById(R.id.tv_competition_history);
        tv_user_info_detail = (TextView) findViewById(R.id.tv_user_info_detail);
        tv_alter_login_pwd = (TextView) findViewById(R.id.tv_alter_login_pwd);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);

        tv_back.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
        tv_competition_history.setOnClickListener(this);
        tv_user_info_detail.setOnClickListener(this);
        tv_alter_login_pwd.setOnClickListener(this);

        if (UICommon.INSTANCE.getmIdentity() == UICommon.STUDENT) {
            Student student = (Student) UICommon.INSTANCE.getUser();
            tv_username.setText(student.getS_name());
            tv_class.setText(student.getA_name() + " " + student.getC_name());
        }
        if (UICommon.INSTANCE.getmIdentity() == UICommon.TEACHER) {
            Teacher teacher = (Teacher) UICommon.INSTANCE.getUser();
            tv_username.setText(teacher.getT_name());
            tv_class.setText(teacher.getA_name() + " " + teacher.getD_name());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                this.finish();
                break;
            case R.id.tv_user_info_detail:
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
            case R.id.btn_cancle:
                cancleLogin();//退出登录
                break;
            case R.id.tv_alter_login_pwd:
                startActivity(new Intent(this, ChangePwdActivity.class));
                break;
            case R.id.tv_competition_history:
                if (UICommon.INSTANCE.getUser() == null) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("target", MyCompetitionActivity.class.getName());
                    startActivity(intent);
                } else {
                    startActivity(new Intent(this, MyCompetitionActivity.class));
                    break;
                }
                break;
        }
    }

    private void cancleLogin() {

        UICommon.INSTANCE.setUser(null, UICommon.UNLOGIN);
        SPUtil.deleteString(this, MyApplication.COOKIES);//删除储存的cookies
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("target",UserActivity.class.getName());
        startActivity(intent);
        this.finish();
    }
}
