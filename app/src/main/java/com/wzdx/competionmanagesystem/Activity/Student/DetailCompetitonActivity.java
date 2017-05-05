package com.wzdx.competionmanagesystem.Activity.Student;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wzdx.competionmanagesystem.Activity.MyCompetitionActivity;
import com.wzdx.competionmanagesystem.App.UICommon;
import com.wzdx.competionmanagesystem.JavaBean.CompetitionDetail;
import com.wzdx.competionmanagesystem.JavaBean.General;
import com.wzdx.competionmanagesystem.JavaBean.Major;
import com.wzdx.competionmanagesystem.JavaBean.Student;
import com.wzdx.competionmanagesystem.R;
import com.wzdx.competionmanagesystem.Utils.MyHttpRequest;
import com.wzdx.competionmanagesystem.Utils.MyUtilss;
import com.wzdx.competionmanagesystem.Utils.TispToastFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sjk on 2016/11/17.
 */

public class DetailCompetitonActivity extends Activity implements View.OnClickListener {

    private LinearLayout ll_group;
    private TextView tvTime, tv_left, tvGradeLimt, tvMarjorLimit, tvState, tvType,
            tvDescribe, tvMoney, tvStartTime, tvGroupPeople, tvMaxPeople, tvLevel, tvName;
    private Button btn_cancle, btn_check_team;
    private int mID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_competition);
        mID = getIntent().getIntExtra("ID", -1);
        initView();
        initData();
    }


    private void initView() {
        //标题栏
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("比赛详情");
        findViewById(R.id.ib_back).setOnClickListener(this);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_left.setText("查看团队");
        tv_left.setVisibility(View.GONE);
        tv_left.setOnClickListener(this);
        tvName = (TextView) findViewById(R.id.tvName);
        tvLevel = (TextView) findViewById(R.id.tvLevel);
        tvMaxPeople = (TextView) findViewById(R.id.tvMaxPeople);
        tvType = (TextView) findViewById(R.id.tvType);
        ll_group = (LinearLayout) findViewById(R.id.ll_group);
        tvGroupPeople = (TextView) findViewById(R.id.tvGroupPeople);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvGradeLimt = (TextView) findViewById(R.id.tvGradeLimt);
        tvMarjorLimit = (TextView) findViewById(R.id.tvMarjorLimit);
        tvState = (TextView) findViewById(R.id.tvState);
        tvDescribe = (TextView) findViewById(R.id.tvDescribe);
        tvMoney = (TextView) findViewById(R.id.tvMoney);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        btn_check_team = (Button) findViewById(R.id.btn_check_team);

    }

    private void initData() {
        String url = "competitionController/getCompetitionDataDetailById";
        Map<String, String> params = new HashMap<String, String>();
        params.put("ID", mID + "");
        new MyHttpRequest(DetailCompetitonActivity.this, url, params) {
            @Override
            public void sucessListener(String result) {
                Gson gson = new Gson();
                final CompetitionDetail data = gson.fromJson(result, CompetitionDetail.class);
                if (data.getSuccess()) {
                    initShow(data);
                } else {
                    if (data.getMsg().equals("未登录")) {
                        UICommon.INSTANCE.toLogin(DetailCompetitonActivity.this);
                    } else {
                        TispToastFactory.showTip(data.getMsg());
                    }
                }

            }
        }.startRequestByPost();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                this.finish();
                break;
            case R.id.tv_left://查看队伍
                getTeamIDAndToTeamActivity();
                break;
            case R.id.btn_cancle:
                new AlertDialog.Builder(DetailCompetitonActivity.this).setMessage("该操作将会取消你对该比赛的报名\n" +
                        "若您为小队的队长，则队伍的所有成员都会取消报名\n是否确定改操作？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancleApply();
                    }
                }).
                        setNegativeButton("取消", null).create().show();
                break;
            case R.id.btn_check_team:
                getTeamIDAndToTeamActivity();
                break;
        }
    }

    private void cancleApply() {
        if (UICommon.INSTANCE.getmIdentity() != UICommon.STUDENT) {
            return;
        }
        Student student = (Student) UICommon.INSTANCE.getUser();
        String url = "competitionController/cancleCompetitionApply";
        Map<String, String> params = new HashMap<String, String>();
        params.put("stuID", student.getID() + "");
        params.put("competitionID", mID + "");
        new MyHttpRequest(DetailCompetitonActivity.this, url, params) {
            @Override
            public void sucessListener(String result) {
                Gson gson = new Gson();
                final General general = gson.fromJson(result, General.class);
                if (general.getSuccess()) {
                    new AlertDialog.Builder(DetailCompetitonActivity.this).setMessage("成功取消报名").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(DetailCompetitonActivity.this, MyCompetitionActivity.class));
                        }
                    }).create().show();
                } else {
                    if (general.getMsg().equals("未登录")) {
                        UICommon.INSTANCE.toLogin(DetailCompetitonActivity.this);
                    } else {
                        TispToastFactory.showTip(general.getMsg());
                    }
                }

            }
        }.startRequestByPost();
    }


    public void initShow(CompetitionDetail data) {
        tvName.setText(data.getComp_name());
        tvLevel.setText(data.getR_name());
        tvMaxPeople.setText(data.getMax_num() + "");
        tvType.setText(data.getTypeName());
        tvGroupPeople.setText(data.getGroup_num() + "");
        //如果是个人赛则隐藏小组人数这一列
        if (data.getComp_type() == 1) {
            ll_group.setVisibility(View.GONE);
            tv_left.setVisibility(View.GONE);
            btn_check_team.setVisibility(View.GONE);
        } else {
            tv_left.setVisibility(View.VISIBLE);
            btn_check_team.setVisibility(View.VISIBLE);
        }
        tvStartTime.setText(data.getComp_time());
        tvTime.setText(MyUtilss.getTimeFromTimestamp(data.getS_time())
                + "--" + MyUtilss.getTimeFromTimestamp(data.getF_time()));

        tvGradeLimt.setText(data.getGrade_limited());
        ArrayList<Major> majors = data.getLimitedMajor();
        String limitedMajors = "";
        if (majors != null && majors.size() > 0) {
            for (int i = 0; i < majors.size(); i++) {
                limitedMajors += (majors.get(i).getM_name() + " ");
            }
        } else {
            limitedMajors = "无";
        }
        tvMarjorLimit.setText(limitedMajors);
        tvState.setText(data.getStateName());
        tvDescribe.setText(data.getExplained());
        tvMoney.setText("本竞赛需缴纳报名费" + data.getPaymoney() + "元");

        //判断是否可以取消报名（报名时间已过的比赛无法取消报名）
        if (data.getState() == 3) {//报名已结束
            btn_cancle.setEnabled(false);
        }

        btn_cancle.setOnClickListener(this);
        btn_check_team.setOnClickListener(this);
    }

    public void getTeamIDAndToTeamActivity() {
        if (UICommon.INSTANCE.getmIdentity() != UICommon.STUDENT) {
            return;
        }
        Student student = (Student) UICommon.INSTANCE.getUser();
        String url = "competitionController/getTeamID";
        Map<String, String> params = new HashMap<String, String>();
        params.put("stuID", student.getID() + "");
        params.put("competitionID", mID + "");
        new MyHttpRequest(DetailCompetitonActivity.this, url, params) {
            @Override
            public void sucessListener(String result) {
                Gson gson = new Gson();
                final General general = gson.fromJson(result, General.class);
                if (general.getSuccess()) {
                    Intent intent = new Intent(DetailCompetitonActivity.this, MyTeamInfoActivity.class);
                    int teamID = Integer.parseInt(general.getMsg());
                    intent.putExtra("teamID", teamID);
                    intent.putExtra("compID", mID);
                    startActivity(intent);
                } else {
                    if (general.getMsg().equals("未登录")) {
                        UICommon.INSTANCE.toLogin(DetailCompetitonActivity.this);
                    } else {
                        TispToastFactory.showTip(general.getMsg());
                    }
                }
            }
        }.startRequestByPost();
    }
}


