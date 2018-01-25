package com.example.cjw.newsproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cjw.newsproject.activity.GuideActivity;
import com.example.cjw.newsproject.activity.MainActivity;
import com.example.cjw.newsproject.utils.CacheUtils;

public class SplashActivity extends Activity {

    public static final String START_MAIN = "start_main";
    private RelativeLayout rl_splash_root;
    private Context context = SplashActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();

        //渐变动画,缩放动画,旋转动画
        AlphaAnimation aa = new AlphaAnimation(0,1);
//        aa.setDuration(2000);//持续时长
        aa.setFillAfter(true);//停留状态

        ScaleAnimation sa = new ScaleAnimation(0,1,0,1, ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
//        sa.setDuration(2000);//持续时长
        sa.setFillAfter(true);//停留状态

        AnimationSet set  = new AnimationSet(false);
        //没有先后
        set.addAnimation(aa);
        set.addAnimation(sa);
        set.setDuration(2000);
        set.setAnimationListener(new MyAnimationListener());
        //同时播放动画
        rl_splash_root.startAnimation(set);
    }

    private void initView() {
        rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
    }

    class MyAnimationListener implements Animation.AnimationListener{

        //动画开始播放的时候
        @Override
        public void onAnimationStart(Animation animation) {

        }

        //动画播放结束的时候
        @Override
        public void onAnimationEnd(Animation animation) {
            Toast.makeText(context, "动画播放结束", Toast.LENGTH_SHORT).show();

            //判断是否进入过主界面
            boolean isStartMain = CacheUtils.getBoolean(context, START_MAIN);
            if (isStartMain){
                //如果进入过主界面直接进入主界面
                startActivity(new Intent(context, MainActivity.class));
            }else {
                //如果没有进入过主界面 进入引导界面
                Intent intent = new Intent(context,GuideActivity.class);
                startActivity(intent);
            }
            //关闭
            finish();
        }

        //动画重复播放的时候
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
