package com.wzdx.competionmanagesystem.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 11032 on 2016/3/17.
 */
public class SPUtil {
    private static SharedPreferences sp = null;
    public static SharedPreferences getSP(Context context){
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp;
    };
    /**
     * @param context 上下文环境
     * @param key     表示关键字
     * @param value   表示需要保存的值
     */
    public static void putString(Context context, String key, String value) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }


    /**
     * @param context  上下文环境
     * @param key      关键字
     * @param defvalue 默认放回值
     * @return 放回一个String类型的字符串
     */
    public static String getString(Context context, String key, String defvalue) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        String value = sp.getString(key, defvalue);
        return value;
    }

    public static void deleteString(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key).commit();
    }

    /**
     * @param context 上下文环境
     * @param key     表示关键字
     * @param value   表示需要保存的值
     */
    public static void putBoolean(Context context, String key, Boolean value) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    /**
     * @param context  上下文环境
     * @param key      关键字
     * @param defvalue 默认放回值
     * @return 放回一个boolean类型的字符串
     */
    public static boolean getBoolean(Context context, String key, boolean defvalue) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        boolean value = sp.getBoolean(key, defvalue);
        return value;
    }

}
