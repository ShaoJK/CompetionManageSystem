package com.wzdx.competionmanagesystem.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
 * Created by sjk on 2016/11/20.
 */

public class EditUserInfoActivity extends Activity implements View.OnClickListener {
    private ImageButton ib_back;
    private TextView tv_complished, tv_sex, tv_study_id;
    private EditText ed_phone, ed_email, ed_id_card;
//    private LinearLayout ll_college, ll_profession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        initView();
    }

    private void initView() {
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        tv_complished = (TextView) findViewById(R.id.tv_complished);
        tv_study_id = (TextView) findViewById(R.id.tv_study_id);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_email = (EditText) findViewById(R.id.ed_email);
        ed_id_card = (EditText) findViewById(R.id.ed_id_card);
//        ll_college = (LinearLayout) findViewById(R.id.ll_college);
//        tv_college = (TextView) findViewById(R.id.tv_college);
//        ll_profession = (LinearLayout) findViewById(R.id.ll_profession);
//        tv_profession = (TextView) findViewById(R.id.tv_profession);
//        tv_statue = (TextView) findViewById(R.id.tv_statue);

        ib_back.setOnClickListener(this);
        tv_complished.setOnClickListener(this);
        tv_sex.setOnClickListener(this);
//        ll_college.setOnClickListener(this);
//        ll_profession.setOnClickListener(this);
//        tv_statue.setOnClickListener(this);

        if (UICommon.INSTANCE.getmIdentity() == UICommon.STUDENT) {
            initStudentView();
        }
        if (UICommon.INSTANCE.getmIdentity() == UICommon.TEACHER) {
            initTeacherView();
        }
    }

    private void initTeacherView() {
        findViewById(R.id.ll_id_card).setVisibility(View.GONE);
        Teacher obj = (Teacher) UICommon.INSTANCE.getUser();
        tv_study_id.setText(obj.getT_number());
        tv_sex.setText(obj.getSex());

        ed_email.setText(obj.getEmail());
        ed_phone.setText(obj.getPhone());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                setResult(1);
                this.finish();
                break;
            case R.id.tv_complished:
                if (UICommon.INSTANCE.getmIdentity() == UICommon.STUDENT) {
                    updateStudentInfo();
                }
                if (UICommon.INSTANCE.getmIdentity() == UICommon.TEACHER) {
                    updateTeacherInfo();
                }
                break;
            case R.id.tv_sex:
                showSexChooseDialog();
                break;
        }
    }

    private void updateStudentInfo() {
        final Student obj = (Student) UICommon.INSTANCE.getUser();
        final String sex = tv_sex.getText().toString().trim();
        final String phone = ed_phone.getText().toString().trim() + "";
        final String email = ed_email.getText().toString().trim() + "";
        final String IDcard = ed_id_card.getText().toString().trim() + "";
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("修改中");
        dialog.setCancelable(false);
        dialog.show();
        RequestQueue queue = UICommon.INSTANCE.getRequestQueue(getApplicationContext());
        StringWithCookiesRequest request = new StringWithCookiesRequest(Request.Method.POST, MyHttpRequest.ROOT_URL + "usercontroller/updateStudent",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        dialog.dismiss();
                        System.out.println(s);
                        General general = new Gson().fromJson(s, General.class);
                        TispToastFactory.getToast(EditUserInfoActivity.this, general.getMsg(), Toast.LENGTH_LONG).show();
                        if (general.getSuccess()) {
                            obj.setSex(sex);
                            obj.setPhone(phone);
                            obj.setEmail(email);
                            obj.setIDcard(IDcard);
                            setResult(0);
                            EditUserInfoActivity.this.finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                TispToastFactory.getToast(EditUserInfoActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                System.out.println(volleyError);
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("ID", obj.getID() + "");
                map.put("sex", sex);
                map.put("phone", phone);
                map.put("email", email);
                map.put("IDcard", IDcard);
                map.put("pwd", obj.getPwd());
                return map;
            }
        };
        queue.add(request);
    }

    private void showSexChooseDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).setSingleChoiceItems(new String[]{"男", "女"}, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                tv_sex.setText("男");
                                break;
                            case 1:
                                tv_sex.setText("女");
                                break;
                        }
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    private void initStudentView() {
        Student obj = (Student) UICommon.INSTANCE.getUser();
        tv_study_id.setText(obj.getS_number());
        tv_sex.setText(obj.getSex());

        ed_email.setText(obj.getEmail());
        ed_phone.setText(obj.getPhone());
        ed_id_card.setText(obj.getIDcard());
    }

    private void updateTeacherInfo() {
        final Teacher obj = (Teacher) UICommon.INSTANCE.getUser();
        final String sex = tv_sex.getText().toString().trim();
        final String phone = ed_phone.getText().toString().trim() + "";
        final String email = ed_email.getText().toString().trim() + "";
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("修改中");
        dialog.setCancelable(false);
        dialog.show();
        RequestQueue queue = UICommon.INSTANCE.getRequestQueue(getApplicationContext());
        StringWithCookiesRequest request = new StringWithCookiesRequest(Request.Method.POST, MyHttpRequest.ROOT_URL + "usercontroller/updateTeacher",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        dialog.dismiss();
                        System.out.println(s);
                        General general = new Gson().fromJson(s, General.class);
                        TispToastFactory.getToast(EditUserInfoActivity.this, general.getMsg(), Toast.LENGTH_LONG).show();
                        if (general.getSuccess()) {
                            obj.setSex(sex);
                            obj.setPhone(phone);
                            obj.setEmail(email);
                            setResult(0);
                            EditUserInfoActivity.this.finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                TispToastFactory.getToast(EditUserInfoActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                System.out.println(volleyError);
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("ID", obj.getID() + "");
                map.put("sex", sex);
                map.put("phone", phone);
                map.put("email", email);
                map.put("pwd", obj.getPwd());
                for (Map.Entry enter : map.entrySet()) {
                    System.out.println(enter.getKey()+" "+enter.getValue());
                }
                return map;
            }
        };
        queue.add(request);
    }

}
