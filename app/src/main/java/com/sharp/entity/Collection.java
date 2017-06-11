package com.sharp.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by zjfsharp on 2017/6/11.
 */
public class Collection extends BmobObject {

    private String title;

    private String url;

    private String time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
