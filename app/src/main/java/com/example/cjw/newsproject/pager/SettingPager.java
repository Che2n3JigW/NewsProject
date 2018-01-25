package com.example.cjw.newsproject.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.cjw.newsproject.base.BasePager;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2017/9/19.
 */

public class SettingPager extends BasePager {

    public SettingPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        Logger.d("设置数据被初始化");

        tv_title.setText("设置");

        TextView textView = new TextView(context);
        textView.setText("设置内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(27);
        fl_content.addView(textView);
    }
}
