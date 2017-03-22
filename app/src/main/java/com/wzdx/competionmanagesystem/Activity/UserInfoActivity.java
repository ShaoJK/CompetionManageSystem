package com.wzdx.competionmanagesystem.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wzdx.competionmanagesystem.App.UICommon;
import com.wzdx.competionmanagesystem.JavaBean.Student;
import com.wzdx.competionmanagesystem.JavaBean.Teacher;
import com.wzdx.competionmanagesystem.R;

import org.w3c.dom.Text;

/**
 * Created by sjk on 2016/11/20.
 */

public class UserInfoActivity extends Activity {

    private ImageButton ib_back;
    private TextView tv_edit, tv_study_id, tv_sex, tv_phone,
            tv_email, tv_college, tv_profession, tv_statue, tv_class, tv_id_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
    }

    private void initView() {
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        tv_study_id = (TextView) findViewById(R.id.tv_study_id);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_college = (TextView) findViewById(R.id.tv_college);
        tv_profession = (TextView) findViewById(R.id.tv_profession);
        tv_statue = (TextView) findViewById(R.id.tv_statue);
        tv_class = (TextView) findViewById(R.id.tv_class);
        tv_id_card = (TextView) findViewById(R.id.tv_id_card);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.this.finish();
            }
        });
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(UserInfoActivity.this, EditUserInfoActivity.class),0);
            }
        });

        if (UICommon.INSTANCE.getmIdentity() == UICommon.STUDENT) {
            initStudentView();
        }
        if (UICommon.INSTANCE.getmIdentity() == UICommon.TEACHER) {
            initTeacherView();
        }
    }

    private void initStudentView() {
        Student obj = (Student) UICommon.INSTANCE.getUser();
        tv_study_id.setText(obj.getS_number());
        tv_sex.setText(obj.getSex());
        tv_phone.setText(obj.getPhone());
        tv_email.setText(obj.getEmail());
        tv_college.setText(obj.getA_name());
        tv_profession.setText(obj.getD_name() + " " + obj.getM_name());
        tv_class.setText(obj.getC_name());
        tv_id_card.setText(obj.getIDcard());
        if (obj.getStudent_status() == 1) {
            tv_statue.setText("在校");
        } else {
            tv_statue.setText("毕业");
        }
    }

    private void initTeacherView() {
        findViewById(R.id.ll_class).setVisibility(View.GONE);
        findViewById(R.id.view_line).setVisibility(View.GONE);
        findViewById(R.id.ll_id_card).setVisibility(View.GONE);
        findViewById(R.id.view_line2).setVisibility(View.GONE);
        Teacher obj = (Teacher) UICommon.INSTANCE.getUser();
        tv_study_id.setText(obj.getT_number());
        tv_sex.setText(obj.getSex());
        tv_phone.setText(obj.getPhone());
        tv_email.setText(obj.getEmail());
        tv_college.setText(obj.getA_name());
        tv_profession.setText(obj.getD_name() + " " + obj.getM_name());
        if (obj.getOnjob() == 1) {
            tv_statue.setText("在职");
        } else {
            tv_statue.setText("离校");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case 0://以修改数据
                if (UICommon.INSTANCE.getmIdentity() == UICommon.STUDENT) {
                    initStudentView();
                }
                if (UICommon.INSTANCE.getmIdentity() == UICommon.TEACHER) {
                    initTeacherView();
                }
                break;
            case 1://未修改数据
                break;
        }
    }
}
