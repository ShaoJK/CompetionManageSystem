package com.wzdx.competionmanagesystem.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wzdx.competionmanagesystem.App.UICommon;
import com.wzdx.competionmanagesystem.JavaBean.Announcement;
import com.wzdx.competionmanagesystem.JavaBean.AnnouncementList;
import com.wzdx.competionmanagesystem.JavaBean.Student;
import com.wzdx.competionmanagesystem.JavaBean.Teacher;
import com.wzdx.competionmanagesystem.R;
import com.wzdx.competionmanagesystem.Reciver.UpdateProcessReciver;
import com.wzdx.competionmanagesystem.Service.DownloadService;
import com.wzdx.competionmanagesystem.Utils.BaseViewHolder;
import com.wzdx.competionmanagesystem.Utils.MyHttpRequest;
import com.wzdx.competionmanagesystem.Utils.MyUtilss;
import com.wzdx.competionmanagesystem.Utils.TispToastFactory;

import java.util.ArrayList;

/**
 * Created by sjk on 2016/11/16.
 */

public class MainActivity extends Activity implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, UpdateProcessReciver.DownloadListener {

    private DrawerLayout mDrawerLayout;
    private FrameLayout fl_main_content;
    private FrameLayout fl_left_content;
    private View mMainContent;
    private View mLeftContent;
    private TextView tv_user_name,tv_class;
    private ListView lv_announcement;
    private TextView tv_user_center, tvAllCompetiton, tv_my_competition;
    private SwipeRefreshLayout sl_content;
    private String mCurDownloadUrl;
    private ProgressBar mProgressBar;
    private AlertDialog mDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 001:
                    sl_content.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (UICommon.INSTANCE.getmIdentity()) {
            case UICommon.UNLOGIN:
                tv_user_name.setText("游客");
                tv_class.setText("点击头像登录");
                break;
            case UICommon.STUDENT:
                Student stu = (Student) UICommon.INSTANCE.getUser();
                tv_user_name.setText(stu.getS_name());
                tv_class.setText(stu.getD_name()+" "+stu.getC_name());
                break;
            case UICommon.TEACHER:
                Teacher tea = (Teacher) UICommon.INSTANCE.getUser();
                tv_user_name.setText(tea.getT_name());
                tv_class.setText(tea.getA_name()+" "+tea.getD_name());
                break;
        }

    }

    private void initData() {
        String url = "othercontroller/getAnnouncementList";
        //加载公告信息
        MyHttpRequest request = new MyHttpRequest(this, url, null) {
            @Override
            public void sucessListener(String result) {
                AnnouncementList data = new Gson().fromJson(result, AnnouncementList.class);
                if (data.getSuccess()) {
                    lv_announcement.setAdapter(new AnnouncementAdapter(MainActivity.this, data.getList()));
                } else {
                    TispToastFactory.showTip(data.getMsg());
                }
            }
        };
        request.startRequestByPost();
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        fl_main_content = (FrameLayout) findViewById(R.id.fl_main_content);
        fl_left_content = (FrameLayout) findViewById(R.id.fl_left_content);

        mLeftContent = View.inflate(this, R.layout.view_left_content, fl_left_content);
        mMainContent = View.inflate(this, R.layout.view_main_content, fl_main_content);

        lv_announcement = (ListView) mMainContent.findViewById(R.id.lv_announcement);
        mMainContent.findViewById(R.id.img_open_left).setOnClickListener(this);//设置打开侧边栏事件监听

        tv_user_center = (TextView) mLeftContent.findViewById(R.id.tv_user_center);
        tv_user_center.setOnClickListener(this);//前往个人中心
        tvAllCompetiton = (TextView) mLeftContent.findViewById(R.id.tvAllCompetiton);
        tvAllCompetiton.setOnClickListener(this);
        mLeftContent.findViewById(R.id.img_head).setOnClickListener(this);
        mLeftContent.findViewById(R.id.tv_user_name).setOnClickListener(this);
        mLeftContent.findViewById(R.id.tv_look_announcement).setOnClickListener(this);
        mLeftContent.findViewById(R.id.tv_class).setOnClickListener(this);

        sl_content = (SwipeRefreshLayout) mMainContent.findViewById(R.id.sl_content);
        sl_content.setOnRefreshListener(this);
        sl_content.setColorSchemeResources(android.R.color.holo_blue_light);

        tv_my_competition = (TextView) findViewById(R.id.tv_my_competition);
        tv_my_competition.setOnClickListener(this);
        //设置左滑边框的宽度为屏幕的5/4
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) fl_left_content.getLayoutParams();
        params.width = MyUtilss.getScreenWidth(this) / 5 * 4;
        fl_left_content.setLayoutParams(params);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_class = (TextView) findViewById(R.id.tv_class);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_user_center://个人中心
            case R.id.img_head:
            case R.id.tv_user_name:
            case R.id.tv_class:
                if (UICommon.INSTANCE.getUser() == null) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("target", UserActivity.class.getName());
                    startActivity(intent);
                } else {
                    startActivity(new Intent(this, UserActivity.class));
                    break;
                }
            case R.id.img_open_left://打开侧边栏
                if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.tv_look_announcement://查看公告
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tvAllCompetiton://所有竞赛
                startActivity(new Intent(this, CompetitionActivity.class));
                break;
            case R.id.tv_my_competition://我的竞赛
                if (UICommon.INSTANCE.getUser() == null) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("target", MyCompetitionActivity.class.getName());
                    startActivity(intent);
                } else {
                    startActivity(new Intent(this, MyCompetitionActivity.class));
                    break;
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(001, 5000);
    }

    //根据URL下载文件
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

    class AnnouncementAdapter extends BaseAdapter {
        private ArrayList<Announcement> mAnnounceList = new ArrayList();
        private Context mContext;

        public AnnouncementAdapter(Context context, ArrayList announcements) {
            mAnnounceList = announcements;
            mContext = context;
        }

        @Override
        public int getCount() {
            return mAnnounceList.size();
        }

        @Override
        public Object getItem(int position) {
            return mAnnounceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.view_item_announcement_list, null);
            }
            final Announcement item = mAnnounceList.get(position);
            TextView tv_user_name = BaseViewHolder.get(convertView, R.id.tv_user_name);
            tv_user_name.setText(item.getSource());
            TextView tv_publish_time = BaseViewHolder.get(convertView, R.id.tv_publish_time);

            //将秒的时间戳改为毫秒
            long timestamp = item.getRelease_time() * 1000L;
            tv_publish_time.setText(MyUtilss.getTimeFromTimestamp(timestamp));

            TextView tv_content = BaseViewHolder.get(convertView, R.id.tv_info_content);
            tv_content.setText(item.getContent());

            TextView tv_download = BaseViewHolder.get(convertView, R.id.tv_download);
            if (TextUtils.isEmpty(item.getAdjunct())) {
                tv_download.setVisibility(View.GONE);
            } else {
                tv_download.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(item.getAdjunct())) {//附件地址为空
                tv_download.setVisibility(View.INVISIBLE);
                tv_download.setEnabled(false);
            } else {
                tv_download.setVisibility(View.VISIBLE);
                tv_download.setEnabled(true);
            }
            tv_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadFile(item.getAdjunct());
                }
            });

            return convertView;
        }
    }
}
