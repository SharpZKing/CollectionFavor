package com.sharp.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zjfsharp on 2017/6/23.
 */
public class SpiderSomeInfo {

    /**
     * 获取url网站下网页的标题
     * @param url
     * @return
     */
    public static String getUrlTitle(String url){
        String title = null;

        BufferedReader br = null;
        HttpURLConnection conn;
        StringBuilder sb = new StringBuilder();
        try {
            URL urlReal = new URL(url);
            conn = (HttpURLConnection) urlReal.openConnection();
            conn.connect();
            conn.setConnectTimeout(10000);
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = br.readLine())!=null){
                sb.append(line);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }finally {
            if (br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        title = getTile(sb.toString());

        return title;
    }

    /**
     * 由 getUrlTitle 调用
     * @param html
     * @return
     */
    private static String getTile(String html) {
        String patternStr = "<title>[\\s\\S]*?</title>";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(html);
        String title = null;
        if (matcher.find()){
            title = matcher.group(0);
            title = title.substring(7, title.length()-8);
        }

        return title;
    }

}
