package com.wzdx.competionmanagesystem.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sjk on 2017/2/10.
 */

public class DownloadService extends Service {
    private Map<String, Thread> threadMap;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        threadMap = new HashMap<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        if (TextUtils.isEmpty(url)) {//检查url是否为空
            return super.onStartCommand(intent, flags, startId);
        }
        //判断是该文件是否已经在下载了
        if (threadMap.containsKey("url")) {
            if (threadMap.get(url).isAlive()) {
                return super.onStartCommand(intent, flags, startId);
            } else {
                threadMap.remove(url);
            }
        }
        DownloadThread thread = new DownloadThread(url);
        threadMap.put(url, thread);
        thread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    class DownloadThread extends Thread {
        private String mUrl;

        public DownloadThread(String url) {
            mUrl = url;
        }

        @Override
        public void run() {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                //文件路径
                String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/compms/download";
                File file = new File(dirPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String fileName = mUrl.substring(mUrl.lastIndexOf("\\") + 1);
                File target = new File(dirPath, fileName);
                //防止文件重名
                int index = 1;
                while (target.exists()) {
                    target = new File(dirPath, fileName + "(" + index + ")");
                    index++;
                }
                System.out.println("fileName------>"+dirPath+"   "+fileName);
                target.createNewFile();
                URL url = new URL(mUrl);
                URLConnection conn = url.openConnection();
                conn.connect();
                is = conn.getInputStream();
                long fileSize = conn.getContentLength();//根据响应获取文件大小
                if (fileSize <= 0) throw new RuntimeException("无法获知文件大小 ");
                if (is == null) throw new RuntimeException("stream is null");
                fos = new FileOutputStream(target);
                sendBroadcastWithProgress(0);
                long curFileSize = 0;
                byte[] b = new byte[1024];
                int length = -1;
                while ((length = is.read(b)) != -1) {
                    fos.write(b, 0, length);
                    curFileSize += length;
                    //发送广播
                    sendBroadcastWithProgress( (curFileSize*1.0 / fileSize)*100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                    //下载完毕从map中删除
                    threadMap.remove(mUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendBroadcastWithProgress(double process) {
            Intent intent = new Intent();
            intent.setAction("com.sjk.file.download");
            intent.putExtra("process", process);
            intent.putExtra("url", mUrl);
            sendBroadcast(intent);
        }
    }


}
