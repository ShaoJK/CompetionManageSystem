package com.wzdx.competionmanagesystem.Activity;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wzdx.competionmanagesystem.Activity.Student.GroupApplyActivity;
import com.wzdx.competionmanagesystem.Activity.Student.SingleApplyActivity;
import com.wzdx.competionmanagesystem.Activity.Teacher.SingleApplyTeacherActivity;
import com.wzdx.competionmanagesystem.App.UICommon;
import com.wzdx.competionmanagesystem.JavaBean.CompetitionDetail;
import com.wzdx.competionmanagesystem.JavaBean.CompetitonList;
import com.wzdx.competionmanagesystem.JavaBean.Contestant;
import com.wzdx.competionmanagesystem.JavaBean.ContestantList;
import com.wzdx.competionmanagesystem.JavaBean.Major;
import com.wzdx.competionmanagesystem.JavaBean.SimpleCompetitonData;
import com.wzdx.competionmanagesystem.JavaBean.Student;
import com.wzdx.competionmanagesystem.JavaBean.Teacher;
import com.wzdx.competionmanagesystem.R;
import com.wzdx.competionmanagesystem.Reciver.UpdateProcessReciver;
import com.wzdx.competionmanagesystem.Service.DownloadService;
import com.wzdx.competionmanagesystem.Utils.BaseViewHolder;
import com.wzdx.competionmanagesystem.Utils.MyHttpRequest;
import com.wzdx.competionmanagesystem.Utils.MyUtilss;
import com.wzdx.competionmanagesystem.Utils.TispToastFactory;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sjk on 2016/11/16.
 */

public class ContestantsActivity extends Activity implements UpdateProcessReciver.DownloadListener {
    private ListView lv_content;
    private String mCurDownloadUrl;
    private ProgressBar mProgressBar;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contestants);
        initView();
        initData();
    }

    private void initData() {
        String url = "";
        Map<String, String> params = new HashMap<>();
        if (UICommon.INSTANCE.getmIdentity() == UICommon.STUDENT) {
            url = "competitionController/getAwardList";
            Student stu = (Student) UICommon.INSTANCE.getUser();
            params.put("stuID", stu.getID() + "");
        } else {
            Teacher teach = (Teacher) UICommon.INSTANCE.getUser();
            url = "competitionController/getAwardListByTeacher";
            params.put("teacID", teach.getID() + "");
        }

        new MyHttpRequest(this, url, params) {
            @Override
            public void sucessListener(String result) {
                ContestantList data = new Gson().fromJson(result, ContestantList.class);
                if (data.getSuccess()) {
                    lv_content.setAdapter(new ContestantsAdapter(ContestantsActivity.this, data));
                } else {
                    if (data.getMsg().equals("未登录")) {
                        UICommon.INSTANCE.toLogin(ContestantsActivity.this);
                    } else {
                        TispToastFactory.showTip(data.getMsg());
                    }
                }
            }
        }.startRequestByPost();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("所获奖项");
        ImageButton ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContestantsActivity.this.finish();
            }
        });

        lv_content = (ListView) findViewById(R.id.lv_content);
    }

    private void downloadFile(String url) {
        mCurDownloadUrl = url;
        //注册广播接受下载进度
        IntentFilter intentFilter = new IntentFilter("com.sjk.file.download");
        final UpdateProcessReciver reciver = new UpdateProcessReciver();
        reciver.setmDownloadListener(this);
        registerReceiver(reciver, intentFilter);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "compms/download";
        View dialogView = View.inflate(this, R.layout.dialog_download_process, null);
        TextView tv_hint = (TextView) dialogView.findViewById(R.id.tv_hint);
        tv_hint.setText("该文件将会被下载到" + path + "文件夹下");
        mProgressBar = (ProgressBar) dialogView.findViewById(R.id.progressbar);
        mDialog = new AlertDialog.Builder(this).setView(dialogView).create();
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                unregisterReceiver(reciver);
            }
        });
        mDialog.show();

        //打开下载服务器
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("url", url);
        startService(intent);


    }

    @Override
    public void updateListener(String url, double process) {
        if (mProgressBar != null && mCurDownloadUrl.equals(url)) {
            int processInt = (int) process;
            mProgressBar.setProgress((int) process);
            if ((int) process >= 100 && mDialog != null) {
                mDialog.dismiss();
            }
        }
    }

    class ContestantsAdapter extends BaseAdapter {
        private ContestantList mListInfo;
        private Context mContext;

        public ContestantsAdapter(Context context, ContestantList list) {
            mContext = context;
            mListInfo = list;
        }

        public ContestantList getmListInfo() {
            return mListInfo;
        }

        public void setmListInfo(ContestantList mListInfo) {
            this.mListInfo = mListInfo;
        }

        public Context getmContext() {
            return mContext;
        }

        public void setmContext(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListInfo.getData().size();
        }

        @Override
        public Object getItem(int position) {
            return mListInfo.getData().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.view_item_contestants, null);
            }
            final Contestant item = mListInfo.getData().get(position);
            TextView tv_comp_name = BaseViewHolder.get(convertView, R.id.tv_comp_name);
            tv_comp_name.setText(item.getCompName());
            TextView tv_type = BaseViewHolder.get(convertView, R.id.tv_type);
            if (item.getCompType() == 1) {
                tv_type.setText("个人赛");
            } else {
                tv_type.setText("团体赛");
            }
            TextView tv_award_name = BaseViewHolder.get(convertView, R.id.tv_award_name);
            tv_award_name.setText(item.getReward_name());
            TextView tv_grade = BaseViewHolder.get(convertView, R.id.tv_grade);
            //学生业绩分
            if (UICommon.INSTANCE.getmIdentity() == UICommon.STUDENT) {
                tv_grade.setText(item.getS_perpoint() + "");
            } else {  //老师业绩分
                tv_grade.setText(item.getTeachPoint() + "");
            }

            TextView tv_name = BaseViewHolder.get(convertView, R.id.tv_name);
            if (item.getCompType() == 1) {
                tv_name.setText(item.getStuName());
            } else {
                tv_name.setText(item.getStuName() + "的小队");
            }
            Button btn_download_file = BaseViewHolder.get(convertView, R.id.btn_download_file);
            //下载获奖文件
            btn_download_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            if (TextUtils.isEmpty(item.getReward_file())) {//没有下载地址的时候
                btn_download_file.setVisibility(View.INVISIBLE);
                btn_download_file.setEnabled(false);
            }else{
                btn_download_file.setVisibility(View.VISIBLE);
                btn_download_file.setEnabled(true);
            }
            btn_download_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadFile(item.getReward_file());
                }
            });

            //下载获奖证书图片
            Button btn_download_certificate = BaseViewHolder.get(convertView, R.id.btn_download_certificate);
            if (TextUtils.isEmpty(item.getCertificate())) {//没有下载地址的时候
                btn_download_certificate.setVisibility(View.INVISIBLE);
                btn_download_certificate.setEnabled(false);
            }else{
                btn_download_certificate.setVisibility(View.VISIBLE);
                btn_download_certificate.setEnabled(true);
            }
            btn_download_certificate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadFile(item.getCertificate());
                }
            });
            return convertView;
        }
    }
}
