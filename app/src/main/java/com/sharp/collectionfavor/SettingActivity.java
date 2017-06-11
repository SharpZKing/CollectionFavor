package com.sharp.collectionfavor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.TooManyListenersException;

import cn.bmob.v3.BmobUser;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar mToolbar;

    private TextView mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_register);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLogout = (TextView) findViewById(R.id.setting_logout);
        mLogout.setOnClickListener(this);

        if (BmobUser.getCurrentUser() == null) {
            mLogout.setVisibility(View.GONE);
        }else{
            mLogout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_logout:
                BmobUser.logOut();
                Toast.makeText(SettingActivity.this, "退出账号成功!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
