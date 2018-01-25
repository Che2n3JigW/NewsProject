package com.example.cjw.newsproject.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cjw.newsproject.R;
import com.example.cjw.newsproject.activity.MainActivity;
import com.example.cjw.newsproject.base.BaseFragment;
import com.example.cjw.newsproject.bean.NewsCenterPagerBean;
import com.example.cjw.newsproject.pager.NewsPager;
import com.example.cjw.newsproject.utils.DensityUtil;
import com.orhanobut.logger.Logger;

import java.util.List;


/**
 * Created by Administrator on 2017/9/18.
 */

public class LeftmenuFragment extends BaseFragment {

    private List<NewsCenterPagerBean.DataBean> data;
    private ListView listView;
    private LeftMenuFragmentAdapter adapter;
    //点击的位置
    private int prePosition;

    @Override
    public View initView() {
        listView = new ListView(context);
        listView.setPadding(0, DensityUtil.dip2px(context,40),0,0);
        //设置分割线高度
        listView.setDividerHeight(0);
        listView.setCacheColorHint(Color.TRANSPARENT);

        //设置按下不变色
        listView.setSelection(android.R.color.transparent);

        //设置item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //记录点击的位置，变成红色
                prePosition = position;
                adapter.notifyDataSetChanged();
                //把左侧菜单关闭
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();
                //切换到相应的详细页面
                switchPager(prePosition);


            }
        });
        return listView;
    }

    //根据位置切换到不同页面
    private void switchPager(int position) {
        MainActivity mainActivity = (MainActivity) context;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        NewsPager newsPager = contentFragment.getNewsPager();
        newsPager.switchPager(position);
    }

    @Override
    public void initData() {
        super.initData();
        Logger.d("左侧菜单数据初始化了");
        Log.e("===cjw" , "左侧菜单数据初始化了");

    }

    //接收数据
    public void setData(List<NewsCenterPagerBean.DataBean> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            Logger.d(data.get(i).getTitle());
        }
        //设置适配器
        adapter = new LeftMenuFragmentAdapter();
        listView.setAdapter(adapter);
        //初始化数据
        switchPager(prePosition);
    }

    //左侧菜单listView适配器
    class LeftMenuFragmentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) View.inflate(context, R.layout.item_leftmenu,null);
            textView.setText(data.get(position).getTitle());
//            if (position==prePosition){
//                //设置红色
//                textView.setEnabled(true);
//            }else {
//                textView.setEnabled(false);
//            }
            textView.setEnabled(position==prePosition);
            return textView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


    }
}
