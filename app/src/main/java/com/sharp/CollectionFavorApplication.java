package com.sharp;

import android.app.Application;

import com.sharp.collectionfavor.R;

import cn.bmob.v3.Bmob;

/**
 * Created by zjfsharp on 2017/6/10.
 */
public class CollectionFavorApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Bmob.initialize(this, getString(R.string.appId));

    }
}
