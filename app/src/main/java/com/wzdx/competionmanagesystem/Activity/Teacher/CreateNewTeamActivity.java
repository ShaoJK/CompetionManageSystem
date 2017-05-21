package com.wzdx.competionmanagesystem.Activity.Teacher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wzdx.competionmanagesystem.App.UICommon;
import com.wzdx.competionmanagesystem.JavaBean.CompetitionDetail;
import com.wzdx.competionmanagesystem.JavaBean.General;
import com.wzdx.competionmanagesystem.JavaBean.Student;
import com.wzdx.competionmanagesystem.JavaBean.StudentList;
import com.wzdx.competionmanagesystem.R;
import com.wzdx.competionmanagesystem.Utils.MyHttpRequest;
import com.wzdx.competionmanagesystem.Utils.TispToastFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sjk on 2016/11/16.
 * 竞赛队伍列表Activity
 */

public class CreateNewTeamActivity extends Activity implements View.OnClickListener {

    private ImageButton ib_back;
    private TextView tv_title, tv_left;
    private Button btn_add_capation;
    private LinearLayout ll_capation;
    private LinearLayout ll_teamer;
    private Button btn_add_teamer;
    private int mTeamID = -1;
    private Student mCapation;
    private CompetitionDetail mCompetitionDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_team);
        mCompetitionDetail = (CompetitionDetail) getIntent().getSerializableExtra("competitionInfo");
        mTeamID = getIntent().getIntExtra("teamID", -1);
        initView();
        if (mTeamID != -1) {
            initData();
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
                    for (int i = 0; i < list.getData().size(); i++) {
                        View teamerView = null;
                        Student stu = list.getData().get(i);
                        if (stu.getID() == list.getCapationID()) {
                            mCapation = stu;
                            ll_capation.removeAllViews();
                            teamerView = View.inflate(CreateNewTeamActivity.this, R.layout.view_item_teamer, ll_capation);
                            btn_add_capation.setVisibility(View.GONE);
                        } else {
                            teamerView = View.inflate(CreateNewTeamActivity.this, R.layout.view_item_teamer, ll_teamer);
                        }
                        teamerView.setTag(stu);
                        ((TextView) teamerView.findViewById(R.id.tv_teamer_name)).setText(stu.getS_name());
                        teamerView.findViewById(R.id.tv_delete).setOnClickListener(CreateNewTeamActivity.this);
                    }
                } else {
                    TispToastFactory.showTip(list.getMsg());
                }
            }

        };

        if (mCompetitionDetail.getGroup_num() <= ll_teamer.getChildCount() + 1) {
            btn_add_teamer.setVisibility(View.GONE);
        }else{
            btn_add_teamer.setVisibility(View.VISIBLE);
        }
        request.startRequestByPost();
    }


    private void initView() {
        //标题栏
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("创建队伍");
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_left.setText("完成");
        tv_left.setOnClickListener(this);

        btn_add_capation = (Button) findViewById(R.id.btn_add_capation);
        btn_add_capation.setOnClickListener(this);
        ll_capation = (LinearLayout) findViewById(R.id.ll_capation);
        ll_teamer = (LinearLayout) findViewById(R.id.ll_teamer);
        btn_add_teamer = (Button) findViewById(R.id.btn_add_teamer);
        btn_add_teamer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                this.finish();
                break;
            case R.id.tv_left:
                this.finish();
                break;
            case R.id.btn_add_capation:
            case R.id.btn_add_teamer:
                Intent intent = new Intent(this, AddTeamerActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra("data", mCompetitionDetail);
                intent.putExtra("teamID", mTeamID);
                startActivityForResult(intent, 0);
                break;
            case R.id.tv_delete:
                View view = (View) v.getParent();
                Student stu = (Student) ((View) view.getParent()).getTag();
                System.out.println("stuID------------>" + stu.getID());
                if (mCapation.getID() == stu.getID()) {
                    if (ll_teamer.getChildCount() > 0) {//如果删除的是队长并且队员数量不为空
                        new AlertDialog.Builder(this).setMessage("在删除队长前，请先删除所有的队员").
                                setPositiveButton("确定", null).create().show();
                    } else {
                        cancleApply(stu.getID(), mTeamID, 1, (View) v.getParent());
                    }
                } else {
                    cancleApply(stu.getID(), mTeamID, 0, (View) v.getParent());
                }
                break;
        }

    }

    private void cancleApply(int id, int teamID, final int isCapation, final View view) {
        String url = "competitioncontroller/cancleGroupCompApply";
        Map<String, String> params = new HashMap<>();
        params.put("stuID", id + "");
        params.put("teamID", teamID + "");
        params.put("competionID", mCompetitionDetail.getCompID() + "");
        params.put("isCapation", isCapation + "");
        MyHttpRequest request = new MyHttpRequest(this, url, params) {
            @Override
            public void sucessListener(String result) {
                try {
                    General general = new Gson().fromJson(result, General.class);
                    if (general.getSuccess()) {

                        ((ViewGroup)view.getParent()).removeView(view);

                        if (ll_capation.getChildCount() <= 0) {
                            btn_add_capation.setVisibility(View.VISIBLE);
                            btn_add_teamer.setVisibility(View.GONE);
                        } else {
                            btn_add_teamer.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (general.getMsg().equals("未登录")) {
                            UICommon.INSTANCE.toLogin(CreateNewTeamActivity.this);
                        } else {
                            TispToastFactory.showTip(general.getMsg());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    TispToastFactory.showTip("系统错误");
                }

            }
        };
        request.startRequestByPost();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        Student stu = (Student) data.getSerializableExtra("teamer");
        mTeamID = data.getIntExtra("teamID", -1);
        View teamerView = null;
        if (resultCode == -1) {//添加队长
            mCapation = stu;
            ll_capation.removeAllViews();
            teamerView = View.inflate(this, R.layout.view_item_teamer, ll_capation);
            btn_add_capation.setVisibility(View.GONE);
            btn_add_teamer.setVisibility(View.VISIBLE);
        } else {//添加队员
            teamerView = View.inflate(this, R.layout.view_item_teamer, ll_teamer);
            if (mCompetitionDetail.getGroup_num() >= ll_teamer.getChildCount() + 1) {
                btn_add_teamer.setVisibility(View.GONE);
            }
        }
        teamerView.setTag(stu);
        ((TextView) teamerView.findViewById(R.id.tv_teamer_name)).setText(stu.getS_name());
        teamerView.findViewById(R.id.tv_delete).setOnClickListener(this);
    }
}
