package com.example.cjw.newsproject.fragment;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.example.cjw.newsproject.R;
import com.example.cjw.newsproject.activity.MainActivity;
import com.example.cjw.newsproject.adapter.ContentFragmentAdapter;
import com.example.cjw.newsproject.base.BaseFragment;
import com.example.cjw.newsproject.base.BasePager;
import com.example.cjw.newsproject.pager.HomePager;
import com.example.cjw.newsproject.pager.NewsPager;
import com.example.cjw.newsproject.pager.PoliticiansPager;
import com.example.cjw.newsproject.pager.SettingPager;
import com.example.cjw.newsproject.pager.WisdomPager;
import com.example.cjw.newsproject.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.orhanobut.logger.Logger;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by cjw on 2017/9/18.
 */

public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;

    @ViewInject(R.id.viewpager)
    private NoScrollViewPager viewpager;

    //五个页面
    private ArrayList<BasePager> basePagers;

    @Override
    public View initView() {
        Logger.d("正文视图初始化了");
        View view = View.inflate(context, R.layout.content_fragment,null);

        //把视图关联到框架中
        x.view().inject(ContentFragment.this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Logger.d("正文数据初始化了");

        basePagers = new ArrayList<>();
        basePagers.add(new HomePager(context));
        basePagers.add(new NewsPager(context));
        basePagers.add(new WisdomPager(context));
        basePagers.add(new PoliticiansPager(context));
        basePagers.add(new SettingPager(context));


        viewpager.setAdapter(new ContentFragmentAdapter(basePagers));

        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        //初始化
        rg_main.check(R.id.rb_home);
        basePagers.get(0).initData();
        isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
    }

    //得到这个新闻页面
    public NewsPager getNewsPager() {
        return (NewsPager) basePagers.get(1);
    }

    //下面按钮选择监听
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId){
                case R.id.rb_home:
                    viewpager.setCurrentItem(0);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_news:
                    viewpager.setCurrentItem(1);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                case R.id.rb_wisdom:
                    viewpager.setCurrentItem(2);
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_politicians:
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    viewpager.setCurrentItem(3);
                    break;
                case R.id.rb_setting:
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    viewpager.setCurrentItem(4);
                    break;
                default:

                    break;
            }
        }
    }

    private void isEnableSlidingMenu(int touchmodeFullscreen) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }

    //页面切换监听
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            basePagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
