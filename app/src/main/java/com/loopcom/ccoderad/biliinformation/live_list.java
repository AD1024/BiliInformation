package com.loopcom.ccoderad.biliinformation;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.loopcom.ccoderad.biliinformation.Adapters.LiveAdapter;
import com.loopcom.ccoderad.biliinformation.Beans.LiveBean;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class live_list extends AppCompatActivity implements AdapterView.OnItemSelectedListener, WaveSwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemLongClickListener {

    private ListView mList;
    private List<LiveBean> mData;
    private Spinner mSpinner;
    private Spinner mOrder;
    private WaveSwipeRefreshLayout mRL;
    private int pages=1;
    private String type;
    private LiveLoadTask mTask;
    private TextView mError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_live_list);
        mError = (TextView) findViewById(R.id.live_no_content);
        mData = new ArrayList<>();
        mList = (ListView) findViewById(R.id.live_list);
        mSpinner = (Spinner) findViewById(R.id.live_list_spinner);
        mSpinner.setOnItemSelectedListener(this);
        mRL = (WaveSwipeRefreshLayout) findViewById(R.id.live_refersh);
        mOrder = (Spinner) findViewById(R.id.live_list_order_spinner);
        mRL.setOnRefreshListener(this);
        mOrder.setOnItemSelectedListener(this);
        mRL.setWaveColor(Color.rgb(204, 127, 71));
        mRL.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mList.setOnItemLongClickListener(this);

    }

    void startTask(){
        String order = mOrder.getSelectedItem().toString();
        type = mSpinner.getSelectedItem().toString();
        switch (order){
            case "最火直播":
                order="online";
                break;
            case "最新开播":
                order="live_time";
                break;
        }
        String requestUrl = "http://live.bilibili.com/all?ajax=1&order="+order;
        mTask=new LiveLoadTask();
        mTask.execute(requestUrl);
    }


    List<LiveBean> getData(String url){
        List<LiveBean> Result=new ArrayList<>();
        try {
            String jsonString="";
            InputStream is = new URL(url).openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String bufferString="";
            while((bufferString=br.readLine())!=null){
                jsonString+=bufferString;
            }
            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);
            org.json.JSONArray jsonArray = jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                if(mTask.isCancelled()){
                    return null;
                }
                jsonObject = jsonArray.getJSONObject(i);
                LiveBean bean = new LiveBean();
                bean.author = jsonObject.getString("uname");
                bean.category = jsonObject.getInt("area");
                bean.iconURl = jsonObject.getString("cover");
                bean.live_title = jsonObject.getString("title");
                bean.pop = jsonObject.getInt("online");
                bean.roomUrl = jsonObject.getString("link");
                Result.add(bean);
            }
        } catch (IOException e) {
            e.printStackTrace();
//            Toast.makeText(this,"请检查网络连接",Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mData = Result;
        return Result;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        startTask();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        return;
    }

    @Override
    public void onRefresh() {
        startTask();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        TextView text = (TextView)view.findViewById(R.id.live_list_up_name);
        String upname = text.getText().toString();
        String url = "http://live.bilibili.com";
        for(int i=0;i<mData.size();i++){
            if(mData.get(i).author.equals(upname)){
                url+=mData.get(i).roomUrl;
            }
        }
        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
        return false;
    }

    class LiveLoadTask extends AsyncTask<String,Void,List<LiveBean>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!mRL.isRefreshing())
                mRL.setRefreshing(true);
            setTitle("加载中...");
            mError.setVisibility(View.INVISIBLE);
        }

        @Override
        protected List<LiveBean> doInBackground(String... params) {
            return getData(params[0]);
        }

        @Override
        protected void onPostExecute(List<LiveBean> liveBeans) {
            super.onPostExecute(liveBeans);
            if(this.isCancelled()){
                mRL.setRefreshing(false);
                return;
            }
            mRL.setRefreshing(false);
            LiveAdapter adapter = new LiveAdapter(live_list.this,liveBeans,type);
            mList.setAdapter(adapter);
            if(mList.getAdapter().getCount()==0){
                mError.setVisibility(View.VISIBLE);
                setTitle("精选直播");
                return;
            }
            setTitle("精选直播");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTask!=null && mTask.getStatus()== AsyncTask.Status.RUNNING){
            mTask.cancel(true);
        }
    }
}
