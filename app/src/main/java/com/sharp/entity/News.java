package com.sharp.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by zjfsharp on 2017/6/20.
 */

public class News extends BmobObject {

    /*BmobObject类本身包含objectId、createdAt、updatedAt、ACL四个默认的属性，
    objectId是数据的唯一标示，相当于数据库中表的主键，createdAt是数据的创建时间，
    updatedAt是数据的最后修改时间，ACL是数据的操作权限*/

    private String url;
    private String imageUrl;
    private String title;
    private Integer liked;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLiked() {
        return liked;
    }

    public void setLiked(Integer liked) {
        this.liked = liked;
    }
}
