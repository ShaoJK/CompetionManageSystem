package com.wzdx.competionmanagesystem.Activity.Student;

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
import com.wzdx.competionmanagesystem.JavaBean.GroupInfo;
import com.wzdx.competionmanagesystem.JavaBean.GroupInfoList;
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

public class GroupListActivity extends Activity {
    private ImageButton ib_back;
    private MyEditTextView et_select;
    private TextView tv_left;
    private ListView lv_group;

    private int mCompetitonID;
    private int mMaxGroupNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        mCompetitonID = getIntent().getIntExtra("competitionID", -1);
        mMaxGroupNum = getIntent().getIntExtra("maxGroupNum", 0);

        initView();
        inttData();
    }

    private void inttData() {
        String url = "CompetitionController/getTeamInfoListByCompetionID";
        Map<String, String> params = new HashMap<>();
        params.put("competitionID", mCompetitonID + "");
        MyHttpRequest request = new MyHttpRequest(this, url, params) {
            @Override
            public void sucessListener(String result) {
                GroupInfoList list = new Gson().fromJson(result, GroupInfoList.class);
                if (list.getSuccess()) {
                    lv_group.setAdapter(new GroupListAdapoter(GroupListActivity.this, list.getData()));
                } else {
                    TispToastFactory.showTip(list.getMsg());
                }
            }
        };
        request.startRequestByPost();
    }

    private void initView() {
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupListActivity.this.finish();
            }
        });
        et_select = (MyEditTextView) findViewById(R.id.et_select);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_select.getText();
                if (name.isEmpty()) {
                    TispToastFactory.showTip("请输入队长姓名");
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
                if (mMaxGroupNum <= item.getCurrentNum()) {
                    TispToastFactory.showTip("该队伍的人数已经满了，请选择其他队伍");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("data", item);
                    setResult(GroupApplyActivity.CODE_SELECTED_TEAM, intent);
                    GroupListActivity.this.finish();
                }
            }
        });
    }

    //根据队长姓名对队伍进行筛选查找
    private void selectTeamByName(String captainName) {

        String url = "CompetitionController/selectTeamByName";
        Map<String, String> params = new HashMap<>();
        params.put("competitionID", mCompetitonID + "");
        params.put("captainName", captainName);
        MyHttpRequest request = new MyHttpRequest(this, url, params) {
            @Override
            public void sucessListener(String result) {
                GroupInfoList list = new Gson().fromJson(result, GroupInfoList.class);
                if (list.getSuccess()) {
                    GroupListAdapoter adapoter = (GroupListAdapoter) lv_group.getAdapter();
                    adapoter.setmList(list.getData());
                    adapoter.notifyDataSetChanged();
                } else {
                    TispToastFactory.showTip(list.getMsg());
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
            if (item.getCurrentNum() >= mMaxGroupNum) {
                tv_add.setTextColor(getResources().getColor(R.color.background_color));
            } else {
                tv_add.setTextColor(getResources().getColor(R.color.auxiliary_color));
            }
            return convertView;
        }
    }
}
