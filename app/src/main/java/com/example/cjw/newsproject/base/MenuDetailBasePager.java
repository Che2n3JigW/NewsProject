package com.example.cjw.newsproject.base;

import android.content.Context;
import android.view.View;

/**
 * Created by cjw on 2017/9/24.
 * 菜单详情基础页面
 */

public abstract class MenuDetailBasePager {

    public final Context context;

    public View rootView;

    public MenuDetailBasePager(Context context){
        this.context = context;
        rootView = initView();
    }

    public abstract View initView();

    //子类重写
    public void initData(){

    }
}
