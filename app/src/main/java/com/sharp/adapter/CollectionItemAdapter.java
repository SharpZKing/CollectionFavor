package com.sharp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.AsyncLayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharp.collectionfavor.DetailActivity;
import com.sharp.collectionfavor.R;
import com.sharp.entity.Collection;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by zjfsharp on 2017/6/11.
 */
public class CollectionItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Collection> dataList;
    private LayoutInflater mLayoutInflater;

    public CollectionItemAdapter(Context context, List dataList){
        this.mContext = context;
        this.dataList = dataList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollectionHolder(mLayoutInflater.inflate(R.layout.collection_item,parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CollectionHolder mHolder = (CollectionHolder) holder;
        Collection collection = dataList.get(position);
        mHolder.mTitle.setText(collection.getTitle());
        mHolder.mCreateTime.setText(collection.getTime());
        mHolder.url = collection.getUrl();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CollectionHolder extends RecyclerView.ViewHolder{

        TextView mTitle;
        TextView mCreateTime;
        ImageView mPic;

        String url;

        public CollectionHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.item_title);
            mCreateTime = (TextView) itemView.findViewById(R.id.item_time);
            mPic = (ImageView) itemView.findViewById(R.id.item_icon);

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
