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

public class HomePager extends BasePager {

    public HomePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        Logger.d("主页面数据被初始化");
        tv_title.setText("主页");

        TextView textView = new TextView(context);
        textView.setText("主页内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(27);
        fl_content.addView(textView);
    }
}
