package com.sharp.entity;

import cn.bmob.v3.BmobUser;

/**
 * Created by zjfsharp on 2017/6/10.
 */
public class User extends BmobUser {

    private String sex; //性别
    private String desc; //一句话描述内容
    private int state; //用户状态： 0: 正常;  1: 异常;
    private Integer age;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


}
