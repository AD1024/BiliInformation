package com.loopcom.ccoderad.biliinformation.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopcom.ccoderad.biliinformation.utils.InfoImageLoader;
import com.loopcom.ccoderad.biliinformation.R;
import com.loopcom.ccoderad.biliinformation.Beans.RankBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CCoderAD on 16/2/25.
 */
public class RankAdapter extends BaseAdapter {
    private List<RankBean> mList;
    private InfoImageLoader infoImageLoader;
    private LayoutInflater mInflater;
    private List<View> viewGroup;
    public  RankAdapter(Context context,List<RankBean> list){
        mInflater=LayoutInflater.from(context);
        mList = list;
        viewGroup = new ArrayList<>();
        infoImageLoader=new InfoImageLoader();
    }

    public ImageView requestImg(int pos){
        View view  = viewGroup.get(pos);
        ImageView img = (ImageView) view.findViewById(R.id.rank_img);
        return img;
    }

    private String getType(int position){
        String type = mList.get(position).copyright;
        switch (type){
            case "Original":
                return "原创";
            case "copy":
                return "转载";
            default:return null;
        }
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh =null;
        if(convertView==null){
            vh = new ViewHolder();
            convertView = mInflater.inflate(R.layout.rank_itrm,null);
            vh.av= (TextView) convertView.findViewById(R.id.rank_av_num);
            vh.Title= (TextView) convertView.findViewById(R.id.rank_title);
            vh.pic = (ImageView) convertView.findViewById(R.id.rank_img);
            vh.author = (TextView) convertView.findViewById(R.id.rank_up);
            vh.copyright = (TextView) convertView.findViewById(R.id.rank_copyright);
            vh.type = (TextView) convertView.findViewById(R.id.rank_type);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        vh.pic.setImageResource(R.mipmap.bilirank);
        vh.pic.setTag(mList.get(position).pic);
        infoImageLoader.startLoad(mList.get(position).pic, vh.pic);
        vh.copyright.setText(getType(position));
        vh.Title.setText(mList.get(position).Title);
        vh.type.setText(mList.get(position).type);
        vh.author.setText(mList.get(position).author);
        vh.av.setText(mList.get(position).av);
        viewGroup.add(position,convertView);
        return convertView;
    }

    class ViewHolder {
        TextView av;
        TextView copyright;
        TextView type;
//        public int play;
//        public int comment;
//        public int fav;
        TextView Title;
        ImageView pic;
        TextView author;
//        public String description;
//        public String createDate;
    }
}
