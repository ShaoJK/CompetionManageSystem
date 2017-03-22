package com.wzdx.competionmanagesystem.Activity.Teacher;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wzdx.competionmanagesystem.App.UICommon;
import com.wzdx.competionmanagesystem.JavaBean.CompetitionDetail;
import com.wzdx.competionmanagesystem.JavaBean.Teacher;
import com.wzdx.competionmanagesystem.R;

/**
 * Created by sjk on 2016/11/17.
 */

public class GroupApplyTeacherActivity extends Activity implements View.OnClickListener {
    private TextView tvRed2, tvTeacher, tvCompName;
    private Button btApply;
    private CompetitionDetail mCompetitionDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_group_teacher);
        Bundle bundle = getIntent().getExtras();
        mCompetitionDetail = (CompetitionDetail) bundle.getSerializable("data");
        initView();
    }

    private void initView() {
        //标题栏
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("竞赛报名");
        findViewById(R.id.ib_back).setOnClickListener(this);

        tvCompName = (TextView) findViewById(R.id.tvCompName);
        tvRed2 = (TextView) findViewById(R.id.tvRed2);
        tvTeacher = (TextView) findViewById(R.id.tvTeacher);
        tvTeacher.setTag(-1);
        btApply = (Button) findViewById(R.id.btApply);
        btApply.setOnClickListener(this);
        //竞赛名称
        tvCompName.setText(mCompetitionDetail.getComp_name());

        //是否需要知道老师
        if (mCompetitionDetail.getIsadviser() == 1) {
            tvRed2.setVisibility(View.VISIBLE);
        } else {
            tvRed2.setVisibility(View.INVISIBLE);
        }
        //指导老师设置
        if (UICommon.INSTANCE.getmIdentity() == UICommon.TEACHER) {
            Teacher tea = (Teacher) UICommon.INSTANCE.getUser();
            tvTeacher.setText(tea.getT_name());
            tvTeacher.setTag(tea.getID());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                this.finish();
                break;
            case R.id.ll_group:
                break;
            case R.id.btApply:
                showChooseGroup();
                break;
        }
    }

    /**
     * 弹框选择创建小队还是选择已有的小队
     */
    private void showChooseGroup() {
        AlertDialog dialog = new AlertDialog.Builder(this).setItems(new String[]{"选择已有队伍", "创建新的队伍"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == 0) {//选择已有队伍
                    //前往队伍列表页
                    Intent intent = new Intent(GroupApplyTeacherActivity.this, GroupListActivityTeacher.class);
                    intent.putExtra("competitionInfo",mCompetitionDetail);
                    startActivity(intent);
                } else {//创建新的队伍
                    Intent intent = new Intent(GroupApplyTeacherActivity.this,CreateNewTeamActivity.class);
                    intent.putExtra("competitionInfo",mCompetitionDetail);
                    startActivity(intent);
                }
            }
        }).create();
        dialog.show();
    }

}


