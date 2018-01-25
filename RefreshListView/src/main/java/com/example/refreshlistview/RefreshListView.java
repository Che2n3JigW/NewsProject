package com.example.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;



import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * Created by Administrator on 2017/10/5.
 */

public class RefreshListView extends ListView {


    private LinearLayout headerView;

    private View ll_pull_down_refresh;
    private ImageView iv_arrow;
    private ProgressBar pb_status;
    private TextView tv_status;
    private TextView tv_time;

    private int measuredHeight;
    //下拉刷新
    public static final int PULL_DOWN_REFRESH = 0;
    //手松刷新
    public static final int RELEASE_DOWN_REFRESH = 1;
    //正在刷新
    public static final int REFRESHING = 2;

    private int currentStatus = PULL_DOWN_REFRESH;

    //底部下拉刷新
    private View footerView;
    //底部控件高
    private int footerViewMeasuredHeight;
    //是否已经加载更多
    private boolean isLoadMore = false;
    //顶部轮播图
    private View topnews;
    //listView在Y轴的坐标
    private int listViewOnScreenY = -1;


    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
        initAnimation();
        initFooterView(context);
    }

    private void initFooterView(Context context) {
        footerView = View.inflate(context, R.layout.refresh_footer, null);
        footerView.measure(0, 0);
        footerViewMeasuredHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewMeasuredHeight, 0, 0);
        addFooterView(footerView);
        setOnScrollListener(new MyOnScrollListener());
    }

    public void addTopNewsView(View topnews) {
        if (topnews != null) {
            this.topnews = topnews;
            headerView.addView(topnews);
        }

    }

    class MyOnScrollListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
            if (i == OnScrollListener.SCROLL_STATE_IDLE || i == OnScrollListener.SCROLL_STATE_FLING) {
                //当静止或者惯性滚动的时候 最后一张可见
                if (getLastVisiblePosition() >= getCount() - 1) {
                    //显示加载更多的布局
                    footerView.setPadding(8, 8, 8, 8);
                    //状态改变
                    isLoadMore = true;
                    //回调接口
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onLoadMore();
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i1, int i2) {

        }
    }


    private Animation upAnimation;
    private Animation downAnimation;

    private void initAnimation() {
        upAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    private void initHeaderView(Context context) {
        headerView = (LinearLayout) View.inflate(context, R.layout.refresh_header, null);

        ll_pull_down_refresh = headerView.findViewById(R.id.ll_pull_down_refresh);
        iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        pb_status = (ProgressBar) headerView.findViewById(R.id.pb_status);
        tv_status = (TextView) headerView.findViewById(R.id.tv_status);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);

        //测量
        ll_pull_down_refresh.measure(0, 0);
        measuredHeight = ll_pull_down_refresh.getMeasuredHeight();
        ll_pull_down_refresh.setPadding(0, -measuredHeight, 0, 0);
        addHeaderView(headerView);
    }

    private float startY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    startY = ev.getY();
                }

                //判断顶部轮播图是否完全显示
                boolean isDisplayTopNews = isDisplayTopNews();
                if (!isDisplayTopNews){//没有完全显示
                    //加载更多
                    break;
                }

                //如果是正在刷新 就不让他刷新了
                 if (currentStatus == REFRESHING){
                     break;
                 }
                //新坐标
                float endY = ev.getY();
                //滑动距离
                float distanceY = endY - startY;
                if (distanceY > 0) {
                    //下拉
                    int paddingTop = (int) (-measuredHeight + distanceY);
                    if (paddingTop < 0 && currentStatus != PULL_DOWN_REFRESH) {
                        //下拉刷新状态
                        currentStatus = PULL_DOWN_REFRESH;
                        //更新状态
                        refreshViewStatus();
                    } else if (paddingTop > 0 && currentStatus != RELEASE_DOWN_REFRESH) {
                        //手动刷新状态
                        currentStatus = RELEASE_DOWN_REFRESH;
                        //更新状态
                        refreshViewStatus();
                    }
                    ll_pull_down_refresh.setPadding(0, paddingTop, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (currentStatus == PULL_DOWN_REFRESH) {
                    ll_pull_down_refresh.setPadding(0, -measuredHeight, 0, 0);
                } else if (currentStatus == RELEASE_DOWN_REFRESH) {
                    currentStatus = REFRESHING;
                    refreshViewStatus();
                    ll_pull_down_refresh.setPadding(0, 0, 0, 0);
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onPullDownRefresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private boolean isDisplayTopNews() {
        if (topnews != null){
            //得到listView在屏幕上的坐标
            int[] location = new int[2];
            if (listViewOnScreenY == -1){
                getLocationOnScreen(location);
                listViewOnScreenY = location[1];
            }
            //得到轮播图在屏幕上的位置
            topnews.getLocationOnScreen(location);
            int topNewsViewOnScreenY = location[1];
//        if (listViewOnScreenY <= topNewsViewOnScreenY){
//            return true;
//        }else{
//            return false;
//        }
            return listViewOnScreenY <= topNewsViewOnScreenY;
        }else {
            return true;
        }
    }

    private void refreshViewStatus() {
        switch (currentStatus) {
            case PULL_DOWN_REFRESH:
                iv_arrow.startAnimation(downAnimation);
                tv_status.setText("下拉刷新...");
                break;
            case RELEASE_DOWN_REFRESH:
                iv_arrow.startAnimation(upAnimation);
                tv_status.setText("手松刷新...");
                break;
            case REFRESHING:
                tv_status.setText("正在刷新...");
                pb_status.setVisibility(VISIBLE);
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(GONE);
                break;
        }
    }

    //footer隐藏
    public void onRefreshFinish(boolean b) {
        if (isLoadMore) {//加载更多
            isLoadMore = false;
            //隐藏加载更多布局
            footerView.setPadding(0, -footerViewMeasuredHeight, 0, 0);

        } else {//下拉刷新
            tv_status.setText("下拉刷新");
            currentStatus = PULL_DOWN_REFRESH;
            iv_arrow.clearAnimation();
            pb_status.setVisibility(GONE);
            iv_arrow.setVisibility(VISIBLE);
            ll_pull_down_refresh.setPadding(0, -measuredHeight, 0, 0);
            if (b) {
                //设置最新的更新时间
                tv_time.setText("上次更新时间:" + getSystemTime());
            }
        }

    }

    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    public interface OnRefreshListener {
        //当下拉刷新的时候回调这个方法
        public void onPullDownRefresh();

        public void onLoadMore();
    }

    private OnRefreshListener mOnRefreshListener;

    //设置监听刷新
    public void setOnRefreshListener(OnRefreshListener l) {
        this.mOnRefreshListener = l;
    }
}
