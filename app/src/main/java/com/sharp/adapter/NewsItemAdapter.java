package com.sharp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharp.collectionfavor.DetailActivity;
import com.sharp.collectionfavor.R;
import com.sharp.entity.News;
import com.sharp.util.LoadImages;

import java.util.List;

/**
 * Created by zjfsharp on 2017/6/11.
 */
public class NewsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<News> dataList;
    private LayoutInflater mLayoutInflater;
    private Handler handler = new Handler();

    public NewsItemAdapter(Context context, List dataList){
        this.mContext = context;
        this.dataList = dataList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsHolder(mLayoutInflater.inflate(R.layout.news_item,parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final NewsHolder mHolder = (NewsHolder) holder;
        final News news = dataList.get(position);
        mHolder.mTitle.setText(news.getTitle());
//        mHolder.mCreateTime.setText(collection.getTime());
        LoadImages loadImages = new LoadImages(news.getImageUrl());
        loadImages.loadImage(new LoadImages.ImageCallback() {
            @Override
            public void getBitmap(Bitmap bitmap) {
                mHolder.mPic.setImageBitmap(bitmap);
            }
        });

        mHolder.url = news.getUrl();
        mHolder.imageUrl = news.getImageUrl();
        mHolder.liked = news.getLiked();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }



    class NewsHolder extends RecyclerView.ViewHolder{

        TextView mTitle;
        ImageView mPic;

        String url;
        String imageUrl;
        int liked;


        public NewsHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.item_discovery_title);
            mPic = (ImageView) itemView.findViewById(R.id.item_news_icon);

            itemView.findViewById(R.id.item_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext, "short click", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("url_detail", url);
                    mContext.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Toast.makeText(mContext, "long click", Toast.LENGTH_SHORT ).show();

                    return false;
                }
            });
        }

    }
}
