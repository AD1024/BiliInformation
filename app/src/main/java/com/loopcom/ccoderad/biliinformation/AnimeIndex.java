package com.loopcom.ccoderad.biliinformation;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopcom.ccoderad.biliinformation.Adapters.AnimeIndexAdapter;
import com.loopcom.ccoderad.biliinformation.Beans.AnimeIndexBean;
import com.loopcom.ccoderad.biliinformation.listener.MyOnClickListener;
import com.loopcom.ccoderad.biliinformation.listener.MyOnLongClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnimeIndex extends AppCompatActivity implements AdapterView.OnItemSelectedListener, XRecyclerView.LoadingListener, View.OnLongClickListener, MyOnLongClickListener, MyOnClickListener{

    @Override
    public boolean onLongClick(View v) {
        String url = (String) v.findViewById(R.id.anime_index_itme_card).getTag();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        return false;
    }

    /*
    *
    * Implements of Item On Click/On Long
    * CLick Listener for RecyclerView on this activity
    * */

    @Override
    public void onLongClick(View v, int position) {
        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(mData.get(position-2).animeUrl)));
    }

    @Override
    public void onItemClick(View v, int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.anime_item_dialog);
        TextView tvTitle = (TextView) window.findViewById(R.id.anime_item_dialog_title);
        TextView tvWeek = (TextView) window.findViewById(R.id.anime_item_dialog_week);
        TextView tvUpdated = (TextView) window.findViewById(R.id.anime_item_dialog_updated);
        TextView tvStatus = (TextView) window.findViewById(R.id.anime_item_dialog_status);
        SimpleDraweeView cover = (SimpleDraweeView) window.findViewById(R.id.anime_item_dialog_pic);
        cover.setImageURI(Uri.parse(mData.get(position-2).picUrl));
        AnimeIndexBean bean = mData.get(position-2);
        String updatedTime = "";
        switch (bean.animeUpdateTime){
            case "-1":
                //Maybe not right..Only god knows what does it mean....
                updatedTime = "更新日期:不再更新了呢";
                break;
            case "0":
                updatedTime = "更新日期:星期日";
                break;
            case "1":
                updatedTime = "更新日期:星期一";
                break;
            case "2":
                updatedTime = "更新日期:星期二";
                break;
            case "3":
                updatedTime = "更新日期:星期三";
                break;
            case "4":
                updatedTime = "更新日期:星期四";
                break;
            case "5":
                updatedTime = "更新日期:星期五";
                break;
            case "6":
                updatedTime = "更新日期:星期六";
                break;
            default:updatedTime = "更新日期:未知";break;
        }
        tvWeek.setText(updatedTime);
        String updated = "已更新至:"+bean.animeUpdatedCount+"集"+" "+"共"+Integer.toString(bean.animeTotalCount)+"集";
        tvUpdated.setText(updated);
        tvStatus.setText(bean.animeIsFinished);
        tvTitle.setText(bean.animeIndexTitle);
    }

    //End Of Certain Listener
    //End OF Certain Listener


    class SpaceItemDecoration extends RecyclerView.ItemDecoration{

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if (parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }

    XRecyclerView mListView;
    Spinner mHeaderSpinner;
    FloatingActionButton fab;
    private int pages;
    AnimeIndexFetcher mFetcher;
    private List<AnimeIndexBean> mData;
    String count;
    private int tot=0;
    AnimeIndexAdapter animeIndexAdapter;
    private Button btnRand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_anime_index);
        mListView = (XRecyclerView) findViewById(R.id.anime_index_rv);
        mListView.setRefreshProgressStyle(ProgressStyle.BallClipRotate);
        mListView.setLaodingMoreProgressStyle(ProgressStyle.CubeTransition);
        View ListHeader = LayoutInflater.from(this).inflate(R.layout.anime_rv_header, null);
        mHeaderSpinner = (Spinner) ListHeader.findViewById(R.id.anime_index_spinner);
        mHeaderSpinner.setOnItemSelectedListener(this);
