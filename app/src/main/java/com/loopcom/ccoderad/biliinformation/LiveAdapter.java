package com.loopcom.ccoderad.biliinformation;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CCoderAD on 16/2/26.
 */
public class LiveAdapter extends BaseAdapter {

    private List<LiveBean> mList;
    private LayoutInflater mInflator;
    private static String[] cats = new String[]{"","萌宅推荐","御宅文化","生活娱乐","单机游戏", "网络游戏","电子竞技","放映间"};

    public LiveAdapter(Context context,List<LiveBean> data,String category){
        mList=new ArrayList<>();
        if(category.equals("全部")){
            for(int i=0;i<data.size();i++){
                if(data.get(i).category<8 && data.get(i).category>0){
                    mList.add(data.get(i));
                }
            }
        }else {
            getCatData(data,category);
        }
        mInflator = LayoutInflater.from(context);
    }

    public void getCatData(List<LiveBean> data,String sort){
        for(int i=0;i<data.size();i++){
            if(data.get(i).category<8 && data.get(i).category>0){
                if(sort.equals(cats[data.get(i).category])){
                    mList.add(data.get(i));
                }
            }
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
        ViewHolder vh;
        if(convertView==null){
            vh=new ViewHolder();
            convertView = mInflator.inflate(R.layout.content_live_list,null);
            vh.author = (TextView) convertView.findViewById(R.id.live_list_up_name);
            vh.category = (TextView) convertView.findViewById(R.id.live_list_sort);
            vh.icon = (SimpleDraweeView) convertView.findViewById(R.id.live_list_icon);
            vh.pop = (TextView) convertView.findViewById(R.id.live_list_aud);
            vh.live_title = (TextView) convertView.findViewById(R.id.live_list_title);
            convertView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }
        vh.author.setText(mList.get(position).author);
       // Log.i("CatNum", Integer.toString(mList.get(position).category));
        vh.category.setText(cats[mList.get(position).category]);
        vh.pop.setText(Integer.toString(mList.get(position).pop));
        vh.live_title.setText(mList.get(position).live_title);
        Uri uri = Uri.parse(mList.get(position).iconURl);
        vh.icon.setImageURI(uri);
        return convertView;
    }

    class ViewHolder{
        public SimpleDraweeView icon;
        public TextView live_title;
        public TextView author;
        public TextView pop;
        public TextView category;
    }
}
