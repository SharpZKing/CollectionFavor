package com.sharp.fragments;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharp.adapter.CollectionItemAdapter;
import com.sharp.adapter.TestRecycleViewAdapter;
import com.sharp.collectionfavor.R;
import com.sharp.entity.Collection;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private List<String> datas;
    private List<Collection> collList;

    private LinearLayout mAddLL;
    private TextView mUrlTv;
    private Button mAddBtn;

    private ClipboardManager clipboard = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initDatas();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.home_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //TestRecycleViewAdapter adapter = new TestRecycleViewAdapter(getActivity(), datas);
        CollectionItemAdapter adapter = new CollectionItemAdapter(getActivity(), collList);
        mRecyclerView.setAdapter(adapter);

        mAddLL = (LinearLayout) view.findViewById(R.id.home_add_url);
        mUrlTv = (TextView) view.findViewById(R.id.home_tv_url);
        mAddBtn = (Button) view.findViewById(R.id.home_add_btn);
        mAddBtn.setOnClickListener(this);

        // 初始化剪切板并判断剪切板里的数据是否是url
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData cdata = clipboard.getPrimaryClip();
            ClipData.Item detail = cdata.getItemAt(0);
            if (detail.getText().toString().startsWith("http")){
                mAddLL.setVisibility(View.VISIBLE);
                mUrlTv.setText(detail.getText().toString());
            }else{
                mAddLL.setVisibility(View.GONE);
            }

        }else{
            mAddLL.setVisibility(View.GONE);
        }

        //设置剪切板监听事件 并控制视图显示
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData.Item item = null;
                if (!clipboard.hasPrimaryClip()){
                    mAddLL.setVisibility(View.GONE);
                }

                if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) ){

                    ClipData clipData = clipboard.getPrimaryClip();
                    item = clipData.getItemAt(0);
                    if(item.getText() == null){
                        Toast.makeText(getActivity(), "剪贴板中无内容", Toast.LENGTH_SHORT).show();
                    }else{
//                        Toast.makeText(getActivity(),item.getText().toString(), Toast.LENGTH_SHORT).show();
                        String strUrl = item.getText().toString();
                        if (strUrl.startsWith("http")){
                            mUrlTv.setText(item.getText().toString());
                            mAddLL.setVisibility(View.VISIBLE);
                        }else{
                            mAddLL.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

        return view;
    }

    private void initDatas() {

        collList = new ArrayList<>();
        Collection collection = null;
        for (int i=1; i<=20; i++){
            collection = new Collection();
            collection.setTitle("这是数据在测试这是数据在测试这是数据在测试"+i);
            collection.setUrl("http://mp.weixin.qq.com/s/wDuNAzV7JbRaH1Pe9o4B2g");
            collection.setTime("2017年6月"+i+"日");
            collList.add(collection);
        }

        /*datas = new ArrayList<>();
        for(int i=0; i<16; i++){
            datas.add("Item: "+ i + " ......");
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_add_btn:
                ClipData.Item item = null;
                ClipData clipData = clipboard.getPrimaryClip();
                item = clipData.getItemAt(0);
                Toast.makeText(getActivity(), item.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
