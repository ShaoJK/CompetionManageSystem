package com.wzdx.competionmanagesystem.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wzdx.competionmanagesystem.App.UICommon;
import com.wzdx.competionmanagesystem.JavaBean.Student;
import com.wzdx.competionmanagesystem.JavaBean.Teacher;
import com.wzdx.competionmanagesystem.R;
import com.wzdx.competionmanagesystem.Utils.MyHttpRequest;
import com.wzdx.competionmanagesystem.Utils.TispToastFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sjk on 2016/11/17.
 */

public class LoginActivity extends Activity {
    private ImageView img_back;
    private EditText ed_study_id, ed_pwd;
    private Button btnLogin;
    private String mTargetName;
    private Bundle mBundle;
    private RadioGroup rg_identity_choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            mTargetName = bundle.getString("target");
            if (mTargetName.isEmpty()) {
                mTargetName = MainActivity.class.getName();
            }
            mBundle = bundle.getBundle("bundle");
        }else{
            mTargetName = MainActivity.class.getName();
        }
        initView();
    }

    private void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        ed_study_id = (EditText) findViewById(R.id.ed_study_id);
        ed_pwd = (EditText) findViewById(R.id.ed_pwd);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        rg_identity_choose = (RadioGroup) findViewById(R.id.rg_identity_choose);
        rg_identity_choose.check(R.id.rb_student);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studyID = ed_study_id.getText().toString().trim();
                String pwd = ed_pwd.getText().toString().trim();
                if (studyID.isEmpty()) {
                    TispToastFactory.getToast(LoginActivity.this, "请输入学工号", Toast.LENGTH_LONG).show();
                } else if (pwd.isEmpty()) {
                    TispToastFactory.getToast(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
                } else {
                    if (rg_identity_choose.getCheckedRadioButtonId() == R.id.rb_student) {
                        login(1, studyID, pwd);
                    } else if (rg_identity_choose.getCheckedRadioButtonId() == R.id.rb_teacher) {
                        login(2, studyID, pwd);
                    }
                }
            }
        });
    }

    public void login(final int type, final String userName, final String pwd) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type + "");
        params.put("username", userName);
        params.put("password", pwd);
        MyHttpRequest request = new MyHttpRequest(this,  "usercontroller/login", params) {
            @Override
            public void sucessListener(String result) {
                Gson gson = new Gson();
                switch (type) {
                    case 1:
                        Student obj = gson.fromJson(result, Student.class);
                        if (obj.getSuccess()) {
                            UICommon.INSTANCE.setUser(obj, UICommon.STUDENT);
                            startNextActivity();
                        } else {
                            TispToastFactory.getToast(LoginActivity.this, obj.getMsg(), Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 2:
                        Teacher obj2 = gson.fromJson(result, Teacher.class);
                        if (obj2.getSuccess()) {
                            UICommon.INSTANCE.setUser(obj2, UICommon.TEACHER);
                            startNextActivity();
                        } else {
                            TispToastFactory.getToast(LoginActivity.this, obj2.getMsg(), Toast.LENGTH_LONG).show();
                        }
                }
            }
        };
        request.startRequestByPost();
    }

    private void startNextActivity() {
        Intent intent = new Intent();
        intent.setClassName(getPackageName(), mTargetName);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        startActivity(intent);
        LoginActivity.this.finish();
    }



}
