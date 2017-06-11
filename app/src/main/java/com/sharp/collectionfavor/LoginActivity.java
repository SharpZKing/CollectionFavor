package com.sharp.collectionfavor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sharp.entity.User;
import com.sharp.util.MD5Util;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar mToolbar;

    private EditText mUsername;
    private EditText mPassword;
    private TextView mGoRegister;
    private TextView mLoginBtn;
    private TextView mForgetPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mUsername = (EditText) findViewById(R.id.login_username);
        mPassword = (EditText) findViewById(R.id.login_password);
        mGoRegister = (TextView) findViewById(R.id.login_go_register);
        mGoRegister.setOnClickListener(this);
        mLoginBtn = (TextView) findViewById(R.id.login_login);
        mLoginBtn.setOnClickListener(this);
        mForgetPwd = (TextView) findViewById(R.id.login_forget_pwd);
        mForgetPwd.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_login:
                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(username) & !TextUtils.isEmpty(password)){
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(MD5Util.MD5EncodeUtf8(password));
                    user.login(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e == null){
                                Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                                mLoginBtn.setClickable(false);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "登入失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    if (TextUtils.isEmpty(username)){
                        Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    }
                    if (TextUtils.isEmpty(password)){
                        Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                }


                break;
            case R.id.login_go_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_forget_pwd:
                Toast.makeText(this, "该功能等待后续开放.....", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
