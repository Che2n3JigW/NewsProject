package com.example.cjw.newsproject.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cjw.newsproject.R;

public class NewsDetailActivity extends Activity {

    //控件
    private TextView tv_title;
    private ImageButton ib_menu;
    private ImageButton ib_back;
    private ImageButton ib_share;
    private ImageButton ib_text;
    private WebView web_view;
    private ProgressBar pb_loading;
    private String url;
    private WebSettings settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        initView();

        initData();
        
        initListener();
    }

    private void initListener() {
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ib_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NewsDetailActivity.this, "设置文字大小", Toast.LENGTH_SHORT).show();
                //显示改变文字大小的对话框
                showChangeTextSizeDialog();
            }
        });

        ib_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NewsDetailActivity.this, "分享", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int tempSize = 2;
    private int realSize = tempSize;
    private void showChangeTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = new String[]{
                "超大字体","大字体","正常字体","小字体","超小字体"
        };
        builder.setTitle("设置文字大小").setSingleChoiceItems(items, 2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tempSize = i;
            }
        });

        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                realSize = tempSize;
                //改变文字大小
                changeTextSize(realSize);
            }
        });
        builder.show();
    }

    private void changeTextSize(int realSize) {
        switch (realSize){
            case 0://超大
                settings.setTextZoom(200);
                break;
            case 1://大
                settings.setTextZoom(150);
                break;
            case 2://
                settings.setTextZoom(100);
                break;
            case 3://小
                settings.setTextZoom(75);
                break;
            case 4://超小
                settings.setTextZoom(50);
                break;

        }
    }

    private void initData() {
        tv_title.setVisibility(View.GONE);
        ib_menu.setVisibility(View.GONE);
        ib_back.setVisibility(View.VISIBLE);
        ib_share.setVisibility(View.VISIBLE);
        ib_text.setVisibility(View.VISIBLE);

        //获取Activity传过来的数据
        url = getIntent().getStringExtra("url");
        settings = web_view.getSettings();
        settings.setJavaScriptEnabled(true);
        //设置变大变小
        settings.setUseWideViewPort(true);
        //缩放按钮
        settings.setBuiltInZoomControls(true);
        web_view.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb_loading.setVisibility(View.GONE);
            }
        });
        web_view.loadUrl(url);

    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        ib_menu = (ImageButton) findViewById(R.id.ib_menu);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_share = (ImageButton) findViewById(R.id.ib_share);
        ib_text = (ImageButton) findViewById(R.id.ib_text);
        web_view = (WebView) findViewById(R.id.web_view);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);



    }
}
