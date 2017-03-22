package com.wzdx.competionmanagesystem.Activity.Student;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wzdx.competionmanagesystem.Activity.MyCompetitionActivity;
import com.wzdx.competionmanagesystem.App.UICommon;
import com.wzdx.competionmanagesystem.JavaBean.General;
import com.wzdx.competionmanagesystem.JavaBean.Student;
import com.wzdx.competionmanagesystem.JavaBean.StudentList;
import com.wzdx.competionmanagesystem.R;
import com.wzdx.competionmanagesystem.Utils.BaseViewHolder;
import com.wzdx.competionmanagesystem.Utils.MyHttpRequest;
import com.wzdx.competionmanagesystem.Utils.TispToastFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sjk on 2016/11/16.
 * 竞赛队伍列表Activity
 */

public class MyTeamInfoActivity extends Activity implements View.OnClickListener {
    private ImageButton ib_back;
    private TextView tv_title;
    private ListView lv_teamer;
    private Button quit_team;
    private int mTeamID;
    private int mCompID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team_info);
        mTeamID = getIntent().getIntExtra("teamID", -1);
        mCompID = getIntent().getIntExtra("compID", -1);
        initView();
        initData();
    }

    private void initView() {
        //标题栏
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("我的队伍");

        lv_teamer = (ListView) findViewById(R.id.lv_teamer);
        quit_team = (Button) findViewById(R.id.quit_team);
        quit_team.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                this.finish();
                break;
            case R.id.quit_team:
                cancleApply();
                break;
        }
    }


    private void initData() {
        String url = "competitioncontroller/getAllTeamInfoByGroupID";
        Map<String, String> params = new HashMap<>();
        params.put("teamID", mTeamID + "");
        MyHttpRequest request = new MyHttpRequest(this, url, params) {
            @Override
            public void sucessListener(String result) {
                StudentList list = new Gson().fromJson(result, StudentList.class);
                if (list.getSuccess()) {
                    lv_teamer.setAdapter(new TeamerAdapter(list));
                } else {
                    TispToastFactory.showTip(list.getMsg());
                }
            }

        };
        request.startRequestByPost();
    }

    private void cancleApply() {
        if (UICommon.INSTANCE.getmIdentity() != UICommon.STUDENT) {
            return;
        }
        Student student = (Student) UICommon.INSTANCE.getUser();
        String url = "competitionController/cancleCompetitionApply";
        Map<String, String> params = new HashMap<String, String>();
        params.put("stuID", student.getID() + "");
        params.put("competitionID", mCompID + "");
        new MyHttpRequest(MyTeamInfoActivity.this, url, params) {
            @Override
            public void sucessListener(String result) {
                Gson gson = new Gson();
                final General general = gson.fromJson(result, General.class);
                if (general.getSuccess()) {
                    new AlertDialog.Builder(MyTeamInfoActivity.this).setMessage("成功取消报名").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MyTeamInfoActivity.this, MyCompetitionActivity.class));
                        }
                    }).create().show();
                } else {
                    if (general.getMsg().equals("未登录")) {
                        UICommon.INSTANCE.toLogin(MyTeamInfoActivity.this);
                    } else {
                        TispToastFactory.showTip(general.getMsg());
                    }
                }

            }
        }.startRequestByPost();
    }

    class TeamerAdapter extends BaseAdapter {
        private StudentList mList;

        private TeamerAdapter(StudentList list) {
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.getData().size();
        }

        @Override
        public Object getItem(int position) {
            return mList.getData().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(MyTeamInfoActivity.this, R.layout.view_item_my_teamer, null);
            }
            Student stu = mList.getData().get(position);
            TextView tv1 = BaseViewHolder.get(convertView, R.id.tv1);
            if (mList.getCapationID() == stu.getID()) {
                tv1.setText("队长名称");
            } else {
                tv1.setText("队员名称");
            }

            TextView tv_teamr_name = BaseViewHolder.get(convertView, R.id.tv_teamer_name);
            TextView tv_study_number = BaseViewHolder.get(convertView, R.id.tv_study_number);
            tv_teamr_name.setText(stu.getS_name());
            tv_study_number.setText(stu.getS_number());
            return convertView;
        }
    }
}
