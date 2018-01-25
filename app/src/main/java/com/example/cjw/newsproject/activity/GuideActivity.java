package com.example.cjw.newsproject.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.cjw.newsproject.R;
import com.example.cjw.newsproject.SplashActivity;
import com.example.cjw.newsproject.utils.CacheUtils;
import com.example.cjw.newsproject.utils.DensityUtil;

import java.util.ArrayList;

/**
 * 引导界面
 */
public class GuideActivity extends Activity {

    private Context context = GuideActivity.this;

    private ViewPager viewpager;
    private Button btn_start_main;
    private LinearLayout ll_point_group;
    private ImageView iv_red_point;

    private ArrayList<ImageView> imageViews;
    //两点之间的间距
    private int leftMax;
    private int widthdip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();

        initData();

        initListener();

    }



    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        btn_start_main = (Button) findViewById(R.id.btn_start_main);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
        iv_red_point = (ImageView) findViewById(R.id.iv_red_point);
    }

    private void initData() {
        //准备数据
        int[] ids = new int[]{
                R.drawable.guide1,
                R.drawable.guide2,
                R.drawable.guide3
        };
        widthdip = DensityUtil.dip2px(this,10);
        imageViews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            //设置背景
            imageView.setImageResource(ids[i]);
            imageViews.add(imageView);

            //创建点
            ImageView point = new ImageView(this);
            //添加到线性布局
            point.setImageResource(R.drawable.point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthdip, widthdip);
            if (i != 0) {
                params.leftMargin = 10;
            }
            point.setLayoutParams(params);
            ll_point_group.addView(point);
        }

        //设置viewpager的适配器
        viewpager.setAdapter(new MyViewpagerAdapter());
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());

        //得到屏幕滑动的百分比
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void initListener() {
        btn_start_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存曾经进入过主界面参数
                CacheUtils.putBoolean(context, SplashActivity.START_MAIN,true);
                //跳转到主界面
                startActivity(new Intent(context,MainActivity.class));
                //关闭当前类
                finish();
            }
        });
    }

    class MyViewpagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        /**
         * @param container viewpager
         * @param position  位置
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            return imageView;
        }


        /**
         * 当前创建的视图
         *
         * @param view
         * @param object
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
//            return view == imageViews.get(Integer.parseInt((String)object));
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView(imageViews.get(position));
        }
    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            //执行不止一次
            iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            //间距
            leftMax = ll_point_group.getChildAt(1).getLeft() - ll_point_group.getChildAt(0).getLeft();

        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        //页面滚动了
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //两点间移动距离
            int leftMargin = position * leftMax + (int) (leftMax * positionOffset);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
            params.leftMargin = leftMargin;
            iv_red_point.setLayoutParams(params);
        }

        //页面被选中的时候
        @Override
        public void onPageSelected(int position) {
            if (position == imageViews.size()-1){
                btn_start_main.setVisibility(View.VISIBLE);
            }else {
                btn_start_main.setVisibility(View.INVISIBLE);
            }
        }

        //滑动状态改变的时候
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
