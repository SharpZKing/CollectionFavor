package com.sharp.collectionfavor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sharp.entity.User;
import com.sharp.util.MD5Util;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity implements  View.OnClickListener{

    private Toolbar mToolbar;
    private EditText mUsername;
    private EditText mPwd;
    private EditText mRePwd;
    private EditText mAge;
    private EditText mEmail;
    private EditText mDesc;
    private RadioGroup mSexs;
    private CheckBox mAgree;
    private TextView mXyTv;
    private Button mRegister;

    String gender = "男";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        mUsername = (EditText) findViewById(R.id.et_username);
        mPwd = (EditText) findViewById(R.id.et_password);
        mRePwd = (EditText) findViewById(R.id.et_re_password);
        mAge = (EditText) findViewById(R.id.et_age);
        mEmail = (EditText) findViewById(R.id.et_email);
        mDesc = (EditText) findViewById(R.id.et_desc);
        mSexs = (RadioGroup) findViewById(R.id.sex);
        mAgree = (CheckBox) findViewById(R.id.cb_xy);
        mXyTv = (TextView) findViewById(R.id.look_xy);
        mXyTv.setOnClickListener(this);
        mRegister = (Button) findViewById(R.id.register);
        mRegister.setOnClickListener(this);

        mSexs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male){
                    gender ="男";
                }else if (checkedId == R.id.female){
                    gender = "女";
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.look_xy:
                Intent intent = new Intent(RegisterActivity.this, XYAgreeActivity.class);
                startActivity(intent);
                break;
            case R.id.register:
                checkValidate();
                break;
        }
    }

    private void checkValidate() {
        String username = mUsername.getText().toString().trim();
        String password = mPwd.getText().toString().trim();
        String rePassword = mRePwd.getText().toString().trim();
        String desc = mDesc.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String ageStr = mAge.getText().toString().trim();
        if ("".equals(ageStr)){
            Toast.makeText(this, "年龄必须为整数", Toast.LENGTH_SHORT).show();
            return;
        }
        char[] chs =  ageStr.toCharArray();
        for (char c: chs){
            if (c>='0' && c<='9'){
                continue;
            }else{
                Toast.makeText(this, "年龄必须为整数", Toast.LENGTH_SHORT).show();
                return ;
            }
        }
        Integer age = Integer.parseInt(mAge.getText().toString());

        if (mAgree.isChecked()){

            if (!TextUtils.isEmpty(username) & !TextUtils.isEmpty(password) & !TextUtils.isEmpty(rePassword)  & !TextUtils.isEmpty(email) ){
                if (!password.equals(rePassword)){
                    Toast.makeText(this, "两次密码不一致",Toast.LENGTH_SHORT).show();
                }else{

                    //邮箱格式判断

                    //密码md5加密
                    String md5Pwd = MD5Util.MD5EncodeUtf8(password);

                    if (TextUtils.isEmpty(desc)){
                        desc = "这个人很懒，什么都没留下......";
                    }

                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(md5Pwd);
                    user.setEmail(email);
                    user.setDesc(desc);
                    user.setAge(age);
                    user.setSex(gender);
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e == null){
                                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(RegisterActivity.this, "注册失败！"+e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }
            }else{
                Toast.makeText(this, "编辑项不能为空",Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "请勾选协议后注册", Toast.LENGTH_SHORT).show();
        }
    }
}
