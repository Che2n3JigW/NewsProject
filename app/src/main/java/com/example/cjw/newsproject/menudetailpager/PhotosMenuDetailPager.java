package com.example.cjw.newsproject.menudetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.cjw.newsproject.base.MenuDetailBasePager;
import com.orhanobut.logger.Logger;

/**
 * Created by cjw on 2017/9/24.
 * 图组详细页面
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {

    private TextView textView;

    public PhotosMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        Logger.d("图组详细页面视图被初始化");
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(27);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        Logger.d("图组详细页面数据被初始化");
        textView.setText("图组详细页面内容");
    }
}
