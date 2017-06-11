package com.sharp.collectionfavor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sharp.util.SharedUtil;
import com.sharp.util.ToolUtil;

public class SplashActivity extends AppCompatActivity {

    private static final int HANDLER_SPLASH = 2000;

    private TextView mSplashTv;
    private TextView mSplashTv2;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what){
                case HANDLER_SPLASH:
                    if (isFirst()){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mSplashTv = (TextView) findViewById(R.id.splash_tv);
        ToolUtil.setFontType(this, mSplashTv);
        mSplashTv2 = (TextView) findViewById(R.id.splash_tv2);
        ToolUtil.setFontType(this, mSplashTv2);

        handler.sendEmptyMessageDelayed(HANDLER_SPLASH, 3000);

    }

    public boolean isFirst(){
        boolean isFirst = SharedUtil.getBoolean(this, "ISFIRST", true);
        if (isFirst){
            SharedUtil.putBoolean(this, "ISFIRST", false);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
