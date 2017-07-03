package com.sharp.fragments;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharp.adapter.CollectionItemAdapter;
import com.sharp.collectionfavor.MainActivity;
import com.sharp.collectionfavor.R;
import com.sharp.entity.Collection;
import com.sharp.util.ConstUtil;
import com.sharp.util.SpiderSomeInfo;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class HomeFragment extends Fragment implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private List<String> datas;
    private List<Collection> collList;

    private LinearLayout mAddLL;
    private TextView mUrlTv;
    private Button mAddBtn;

    private ClipboardManager clipboard = null;

    private CollectionItemAdapter adapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstUtil.INIT_COLLECTION_LIST:
                    List<Collection> lists = (List<Collection>) msg.obj;

                    adapter = new CollectionItemAdapter(getActivity(), lists);
                    mRecyclerView.setAdapter(adapter);
                    break;
                case 1:
                    String title = (String) msg.obj;
                    Collection collection = new Collection();
                    collection.setTitle(title);
                    collection.setUrl(mUrlTv.getText().toString());
                    collection.setLiked(1);
                    BmobUser currentUser = BmobUser.getCurrentUser();
                    if (currentUser == null){
                        Toast.makeText(getActivity(), "添加失败，请先登入", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    collection.setUserId(currentUser.getObjectId());

                    collection.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null){
                                Toast.makeText(getActivity(), "成功添加至收藏!", Toast.LENGTH_SHORT).show();
                                mAddLL.setVisibility(View.GONE);
                            }else{
                                Toast.makeText(getActivity(), "添加至收藏夹失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initDatas();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.home_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //TestRecycleViewAdapter adapter = new TestRecycleViewAdapter(getActivity(), datas);
//        CollectionItemAdapter adapter = new CollectionItemAdapter(getActivity(), collList);
//        mRecyclerView.setAdapter(adapter);

        mAddLL = (LinearLayout) view.findViewById(R.id.home_add_url);
        mUrlTv = (TextView) view.findViewById(R.id.home_tv_url);
        mAddBtn = (Button) view.findViewById(R.id.home_add_btn);
        mAddBtn.setOnClickListener(this);

        // 初始化剪切板并判断剪切板里的数据是否是url
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard.getPrimaryClipDescription()!=null && clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData cdata = clipboard.getPrimaryClip();
            final ClipData.Item detail = cdata.getItemAt(0);
            if (detail.getText().toString().startsWith("http")){

                BmobQuery<Collection> query = new BmobQuery<>();
                query.addWhereEqualTo("url", detail.getText().toString());
                BmobUser currentUser = BmobUser.getCurrentUser();
                if (currentUser!=null){
                    query.addWhereEqualTo("userId", currentUser.getObjectId());
                }
                query.findObjects(new FindListener<Collection>() {
                    @Override
                    public void done(List<Collection> list, BmobException e) {
                        if (list.size()>0){
                            mAddLL.setVisibility(View.GONE);
                        }else{
                            mAddLL.setVisibility(View.VISIBLE);
                            mUrlTv.setText(detail.getText().toString());
                        }
                    }
                });

                /*mAddLL.setVisibility(View.VISIBLE);
                mUrlTv.setText(detail.getText().toString());*/
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

        final BmobUser currentUser = BmobUser.getCurrentUser();
        if(currentUser == null){

        }else{
            collList = new ArrayList<>();

            BmobQuery<Collection> query = new BmobQuery<>();
            query.addWhereEqualTo("userId", currentUser.getObjectId());
            query.addWhereEqualTo("liked",1);
            query.order("-createAt");
            query.findObjects(new FindListener<Collection>() {
                @Override
                public void done(List<Collection> list, BmobException e) {

                if (e==null){
                    for (int i=list.size()-1; i>=0; i--){
                        collList.add(list.get(i));
                    }

                    Message message = new Message();
                    message.what = ConstUtil.INIT_COLLECTION_LIST;
                    message.obj = collList;
                    handler.sendMessage(message);


                }else{
                    Log.d("NET", "chucuol ");
                }

                }
            });
        }




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_add_btn:
                ClipData.Item item = null;
                ClipData clipData = clipboard.getPrimaryClip();
                item = clipData.getItemAt(0);

                final String strUrl = item.getText().toString();
                if (strUrl.startsWith("http")){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String title = SpiderSomeInfo.getUrlTitle(strUrl);
                            Message message = new Message();
                            message.obj = title;
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }).start();
                }else{
                    Toast.makeText(getActivity(), "添加失败,似乎开小差了>_<", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
