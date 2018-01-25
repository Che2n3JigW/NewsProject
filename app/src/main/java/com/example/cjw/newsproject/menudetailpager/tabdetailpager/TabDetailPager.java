package com.example.cjw.newsproject.menudetailpager.tabdetailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cjw.newsproject.R;
import com.example.cjw.newsproject.activity.NewsDetailActivity;
import com.example.cjw.newsproject.base.MenuDetailBasePager;
import com.example.cjw.newsproject.bean.NewsCenterPagerBean;
import com.example.cjw.newsproject.bean.TabDetailPagerBean;
import com.example.cjw.newsproject.utils.CacheUtils;
import com.example.cjw.newsproject.utils.Constants;
import com.example.cjw.newsproject.view.HorizontalScrollViewPager;

import com.example.refreshlistview.RefreshListView;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class TabDetailPager extends MenuDetailBasePager {

    public static final String READ_ARRAY_ID = "read_array_id";

    private HorizontalScrollViewPager viewpager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private RefreshListView listview;

    private final NewsCenterPagerBean.DataBean.ChildrenBean childrenBean;
    //顶部新闻的数据(顶部轮播图片数据)
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;
    //新闻列表数据
    private List<TabDetailPagerBean.DataBean.NewsBean> news;

    private MyListAdapter myListAdapter;

    private ImageOptions imageOptions;

    //加载更多的链接
    private String moreUrl;
    private String url;
    //之前高亮过的位置
    private int prePosition;
    private boolean isLoadMore = false;

    public TabDetailPager(Context context, NewsCenterPagerBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;

        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(100), DensityUtil.dip2px(100))
                .setRadius(DensityUtil.dip2px(5))
                .setLoadingDrawableId(R.drawable.icon_default_pic)
                .setFailureDrawableId(R.drawable.icon_default_pic)
                .build();
    }


    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.tabdetail_pager, null);
        View topnews = View.inflate(context, R.layout.topnews, null);
        viewpager = (HorizontalScrollViewPager) topnews.findViewById(R.id.viewpager);
        tv_title = (TextView) topnews.findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) topnews.findViewById(R.id.ll_point_group);
        listview = (RefreshListView) view.findViewById(R.id.listview);

        listview.addTopNewsView(topnews);
        //下拉刷新
        listview.setOnRefreshListener(new MyOnRefreshListener());
        //设置listView的item监听
        listview.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            int realPosition = i - 1;
            TabDetailPagerBean.DataBean.NewsBean newsBean = news.get(realPosition);
            Toast.makeText(context, "newsData ===  id === " + newsBean.getId()
                    + "newsData=== title ==" + newsBean.getTitle(), Toast.LENGTH_SHORT).show();
            //取出id的集合
            String idArray = CacheUtils.getString(context, READ_ARRAY_ID);
            //判断是否存在
            if (!idArray.contains(newsBean.getId() + "")) {
                CacheUtils.putString(context, READ_ARRAY_ID, idArray + newsBean.getId() + ",");
                //刷新适配器
                myListAdapter.notifyDataSetChanged();
            }
            //跳转到新闻浏览页面
            Intent intent = new Intent(context,NewsDetailActivity.class);
            intent.putExtra("url" , Constants.BASE_URL + newsBean.getUrl());
            context.startActivity(intent);
        }
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + childrenBean.getUrl();
        //把之前缓存的数据取出来
        String saveJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        Logger.d(childrenBean.getTitle() + "的联网请求地址:" + url);

        getDataFromNet();
    }

    //分析数据
    private void processData(String json) {
        TabDetailPagerBean bean = parsedJson(json);

        String title = bean.getData().getNews().get(0).getTitle();
        Logger.d(childrenBean.getTitle() + "解析json数据 == " + title);

        topnews = bean.getData().getTopnews();

        moreUrl = "";
        if (TextUtils.isEmpty(bean.getData().getMore())) {
            moreUrl = "";
        } else {
            moreUrl = Constants.BASE_URL + bean.getData().getMore();
        }

        if (!isLoadMore) {//默认
            //设置适配器
            viewpager.setAdapter(new MyPagerAdapter());
            //向视图添加点
            addPoint();
            //监听ViewPager变化
            viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
            tv_title.setText(topnews.get(prePosition).getTitle());
            //准备ListView的数据集合
            news = bean.getData().getNews();
            myListAdapter = new MyListAdapter();
            //设置ListView的适配器
            listview.setAdapter(myListAdapter);
        } else {//加载更多
            isLoadMore = false;
            //添加到原来的集合中
            news.addAll(bean.getData().getNews());
            //刷新适配器
            myListAdapter.notifyDataSetChanged();
        }


    }

    private void addPoint() {
        //移除所有的红点
        ll_point_group.removeAllViews();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(8.0f), DensityUtil.dip2px(8.0f));

        for (int i = 0; i < topnews.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.point_selector);

            if (i == 0) {
                imageView.setEnabled(true);
            } else {
                imageView.setEnabled(false);
                params.leftMargin = DensityUtil.dip2px(8.0f);
            }

            imageView.setLayoutParams(params);
            ll_point_group.addView(imageView);
        }
    }

    class MyOnRefreshListener implements RefreshListView.OnRefreshListener {

        @Override
        public void onPullDownRefresh() {
            getDataFromNet();
        }

        @Override
        public void onLoadMore() {
//            Toast.makeText(context, "加载更多回调", Toast.LENGTH_SHORT).show();
            if (TextUtils.isEmpty(moreUrl)) {
                Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
                listview.onRefreshFinish(false);
            } else {
                getMoreDataFromNet();
            }

        }
    }

    private void getMoreDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);
        params.setConnectTimeout(6000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.d("加载更多的返回数据 : " + result);
                listview.onRefreshFinish(false);
                isLoadMore = true;
                processData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.d("加载更多失败信息 : " + ex.getMessage());
                listview.onRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Logger.d("加载更多取消信息 : " + cex.getMessage());
            }

            @Override
            public void onFinished() {
                Logger.d("加载更多完成");
            }
        });
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //设置文本
            tv_title.setText(topnews.get(position).getTitle());
            //红点高亮 把之前设置成灰色 选择的红色
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            ll_point_group.getChildAt(position).setEnabled(true);
            prePosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);

            TabDetailPagerBean.DataBean.TopnewsBean topnewsBean = topnews.get(position);
            String topimageUrl = Constants.BASE_URL + topnewsBean.getTopimage();
            //联网请求图片
            x.image().bind(imageView, topimageUrl);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = View.inflate(context, R.layout.item_tabdetail_pager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
                viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            //根据位置获取数据
            TabDetailPagerBean.DataBean.NewsBean newsBean = news.get(i);
            String imageUrl = Constants.BASE_URL + newsBean.getListimage();
            //请求图片
            x.image().bind(viewHolder.iv_icon, imageUrl, imageOptions);

            //设置标题
            viewHolder.tv_title.setText(newsBean.getTitle());
            //设置时间
            viewHolder.tv_time.setText(newsBean.getPubdate());

            String idArray = CacheUtils.getString(context,READ_ARRAY_ID);
            if (idArray.contains(newsBean.getId() + "")){
                //设置灰色
                viewHolder.tv_title.setTextColor(Color.GRAY);
            }else {
                //设置黑色
                viewHolder.tv_title.setTextColor(Color.BLACK);
            }
            return view;
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
    }

    //分析数据
    private TabDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json, TabDetailPagerBean.class);
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(6000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.d(childrenBean.getTitle() + result);
                //缓存数据
                CacheUtils.putString(context, url, result);
                //解析和处理显示数据
                processData(result);

                //隐藏下拉刷新控件 重新显示数据 更新时间
                listview.onRefreshFinish(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.d(childrenBean.getTitle() + ex.getMessage());
                listview.onRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Logger.d(childrenBean.getTitle() + cex.getMessage());
            }

            @Override
            public void onFinished() {
                Logger.d("请求完成");
            }
        });
    }
}
