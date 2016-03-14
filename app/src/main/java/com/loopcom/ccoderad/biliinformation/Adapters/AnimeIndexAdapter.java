package com.loopcom.ccoderad.biliinformation.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.loopcom.ccoderad.biliinformation.Beans.AnimeIndexBean;
import com.loopcom.ccoderad.biliinformation.R;
import com.loopcom.ccoderad.biliinformation.listener.MyOnClickListener;
import com.loopcom.ccoderad.biliinformation.listener.MyOnLongClickListener;


import java.util.List;

/**
 * Created by CCoderAD on 16/3/5.
 */
public class AnimeIndexAdapter extends RecyclerView.Adapter<AnimeIndexAdapter.ViewHolder> {
    List<AnimeIndexBean> mData;
    Context mContext;
    MyOnLongClickListener mLongClickListener;
    MyOnClickListener mOnClickListener;
    public AnimeIndexAdapter(Context context,List<AnimeIndexBean> mList){
        mContext=context;
        mData=mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.anime_index_item,null);
        ViewHolder vh = new ViewHolder(v,mLongClickListener,mOnClickListener);
        return vh;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(AnimeIndexAdapter.ViewHolder holder, int position) {
        final AnimeIndexBean resource = mData.get(position);
        Uri uri = Uri.parse(resource.picUrl);
        holder.mPic.setImageURI(uri);
        holder.isFinished.setText(resource.animeIsFinished);
        holder.animeTitle.setText(resource.animeIndexTitle);
        holder.animeCount.setText(resource.animeUpdatedCount);
        holder.cdv.setTag(resource.animeUrl);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemLongCLickListener(MyOnLongClickListener Listener){
        this.mLongClickListener=Listener;
    }

    public void setOnItemClickListener(MyOnClickListener listener){
        this.mOnClickListener=listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        SimpleDraweeView mPic;
        TextView animeTitle;
        TextView animeCount;
        TextView isFinished;
        CardView cdv;
        MyOnClickListener myOnClickListener;
        MyOnLongClickListener mListener;
        public ViewHolder(View itemView,MyOnLongClickListener listener,MyOnClickListener clickListener) {
            super(itemView);
            this.
            mPic= (SimpleDraweeView) itemView.findViewById(R.id.anime_index_item_pic);
            animeCount= (TextView) itemView.findViewById(R.id.anime_index_item_updated_count);
            animeTitle= (TextView) itemView.findViewById(R.id.anime_index_item_title);
            isFinished = (TextView) itemView.findViewById(R.id.anime_index_is_finished);
            cdv = (CardView) itemView.findViewById(R.id.anime_index_itme_card);
            this.mListener=listener;
            this.myOnClickListener = clickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if(mListener!=null){
                mListener.onLongClick(v,getPosition());
            }
            return true;
        }


        @Override
        public void onClick(View v) {
            if(myOnClickListener!=null){
                myOnClickListener.onItemClick(v,getPosition());
            }
        }
    }

}
