package com.loopcom.ccoderad.biliinformation;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopcom.ccoderad.biliinformation.Adapters.RankAdapter;
import com.loopcom.ccoderad.biliinformation.Beans.RankBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class WeekRank extends AppCompatActivity implements WaveSwipeRefreshLayout.OnRefreshListener, AbsListView.OnItemLongClickListener, AbsListView.OnItemClickListener {

    private ListView mList;
    private ProgressBar mPg;
    private WaveSwipeRefreshLayout mRL;
    private List<RankBean> data;
    RankTask rankTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_rank);
        mList = (ListView) findViewById(R.id.ranklist);
        mPg = (ProgressBar) findViewById(R.id.rankpg);
        mRL = (WaveSwipeRefreshLayout) findViewById(R.id.rank_refresh);
        mRL.setWaveColor(Color.argb(255, 197, 103, 199));
        mRL.setColorSchemeColors(Color.WHITE, Color.WHITE);
        rankTask= new RankTask();
        rankTask.execute("http://api.bilibili.cn/index");
        mRL.setOnRefreshListener(this);
        data = new ArrayList<>();
//        mList.setOnItemLongClickListener(this);
    }


    void ListenList(){
        mList.setOnItemLongClickListener(this);
        mList.setOnItemClickListener(this);
    }

    private List<RankBean> addtolist(List<JSONObject> arrays) {
        List<RankBean> ans = new ArrayList<>();
        for (int i = 0; i < arrays.size(); i++) {
            RankBean bean = new RankBean();
            JSONObject jsonObject = null;
            jsonObject = arrays.get(i);
            if(rankTask.isCancelled()){
                return null;
            }
            try {
                bean.author = jsonObject.getString("author");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                bean.av = jsonObject.getString("aid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                bean.comment = jsonObject.getInt("comment");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                bean.copyright = jsonObject.getString("copyright");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                bean.createDate = jsonObject.getString("create");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                bean.description = jsonObject.getString("description");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                bean.Title = jsonObject.getString("title");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                bean.fav = jsonObject.getInt("favorites");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                bean.play = jsonObject.getInt("play");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                bean.type = jsonObject.getString("typename");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                bean.pic = jsonObject.getString("pic");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ans.add(bean);
//            data.add(bean);
        }
        return ans;
    }

    private List<RankBean> getList(String Url) {
        List<RankBean> datalist = new ArrayList<>();
        try {
            RankBean bean = new RankBean();
            String jsonString = getJson(new URL(Url).openStream());
            JSONObject jsonObject = new JSONObject(jsonString);
            /*
            Algorithm Falut
            JSONArray jsonArray = jsonObject.getJSONArray("type1");
            JSONArray jsonArray1 = jsonObject.getJSONArray("type3");
            JSONArray jsonArray2 = jsonObject.getJSONArray("type4");
            JSONArray jsonArray3 = jsonObject.getJSONArray("type5");
            JSONArray jsonArray4 = jsonObject.getJSONArray("type11");
            JSONArray jsonArray5 = jsonObject.getJSONArray("type13");
            JSONArray jsonArray6 = jsonObject.getJSONArray("type36");
            JSONArray[] arrays = new JSONArray[]{jsonArray,jsonArray1,jsonArray2,jsonArray3,jsonArray4,jsonArray5,jsonArray6};
            */
            String[] tags = new String[]{"type1", "type3", "type4", "type5", "type11", "type13", "type36"};
            List<JSONObject> jsonObjects = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                JSONObject jobj = jsonObject.getJSONObject(tags[i]);
                for (int j = 0; j < jobj.length() - 1; j++) {
                    if(rankTask.isCancelled()){
                        return null;
                    }
                    JSONObject json = jobj.getJSONObject(Integer.toString(j));
                    jsonObjects.add(json);
                }
            }
            datalist = addtolist(jsonObjects);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datalist;
    }

    private String getJson(InputStream is) {
        InputStreamReader isr = null;
        String res = "";
        try {
            String lines = "";
            isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            while ((lines = br.readLine()) != null) {
                res += lines;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("xys", res);
        return res;

    }

    @Override
    public void onRefresh() {
        new RankTask().execute("http://api.bilibili.cn/index");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        RankBean rb;
        Context context = WeekRank.this;
        ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        rb = (RankBean) mList.getAdapter().getItem(position);
        ClipData clipData = ClipData.newPlainText("AVNum",rb.av);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, "AV号已复制", Toast.LENGTH_SHORT).show();
//        String url = "http://www.bilibli.com/mobile/video/av"+rb.av+".html";
//        Uri uri = Uri.parse(url);
//        startActivity(new Intent(Intent.ACTION_VIEW,uri));
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this,detiails_rank.class);
        Bundle b = new Bundle();
        RankBean bean = (RankBean) mList.getAdapter().getItem(position);
        b.putSerializable("Data",bean);
        i.putExtras(b);
        startActivity(i);
    }


    class RankTask extends AsyncTask<String, Void, List<RankBean>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPg.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<RankBean> doInBackground(String... params) {
            List<RankBean> bean = getList(params[0]);
            if(rankTask.isCancelled()){
                return null;
            }
            return bean;
        }

        @Override
        protected void onPostExecute(List<RankBean> list) {
            super.onPostExecute(list);
            if(rankTask.isCancelled()){
                return;
            }
            mPg.setVisibility(View.INVISIBLE);
            if (mRL.isRefreshing()) {
                mRL.setRefreshing(false);
            }
            Log.i("xys", "Data Ready");
            RankAdapter adapter = new RankAdapter(WeekRank.this, list);
            mList.setAdapter(adapter);
            ListenList();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(rankTask!=null && rankTask.getStatus()== AsyncTask.Status.RUNNING){
            rankTask.cancel(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(rankTask!=null && rankTask.getStatus()== AsyncTask.Status.RUNNING){
            rankTask.cancel(true);
        }
    }
}
