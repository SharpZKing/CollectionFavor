package com.sharp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zjfsharp on 2017/6/22.
 */

public class LoadImages {

    public String imageUrl;

    public LoadImages(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void loadImage(final ImageCallback callback){

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bitmap bmp = (Bitmap) msg.obj;

                callback.getBitmap(bmp);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp = getURLimage(imageUrl);
                Message message = new Message();
                message.obj = bmp;

                handler.sendMessage(message);
            }
        }).start();
    }

    //加载图片
    private Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public interface ImageCallback {
        public void getBitmap(Bitmap bitmap);
    }

}