//        StartFetch(-1);
        mData=new ArrayList<>();
        animeIndexAdapter=new AnimeIndexAdapter(this,mData);
        animeIndexAdapter.setOnItemLongCLickListener(this);
        animeIndexAdapter.setOnItemClickListener(this);
        btnRand = (Button) findViewById(R.id.anime_index_rand);
        btnRand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random(System.currentTimeMillis());
                int pos = random.nextInt(tot);
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(mData.get(pos-2).animeUrl)));
            }
        });
        mListView.addHeaderView(ListHeader);
        mListView.setPullRefreshEnabled(true);
        mListView.setLoadingMoreEnabled(true);
        mListView.setLoadingListener(this);
        mListView.setAdapter(animeIndexAdapter);
        mListView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mListView.addItemDecoration(new SpaceItemDecoration(40));
        pages=1;
        mListView.addOnScrollListener(new OnScrollListener() {

            //This is a fucking cool listener!Much simpler than traditional ListView
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0){
                    fab.hide();
                }else {
                    fab.show();
                }
            }
            /* Alternative Listener:UI enhancement.
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }
            }
            */
        });
        fab = (FloatingActionButton) findViewById(R.id.btn_back_to_top);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.smoothScrollToPosition(0);
            }
        });
        mListView.setOnLongClickListener(this);
        mListView.setItemAnimator(new DefaultItemAnimator());
    }

    /*
    * Parameter: status-->-1 for refersh others for load more
    * */
    private void StartFetch(int status){
        String requestUrl;
        String order;
        switch (mHeaderSpinner.getSelectedItem().toString()) {
            case "更新排序":
                order = "0";
                break;
            case "订阅排序":
                order = "1";
                break;
            default:
                order = "2";
                break;
        }
        if(status==-1) {
            requestUrl = "http://www.bilibili.com/api_proxy?app=bangumi&action=site_season_index&page=1&indexType=" + order+"&pagesize=40";
            mFetcher = new AnimeIndexFetcher();
            mFetcher.execute(requestUrl);
        }else{
            requestUrl="http://www.bilibili.com/api_proxy?app=bangumi&action=site_season_index&page="+Integer.toString(pages)+"&indexType="+order+"&pagesize=40";
            mFetcher = new AnimeIndexFetcher();
            mFetcher.execute(requestUrl);
        }
        tot+=40;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mData.clear();
        tot=0;
       StartFetch(-1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRefresh() {
        pages=1;
        mData.clear();
        StartFetch(-1);
    }

    @Override
    public void onLoadMore() {
        pages+=1;
        StartFetch(pages);
    }

    class AnimeIndexFetcher extends AsyncTask<String,Void,List<AnimeIndexBean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setTitle("拉取信息ing");
        }

        @Override
        protected List<AnimeIndexBean> doInBackground(String... params) {
            InputStream is = null;
            AnimeIndexBean bean;
            List<AnimeIndexBean> resList = new ArrayList<>();
            try {
                is = new URL(params[0]).openStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String jsonString="";
                String Temp="";
                while ((Temp=br.readLine())!=null){
                    jsonString+=Temp;
                }
                JSONObject jsonObject=new JSONObject(jsonString).getJSONObject("result");
                count=jsonObject.getString("count");
                JSONArray jsonArray =  jsonObject.getJSONArray("list");
                for(int i=0;i<jsonArray.length();i++){
                    if(mFetcher.isCancelled()){
                        return null;
                    }
                    bean = new AnimeIndexBean();
                    jsonObject = jsonArray.getJSONObject(i);
                    bean.animeIndexTitle = jsonObject.getString("title");
                    switch (jsonObject.getInt("is_finish")){
                        case 1:
                            bean.animeIsFinished="正在连载";
                            break;
                        case 2:
                            bean.animeIsFinished="已完结";
                            break;
                        default:bean.animeIsFinished="未知";
                    }
                    bean.animeUpdatedCount=jsonObject.getString("newest_ep_index");
                    bean.animeTotalCount=jsonObject.getInt("total_count");
                    bean.animeUpdateTime=jsonObject.getString("week");
                    bean.animeUrl=jsonObject.getString("url");
                    bean.picUrl=jsonObject.getString("cover");
                    resList.add(bean);
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return resList;
        }

        @Override
        protected void onPostExecute(List<AnimeIndexBean> animeIndexBeans) {
            super.onPostExecute(animeIndexBeans);
            if(animeIndexBeans.size()==0){
                setTitle("加载失败...");
                Toast.makeText(AnimeIndex.this,"请检查网络连接",Toast.LENGTH_SHORT).show();
                return;
            }
            mData.addAll(animeIndexBeans);
            mListView.refreshComplete();
            mListView.loadMoreComplete();
            setTitle("番剧索引");
            animeIndexAdapter.notifyDataSetChanged();
        }
    }

    private void stopTask(){
     if(mFetcher!=null && mFetcher.getStatus()== AsyncTask.Status.RUNNING){
         mFetcher.cancel(true);
     }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTask();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTask();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTask();
    }
}
