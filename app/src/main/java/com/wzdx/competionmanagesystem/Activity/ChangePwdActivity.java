package com.wzdx.competionmanagesystem.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.wzdx.competionmanagesystem.App.UICommon;
import com.wzdx.competionmanagesystem.JavaBean.General;
import com.wzdx.competionmanagesystem.JavaBean.Student;
import com.wzdx.competionmanagesystem.JavaBean.Teacher;
import com.wzdx.competionmanagesystem.R;
import com.wzdx.competionmanagesystem.Utils.MyHttpRequest;
import com.wzdx.competionmanagesystem.Utils.TispToastFactory;
import com.wzdx.competionmanagesystem.Widget.StringWithCookiesRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sjk on 2016/11/16.
 */

public class ChangePwdActivity extends Activity {
    private ImageButton mIb_back;
    private EditText mEd_old_pwd;
    private EditText mEd_new_pwd;
    private EditText mEd_sure_pwd;
    private Button mBt_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        initView();
    }

    private void initView() {
        mIb_back = (ImageButton) findViewById(R.id.ib_back);
        mEd_old_pwd = (EditText) findViewById(R.id.ed_old_pwd);
        mEd_new_pwd = (EditText) findViewById(R.id.ed_new_pwd);
        mEd_sure_pwd = (EditText) findViewById(R.id.ed_sure_pwd);
        mBt_sure = (Button) findViewById(R.id.bt_sure);

        mIb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePwdActivity.this.finish();
            }
        });
        mBt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPwd = mEd_old_pwd.getText().toString().trim();
                String newPwd = mEd_new_pwd.getText().toString().trim();
                String surePwd = mEd_sure_pwd.getText().toString().trim();
                String pwd = "";
                String number = "";
                switch (UICommon.INSTANCE.getmIdentity()) {
                    case UICommon.STUDENT:
                        pwd = ((Student) UICommon.INSTANCE.getUser()).getPwd();
                        number = ((Student) UICommon.INSTANCE.getUser()).getS_number();
                        break;
                    case UICommon.TEACHER:
                        number = ((Teacher) UICommon.INSTANCE.getUser()).getT_number();
                        pwd = ((Teacher) UICommon.INSTANCE.getUser()).getPwd();
                        break;
                }

                if (oldPwd.isEmpty()) {
                    TispToastFactory.getToast(ChangePwdActivity.this, "请输入原密码", Toast.LENGTH_LONG).show();
                } else if (!pwd.equals(oldPwd)) {
                    TispToastFactory.getToast(ChangePwdActivity.this, "输入密碼錯誤", Toast.LENGTH_LONG).show();
                } else if (newPwd.isEmpty()) {
                    TispToastFactory.getToast(ChangePwdActivity.this, "请输入新密码", Toast.LENGTH_LONG).show();
                } else if (!newPwd.equals(surePwd)) {
                    TispToastFactory.getToast(ChangePwdActivity.this, "密码输入不一致", Toast.LENGTH_LONG).show();
                } else {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("type", UICommon.INSTANCE.getmIdentity() + "");
                    map.put("userNumber", number);
                    map.put("oldPwd", pwd);
                    map.put("newPwd", newPwd);
                    changeUserPwd(map);
                }
            }
        });
    }

    private void changeUserPwd(final Map<String, String> map) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("操作请求中");
        dialog.setCancelable(false);
        dialog.show();

        RequestQueue queue = UICommon.INSTANCE.getRequestQueue(getApplicationContext());
        StringWithCookiesRequest request = new StringWithCookiesRequest(Request.Method.POST,
                MyHttpRequest.ROOT_URL + "usercontroller/changeUserPwd", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dialog.dismiss();
                Gson gson = new Gson();
                if (!s.isEmpty()) {
                    General result = gson.fromJson(s, General.class);
                    TispToastFactory.getToast(ChangePwdActivity.this, result.getMsg(), Toast.LENGTH_LONG).show();
                    if (result.getSuccess()) {
                        ChangePwdActivity.this.finish();
                        UICommon.INSTANCE.setUserPwd(map.get("newPwd"));
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                TispToastFactory.getToast(ChangePwdActivity.this, "网络异常", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };

        queue.add(request);
    }


}
