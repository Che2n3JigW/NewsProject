package com.example.cjw.newsproject.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.example.cjw.newsproject.R;
import com.example.cjw.newsproject.fragment.ContentFragment;
import com.example.cjw.newsproject.fragment.LeftmenuFragment;
import com.example.cjw.newsproject.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    public static final String MAIN_CONTENT_TAG = "main_content_tag";
    public static final String LEFT_MENU_TAG = "left_menu_tag";
    private Context context = MainActivity.this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化抽屉菜单
        initSlidingMenu();

        initFragment();
    }

    //初始化抽屉菜单
    private void initSlidingMenu() {
        //设置左侧菜单
        setBehindContentView(R.layout.activity_leftmenu);
        //设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
//        slidingMenu.setSecondaryMenu(R.layout.activity_rightmenu);

        //设置显示模式 左侧菜单加主页  左侧菜单加主页加右侧菜单 主页加右侧菜单
        slidingMenu.setMode(SlidingMenu.LEFT);

        //滑动模式 滑动边缘 全屏滑动 不可滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //设置主页占据的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(context,150));
    }

    private void initFragment() {
        //得到Fragment管理
        FragmentManager fragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //替换
        fragmentTransaction.replace(R.id.fl_main_content,new ContentFragment(), MAIN_CONTENT_TAG);//主页
        fragmentTransaction.replace(R.id.fl_leftmenu,new LeftmenuFragment(), LEFT_MENU_TAG);//主页
        //提交
        fragmentTransaction.commit();
    }

    //获取左侧菜单的Fragment
    public LeftmenuFragment getLeftmenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        return (LeftmenuFragment) fm.findFragmentByTag(LEFT_MENU_TAG);

    }


    //获取主内容的Fragment
    public ContentFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        return (ContentFragment) fm.findFragmentByTag(MAIN_CONTENT_TAG);
    }
}
