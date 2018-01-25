package com.example.cjw.newsproject.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cjw.newsproject.R;
import com.example.cjw.newsproject.activity.MainActivity;

/**
 * Created by Administrator on 2017/9/19.
 */

public class BasePager {

    public final Context context;//mainActivity

    public View rootView;//视图 代表各个页面
    public TextView tv_title;
    public ImageButton ib_menu;
    public FrameLayout fl_content;

    public BasePager(Context context){
        this.context = context;
        rootView = initView();
    }

    private View initView() {
        View view = View.inflate(context, R.layout.base_pager,null);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        ib_menu = (ImageButton) view.findViewById(R.id.ib_menu);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把左侧菜单关闭
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();
            }
        });
        return view;
    }

    public void initData(){

    }
}
