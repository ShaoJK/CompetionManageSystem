package com.wzdx.competionmanagesystem.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wzdx.competionmanagesystem.Activity.Student.GroupApplyActivity;
import com.wzdx.competionmanagesystem.Activity.Student.SingleApplyActivity;
import com.wzdx.competionmanagesystem.App.UICommon;
import com.wzdx.competionmanagesystem.JavaBean.CompetitionDetail;
import com.wzdx.competionmanagesystem.JavaBean.CompetitonList;
import com.wzdx.competionmanagesystem.JavaBean.Major;
import com.wzdx.competionmanagesystem.JavaBean.SimpleCompetitonData;
import com.wzdx.competionmanagesystem.R;
import com.wzdx.competionmanagesystem.Utils.BaseViewHolder;
import com.wzdx.competionmanagesystem.Utils.MyHttpRequest;
import com.wzdx.competionmanagesystem.Utils.MyUtilss;
import com.wzdx.competionmanagesystem.Utils.TispToastFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sjk on 2016/11/16.
 */

public class CompetitionActivity extends Activity {


    private ImageButton ib_back;
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
        ((TextView) findViewById(R.id.tv_title)).setText("所有竞赛");
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompetitionActivity.this.finish();
            }
        });
        srlContent = (android.support.v4.widget.SwipeRefreshLayout) findViewById(R.id.srlContent);
        lv_content = (ListView) findViewById(R.id.lv_content);
        //点击竞赛列表，显示具体竞赛的详细信息
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SimpleCompetitonData item = (SimpleCompetitonData)
                        ((CompetitionAdapter) parent.getAdapter()).getItem(position);
                String url = "competitionController/getCompetitionDataDetailById";
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", item.getCompID() + "");
                new MyHttpRequest(CompetitionActivity.this, url, params) {
                    @Override
                    public void sucessListener(String result) {
                        Gson gson = new Gson();
                        final CompetitionDetail data = gson.fromJson(result, CompetitionDetail.class);
                        View contentView =
                                View.inflate(CompetitionActivity.this, R.layout.view_detail_competition_data, null);
                        final AlertDialog dialog = new AlertDialog.Builder(CompetitionActivity.this).create();

                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                        params.height = MyUtilss.getScreenHeight(CompetitionActivity.this) / 3 * 2;
                        dialog.getWindow().setAttributes(params);
                        dialog.setContentView(contentView);
                        Button mBt_apply = (Button) contentView.findViewById(R.id.bt_apply);
                        initDetailDialog(contentView, data);


                         //  报名时间与现有时间比较，判断是否可报名
                         long curTime = new Date().getTime()/1000;
                         if (curTime > data.getF_time() || curTime < data.getS_time() || data.getState() != 2) {
                         mBt_apply.setEnabled(false);//不可报名时，按钮不可点击
                         }

                        //报名按钮点击
                        mBt_apply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (UICommon.INSTANCE.getmIdentity() == UICommon.UNLOGIN) {
                                    toLoginActivity(data);
                                } else {
                                    toApplyActivity(data);
                                }
                                dialog.dismiss();
                            }
                        });


                    }
                }.startRequestByPost();
            }
        });
    }

    /**
     * 前往登录界面
     *
     * @param data
     */
    private void toLoginActivity(CompetitionDetail data) {
        String activityName = "";
        if (data.getComp_type() == 1) {
            activityName = SingleApplyActivity.class.getName();
        } else {
            activityName = GroupApplyActivity.class.getName();
        }

        Bundle targetBundle = new Bundle();
        targetBundle.putSerializable("data", data);

        Bundle bundle = new Bundle();
        bundle.putString("target", activityName);
        bundle.putBundle("bundle", targetBundle);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //前往报名界面
    private void toApplyActivity(CompetitionDetail data) {
        String activityName = "";
        if (data.getComp_type() == 1) {//个人赛报名界面
            activityName = SingleApplyActivity.class.getName();
        } else {//小组赛报名界面
            activityName = GroupApplyActivity.class.getName();
        }
        Bundle targetBundle = new Bundle();
        targetBundle.putSerializable("data", data);

        Intent intent = new Intent();
        intent.setClassName(this, activityName);
        intent.putExtras(targetBundle);
        startActivity(intent);
    }

    public void initDetailDialog(View contentView, CompetitionDetail data) {
        View ll_group = contentView.findViewById(R.id.ll_group);

        TextView mTvName = (TextView) contentView.findViewById(R.id.tvName);
        mTvName.setText(data.getComp_name());
        TextView mTvLevel = (TextView) contentView.findViewById(R.id.tvLevel);
        mTvLevel.setText(data.getR_name());
        TextView mTvMaxPeople = (TextView) contentView.findViewById(R.id.tvMaxPeople);
        mTvMaxPeople.setText(data.getMax_num() + "");
        TextView mTvType = (TextView) contentView.findViewById(R.id.tvType);
        mTvType.setText(data.getTypeName());

        TextView mTvGroupPeople = (TextView) contentView.findViewById(R.id.tvGroupPeople);
        mTvGroupPeople.setText(data.getGroup_num() + "");
        //如果是个人赛则隐藏小组人数这一列
        if (data.getComp_type() == 1) {
            ll_group.setVisibility(View.GONE);
        }

        TextView mTvStartTime = (TextView) contentView.findViewById(R.id.tvStartTime);
        mTvStartTime.setText(data.getComp_time());
        TextView mTvTime = (TextView) contentView.findViewById(R.id.tvTime);
        mTvTime.setText(MyUtilss.getTimeFromTimestamp(data.getS_time())
                + "--" + MyUtilss.getTimeFromTimestamp(data.getF_time()));
        TextView mTvGradeLimt = (TextView) contentView.findViewById(R.id.tvGradeLimt);
        mTvGradeLimt.setText(data.getGrade_limited());

        TextView mTvMarjorLimit = (TextView) contentView.findViewById(R.id.tvMarjorLimit);
        ArrayList<Major> majors = data.getLimitedMajor();
        String limitedMajors = "";
        if (majors != null && majors.size() > 0) {
            for (int i = 0; i < majors.size(); i++) {
                limitedMajors += (majors.get(i).getM_name() + " ");
            }
        } else {
            limitedMajors = "无";
        }
        mTvMarjorLimit.setText(limitedMajors);

        TextView mTvState = (TextView) contentView.findViewById(R.id.tvState);

        mTvState.setText(data.getStateName());
        TextView mTvDescribe = (TextView) contentView.findViewById(R.id.tvDescribe);
        mTvDescribe.setText(data.getExplained());
        TextView mTvMoney = (TextView) contentView.findViewById(R.id.tvMoney);
        mTvMoney.setText("本竞赛需缴纳报名费" + data.getPaymoney() + "元");
    }

    private void initData() {
        String url = "competitioncontroller/getCompetitionList";
        new MyHttpRequest(CompetitionActivity.this, url, null) {
            @Override
            public void sucessListener(String result) {
                Gson gson = new Gson();
                CompetitonList list = gson.fromJson(result, CompetitonList.class);
                if (list.getSuccess()) {
                    lv_content.setAdapter(new CompetitionAdapter(list));
                } else {
                    TispToastFactory.getToast(CompetitionActivity.this, list.getMsg(), Toast.LENGTH_LONG).show();
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
                convertView = View.inflate(CompetitionActivity.this, R.layout.view_item_simple_competition, null);
            }
            TextView tv_comp_name = BaseViewHolder.get(convertView, R.id.tv_comp_name);
            TextView tv_type = BaseViewHolder.get(convertView, R.id.tv_type);
            TextView tv_start_time = BaseViewHolder.get(convertView, R.id.tv_start_time);
            TextView idtv_apply_time_start = BaseViewHolder.get(convertView, R.id.tv_apply_time_start);
            TextView tv_apply_time_end = BaseViewHolder.get(convertView, R.id.tv_apply_time_end);
            tv_comp_name.setText(item.getComp_name());
            tv_type.setText(item.getTypeName());
            tv_start_time.setText(item.getComp_time());
            idtv_apply_time_start.setText(MyUtilss.getTimeFromTimestamp(item.getS_time()));
            tv_apply_time_end.setText(MyUtilss.getTimeFromTimestamp(item.getF_time()));
            return convertView;
        }
    }


}
