package com.wzdx.competionmanagesystem.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wzdx.competionmanagesystem.Activity.Student.DetailCompetitonActivity;
import com.wzdx.competionmanagesystem.Activity.Teacher.DetailCompetitonTeacherActivity;
import com.wzdx.competionmanagesystem.App.UICommon;
import com.wzdx.competionmanagesystem.JavaBean.CompetitonList;
import com.wzdx.competionmanagesystem.JavaBean.Contestant;
import com.wzdx.competionmanagesystem.JavaBean.SimpleCompetitonData;
import com.wzdx.competionmanagesystem.R;
import com.wzdx.competionmanagesystem.Utils.BaseViewHolder;
import com.wzdx.competionmanagesystem.Utils.MyHttpRequest;
import com.wzdx.competionmanagesystem.Utils.MyUtilss;
import com.wzdx.competionmanagesystem.Utils.TispToastFactory;

/**
 * Created by sjk on 2016/11/16.
 */

public class MyCompetitionActivity extends Activity {


    private ImageButton ib_back;
    private TextView tv_left;
    private android.support.v4.widget.SwipeRefreshLayout srlContent;
    private ListView lv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        initView();
        initData();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("我的竞赛");
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCompetitionActivity.this.finish();
            }
        });
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_left.setText("所获奖项");
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyCompetitionActivity.this, ContestantsActivity.class));
            }
        });
        srlContent = (android.support.v4.widget.SwipeRefreshLayout) findViewById(R.id.srlContent);
        lv_content = (ListView) findViewById(R.id.lv_content);
        //点击竞赛列表，显示具体竞赛的详细信息
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SimpleCompetitonData item = (SimpleCompetitonData) parent.getAdapter().getItem(position);
                Intent intent = null;
                if (UICommon.INSTANCE.getmIdentity() == UICommon.STUDENT) {
                    intent = new Intent(MyCompetitionActivity.this, DetailCompetitonActivity.class);
                } else if (UICommon.INSTANCE.getmIdentity() == UICommon.TEACHER) {
                    intent = new Intent(MyCompetitionActivity.this, DetailCompetitonTeacherActivity.class);
                    intent.putExtra("stuID", item.getStuID());
                }

                intent.putExtra("ID", item.getCompID());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        String url = "";
        //学生获得自己参加的竞赛信息
        if (UICommon.INSTANCE.getmIdentity() == UICommon.STUDENT) {
            url = "competitioncontroller/getStudentCompetitionListById";
            //老师获得自己为指导老师的竞赛信息
        } else if (UICommon.INSTANCE.getmIdentity() == UICommon.TEACHER) {
            url = "competitioncontroller/getTeacherCompetitionListById";
        }
        new MyHttpRequest(MyCompetitionActivity.this, url, null) {
            @Override
            public void sucessListener(String result) {
                Gson gson = new Gson();
                CompetitonList list = gson.fromJson(result, CompetitonList.class);
                if (list.getSuccess()) {
                    lv_content.setAdapter(new CompetitionAdapter(list));
                } else {
                    if (list.getMsg().equals("未登录")) {
                        UICommon.INSTANCE.toLogin(MyCompetitionActivity.this);
                    } else {
                        TispToastFactory.showTip(list.getMsg());
                    }
                }
            }
        }.startRequestByGet();
    }

    class CompetitionAdapter extends BaseAdapter {
        private CompetitonList mList;

        public CompetitonList getmList() {
            return mList;
        }

        public void setmList(CompetitonList mList) {
            this.mList = mList;
        }

        public CompetitionAdapter(CompetitonList list) {
            mList = list;
        }


        @Override
        public int getCount() {
            return mList.getList().size();
        }

        @Override
        public Object getItem(int position) {
            return mList.getList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SimpleCompetitonData item = mList.getList().get(position);
            if (convertView == null) {
                convertView = View.inflate(MyCompetitionActivity.this, R.layout.view_item_my_competition, null);
            }
            TextView tv_comp_name = BaseViewHolder.get(convertView, R.id.tv_comp_name);
            TextView tv_type = BaseViewHolder.get(convertView, R.id.tv_type);
            TextView tv_study_number = BaseViewHolder.get(convertView, R.id.tv_stu_number);
            TextView tv_stu_name = BaseViewHolder.get(convertView, R.id.tv_stu_name);
            tv_comp_name.setText(item.getComp_name());
            tv_type.setText(item.getTypeName());
            tv_study_number.setText(item.getStuNum());
            tv_stu_name.setText(item.getStuName());
            return convertView;
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        initData();
    }


}
