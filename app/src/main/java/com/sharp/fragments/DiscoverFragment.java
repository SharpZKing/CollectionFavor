package com.sharp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
import com.sharp.adapter.NewsItemAdapter;
import com.sharp.collectionfavor.R;
import com.sharp.entity.News;
import com.sharp.util.ConstUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjfsharp on 2017/6/3.
 */
public class DiscoverFragment extends Fragment {


    private RecyclerView discoveryRecyclerView;

    private List<News> newsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        initDatas();

        discoveryRecyclerView = (RecyclerView) view.findViewById(R.id.discovery_recyclerView);
        discoveryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        NewsItemAdapter adapter = new NewsItemAdapter(getActivity(), newsList);
        discoveryRecyclerView.setAdapter(adapter);

        return view;
    }

    private void initDatas() {
        RxVolley.get(ConstUtil.ZHIHU_NEWS_LATESTED_URL, new HttpCallback() {
            @Override
            public void onSuccess(String result) {
                parseJson(result);
            }

            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
            }
        });
    }

    private void parseJson(String jsonStr){
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("stories");
            News news = null;
            for (int i=0; i<jsonArray.length(); i++){
                news = new News();
                newsList.add(news);
                JSONObject jsonNew = (JSONObject) jsonArray.get(i);
                news.setTitle(jsonNew.getString("title"));
                news.setUrl(ConstUtil.ZHIHU_NEWS_DETAIL_URL+jsonNew.get("id"));
                JSONArray jsonArray1 = jsonNew.getJSONArray("images");
                news.setImageUrl(jsonArray1.get(0).toString());
                news.setLiked(0);
            }
            //Toast.makeText(getActivity(), "=="+newsList.get(0).getTitle()+"=="+newsList.get(1).getTitle(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
