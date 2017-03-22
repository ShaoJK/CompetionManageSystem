package com.wzdx.competionmanagesystem.Reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sjk on 2017/2/10.
 */

public class UpdateProcessReciver extends BroadcastReceiver {
    private DownloadListener mDownloadListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        String url = intent.getStringExtra("url");
        double process = intent.getDoubleExtra("process",0);
        System.out.println("---------------------------文件下载中"+process+"%—----------------------------");
        if(mDownloadListener!=null){
            mDownloadListener.updateListener(url,process);
        }
    }


    public void setmDownloadListener(DownloadListener mDownloadListener) {
        this.mDownloadListener = mDownloadListener;
    }

    public interface DownloadListener{
        public void updateListener(String url,double process);
    }
}
