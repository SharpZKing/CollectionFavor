package com.sharp.collectionfavor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.sharp.entity.Collection;
import com.sharp.util.ConstUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class DetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private WebView mWebView;

    String html;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConstUtil.CANCEL_COLLECTION:

                    String result = (String) msg.obj;
                    if ("OK".equals(result)){
                        finish();
                        Toast.makeText(DetailActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                    }else if ("NO".equals(result)){

                        Toast.makeText(DetailActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
    }

    private void initView() {

        mToolbar = (Toolbar) findViewById(R.id.tool_bar_detail);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolbar.inflateMenu(R.menu.item);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cancel_collection:

                        new AlertDialog.Builder(DetailActivity.this).setTitle("取消收藏").setMessage("确定要取消此收藏吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (!"".equals(getIntent().getStringExtra("objectId"))){

                                    Collection collection = new Collection();
                                    collection.setLiked(0);
                                    collection.update(getIntent().getStringExtra("objectId") , new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            Message message = new Message();
                                            message.what = ConstUtil.CANCEL_COLLECTION;
                                            if (e==null){
                                                message.obj = "OK";
                                            }else{
                                                message.obj = "NO";
                                            }
                                            handler.sendMessage(message);
                                        }
                                    });

                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();


                        break;
                }
                return false;
            }
        });

        mWebView = (WebView) findViewById(R.id.detail);
        final String url = getIntent().getStringExtra("url_detail");

        //支持javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        mWebView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (request.getUrl().toString().contains("sina.cn")){
                        view.loadUrl("http://ask.csdn.net/questions/178242");
                        return true;
                    }
                }*/
                view.loadUrl(url);

                return false;
            }


        });

        mWebView.loadUrl(url);
    }
}
