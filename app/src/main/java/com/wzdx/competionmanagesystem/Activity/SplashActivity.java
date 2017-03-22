package com.wzdx.competionmanagesystem.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;

import com.wzdx.competionmanagesystem.R;

public class SplashActivity extends Activity {
    private final static int ENTER_MAIN = 0x01; //进入主界面的标记
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ENTER_MAIN: //跳转到主界面
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //初始化动画
        initAnimation();
        //延迟3秒进入主界面
        mHandler.sendEmptyMessageDelayed(ENTER_MAIN, 3000);
    }

    private void initAnimation() {
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(2000);//动画执行两秒
        findViewById(R.id.rl_root).setAnimation(alpha);
    }
}
