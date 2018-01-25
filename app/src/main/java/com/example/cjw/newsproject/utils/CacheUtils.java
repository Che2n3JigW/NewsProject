package com.example.cjw.newsproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by cjw on 2017/9/17.
 * 缓存软件的一些参数和数据
 */

public class CacheUtils {

    //得到缓存值
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("cjw",Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }

    //保存软件的参数
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("cjw",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }


    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("cjw",Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    //获取缓存文本信息
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("cjw",Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }
}
