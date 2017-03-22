package com.wzdx.competionmanagesystem.Utils;

import android.content.Context;
import android.widget.Toast;

import com.wzdx.competionmanagesystem.App.MyApplication;

/**
 * 不会连续出现的Toast
 * Created by 11032 on 2016/5/24.
 */
public class TispToastFactory {
    private static Toast toast = null;

    public static Toast getToast(Context context, String hint,int duration){
        if(toast == null){
            toast = Toast.makeText(context,hint,duration);
        }else{
            toast.setText(hint);
            toast.setDuration(duration);
        }
        return toast;
    }

    public static void showTip(String hint){
        getToast(MyApplication.getInstance().getApplicationContext(),hint,Toast.LENGTH_LONG).show();
    }
}
