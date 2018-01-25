package com.example.cjw.newsproject.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.cjw.newsproject.activity.MainActivity;
import com.example.cjw.newsproject.base.BasePager;
import com.example.cjw.newsproject.base.MenuDetailBasePager;
import com.example.cjw.newsproject.bean.NewsCenterPagerBean;
import com.example.cjw.newsproject.fragment.LeftmenuFragment;
import com.example.cjw.newsproject.menudetailpager.InteracMenuDetailPager;
import com.example.cjw.newsproject.menudetailpager.NewsMenuDetailPager;
import com.example.cjw.newsproject.menudetailpager.PhotosMenuDetailPager;
import com.example.cjw.newsproject.menudetailpager.TopicMenuDetailPager;
import com.example.cjw.newsproject.utils.CacheUtils;
import com.example.cjw.newsproject.utils.Constants;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */

public class NewsPager extends BasePager {

    //左侧菜单
    private List<NewsCenterPagerBean.DataBean> data;
    private ArrayList<MenuDetailBasePager> detailBasePagers;

    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        Logger.d("新闻中心数据被初始化");
        tv_title.setText("新闻");
        TextView textView = new TextView(context);
        textView.setText("新闻页面");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(27);
        fl_content.addView(textView);
        ib_menu.setVisibility(View.VISIBLE);

        //获取缓存数据
        String  saveJson = CacheUtils.getString(context,Constants.NEWSCENTER_PAGER_URL);

        if (!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }
        //联网请求 使用xUtils3
        getDataFromNet();
    }

    private void getDataFromNet() {

        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.d("使用xUtils3联网成功" + result);
                //数据缓存
                CacheUtils.putString(context,Constants.NEWSCENTER_PAGER_URL,result);

                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.e("使用xUtils3联网失败" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Logger.d("使用xUtils3联网取消" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                Logger.d("使用xUtils3联网完成");
            }
        });
    }

    //解析json数据和现实数据
    private void processData(String json) {
        NewsCenterPagerBean bean = parsedJson(json);
        data = bean.getData();

        String title = data.get(0).getChildren().get(1).getTitle();
        Logger.d("使用Gson解析json数据" + title);

        MainActivity mainActivity = (MainActivity) context;
        //得到左侧菜单
        LeftmenuFragment leftmenuFragment = mainActivity.getLeftmenuFragment();
        //添加详情页面
        detailBasePagers = new ArrayList<>();
        detailBasePagers.add(new NewsMenuDetailPager(context,data.get(0)));//新闻
        detailBasePagers.add(new TopicMenuDetailPager(context,data.get(0)));//专题
        detailBasePagers.add(new PhotosMenuDetailPager(context));//图组
        detailBasePagers.add(new InteracMenuDetailPager(context));//互动
        leftmenuFragment.setData(data);

    }

    private NewsCenterPagerBean parsedJson(String json) {
        Gson gson = new Gson();
        NewsCenterPagerBean bean = gson.fromJson(json, NewsCenterPagerBean.class);
        return bean;
    }

    //根据位置切换到相应的页面
    public void switchPager(int position) {
        //设置标题
        tv_title.setText(data.get(position).getTitle());
        //移除之前的内容
        fl_content.removeAllViews();
        //添加新内容
        MenuDetailBasePager menuDetailBasePager = detailBasePagers.get(position);
        View rootView = menuDetailBasePager.rootView;
        menuDetailBasePager.initData();//初始化数据
        fl_content.addView(rootView);
    }
}
