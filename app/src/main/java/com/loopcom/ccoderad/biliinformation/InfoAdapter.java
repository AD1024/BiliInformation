package com.loopcom.ccoderad.biliinformation;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by CCoderAD on 16/2/24.
 */
public class InfoAdapter extends BaseAdapter {


    List<AnimeBean> mList;
    LayoutInflater mInflater;
    String[] wk = new String[]{"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
   // private InfoImageLoader infoImageLoader;
    public InfoAdapter(Context context,List<AnimeBean> Data) {
        mList = Data;
       // infoImageLoader = new InfoImageLoader();
        mInflater = LayoutInflater.from(context);
    }

    private String getWeekInfo(int p){
        String cmp = mList.get(p).weekInfo;
        String ans="";
        switch (cmp){
            case "0":
                ans=wk[0];
                break;
            case "1":
                ans=wk[1];
                break;
            case "2":
                ans=wk[2];
                break;
            case "3":
                ans=wk[3];
                break;
            case "4":
                ans=wk[4];
                break;
            case "5":
                ans=wk[5];
                break;
            case "6":
                ans=wk[6];
                break;
            default:return null;
        }
        return ans;
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
            convertView = mInflater.inflate(R.layout.info_item,null);
            vh.weekinfo= (TextView) convertView.findViewById(R.id.info_time);
//            vh.spid= (TextView) convertView.findViewById(R.id.info_spid);
            vh.title= (TextView) convertView.findViewById(R.id.tvTitle);
//            vh.typeid= (TextView) convertView.findViewById(R.id.info_typeid);
            vh.img = (SimpleDraweeView) convertView.findViewById(R.id.info_img);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
//        vh.img.setTag(mList.get(position).IconUrl);
//       infoImageLoader.startLoad(mList.get(position).IconUrl,vh.img);
        Uri uri =Uri.parse(mList.get(position).IconUrl);
        vh.img.setImageURI(uri);
//        vh.typeid.setText(mList.get(position).typeid);
        vh.weekinfo.setText(getWeekInfo(position));
        vh.title.setText(mList.get(position).Title);
//        vh.spid.setText(mList.get(position).spid);
        return convertView;
    }

    class ViewHolder{
        SimpleDraweeView img;
        TextView weekinfo;
        TextView title;
        TextView spid;
        TextView typeid;
    }
}
