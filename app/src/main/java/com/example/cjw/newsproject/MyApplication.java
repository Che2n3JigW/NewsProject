package com.example.cjw.newsproject;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import org.xutils.x;


/**
 * Created by Administrator on 2017/9/18.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//                .tag("===cjw")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
//                .build();
        Logger.addLogAdapter(new AndroidLogAdapter());
        x.Ext.init(this);
//        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
