package com.wzdx.competionmanagesystem.Activity.Teacher;

import android.app.Activity;
import android.content.Context;
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
import com.wzdx.competionmanagesystem.App.UICommon;
import com.wzdx.competionmanagesystem.JavaBean.CompetitionDetail;
import com.wzdx.competionmanagesystem.JavaBean.GroupInfo;
import com.wzdx.competionmanagesystem.JavaBean.GroupInfoList;
import com.wzdx.competionmanagesystem.JavaBean.Teacher;
import com.wzdx.competionmanagesystem.R;
import com.wzdx.competionmanagesystem.Utils.BaseViewHolder;
import com.wzdx.competionmanagesystem.Utils.MyHttpRequest;
import com.wzdx.competionmanagesystem.Utils.TispToastFactory;
import com.wzdx.competionmanagesystem.Widget.MyEditTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sjk on 2016/11/16.
 * 竞赛队伍列表Activity
 */

public class GroupListActivityTeacher extends Activity {
    private ImageButton ib_back;
    private MyEditTextView et_select;
    private TextView tv_left;
    private ListView lv_group;

    private CompetitionDetail mCompetitionDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        mCompetitionDetail = (CompetitionDetail) getIntent().getSerializableExtra("competitionInfo");

        initView();
        inttData();
    }

    private void inttData() {
        selectTeamByName("-1");
    }

    private void initView() {
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupListActivityTeacher.this.finish();
            }
        });
        et_select = (MyEditTextView) findViewById(R.id.et_select);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_select.getText();
                if (name.isEmpty()) {
                    selectTeamByName("-1");
                } else {
                    selectTeamByName(name);
                }
            }
        });
        lv_group = (ListView) findViewById(R.id.lv_group);
        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupInfo item = (GroupInfo) parent.getAdapter().getItem(position);
                Intent intent = new Intent(GroupListActivityTeacher.this, CreateNewTeamActivity.class);
                intent.putExtra("competitionInfo", mCompetitionDetail);
                intent.putExtra("teamID", item.getGroupID());
                System.out.println("id---------->"+item.getGroupID());
                startActivity(intent);
            }
        });
    }

    //根据队长姓名对队伍进行筛选查找
    private void selectTeamByName(String captainName) {

        Teacher teacher = (Teacher) UICommon.INSTANCE.getUser();

        String url = "CompetitionController/getTeamInfoListByCompetionIDAndTeacherID";
        Map<String, String> params = new HashMap<>();
        params.put("competitionID", mCompetitionDetail.getCompID() + "");
        params.put("captainName", captainName);
        params.put("teacherID", teacher.getID() + "");
        MyHttpRequest request = new MyHttpRequest(this, url, params) {
            @Override
            public void sucessListener(String result) {
                try {
                    GroupInfoList list = new Gson().fromJson(result, GroupInfoList.class);
                    if (list.getSuccess()) {
                        if (lv_group.getAdapter() == null) {
                            lv_group.setAdapter(new GroupListAdapoter(GroupListActivityTeacher.this, list.getData()));
                        } else {
                            GroupListAdapoter adapoter = (GroupListAdapoter) lv_group.getAdapter();
                            adapoter.setmList(list.getData());
                            adapoter.notifyDataSetChanged();
                        }
                    } else {
                        TispToastFactory.showTip(list.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        request.startRequestByPost();
    }

    class GroupListAdapoter extends BaseAdapter {
        private ArrayList<GroupInfo> mList;
        private Context mContext;

        public ArrayList<GroupInfo> getmList() {
            return mList;
        }

        public void setmList(ArrayList<GroupInfo> mList) {
            this.mList = mList;
        }

        public GroupListAdapoter(Context context, ArrayList<GroupInfo> list) {
            mList = list;
            mContext = context;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GroupInfo item = mList.get(position);
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.view_item_group_list, null);
            }
            TextView tv_group_id = BaseViewHolder.get(convertView, R.id.tv_group_id);
            tv_group_id.setText("队伍编号: " + item.getGroupID());
            TextView tv_caption_name = BaseViewHolder.get(convertView, R.id.tv_caption_name);
            tv_caption_name.setText("队伍名称: " + item.getCaptainName() + "的小队");
            TextView tv_add = BaseViewHolder.get(convertView, R.id.tv_add);
            if (item.getCurrentNum() >= mCompetitionDetail.getGroup_num()) {
                tv_add.setTextColor(getResources().getColor(R.color.background_color));
            } else {
                tv_add.setTextColor(getResources().getColor(R.color.auxiliary_color));
            }
            return convertView;
        }
    }
}
