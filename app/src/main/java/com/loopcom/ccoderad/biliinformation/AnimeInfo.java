package com.loopcom.ccoderad.biliinformation;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.loopcom.ccoderad.biliinformation.utils.ProgressGenerator;

import org.json.JSONArray;
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

public class AnimeInfo extends AppCompatActivity implements WaveSwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener ,ProgressGenerator.OnCompleteListener{

    private String URL;
    private Spinner mYear;
    private Spinner mMonth;
    private ListView mList;
    private ProgressBar mProgressbar;
    private WaveSwipeRefreshLayout mRL;
//    private ActionProcessButton mBtnGet;
    private TextView tvInfoErr;
    private LoadTask loadTask;
    private ProgressGenerator progressGenerator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.anime_info);
        mYear = (Spinner) findViewById(R.id.yearspinner);
        mMonth= (Spinner) findViewById(R.id.monthspinner);
        mList= (ListView) findViewById(R.id.infolist);
        mProgressbar = (ProgressBar) findViewById(R.id.infopg);
//        mBtnGet = (ActionProcessButton) findViewById(R.id.getInfo);
        mRL = (WaveSwipeRefreshLayout) findViewById(R.id.info_fresh);
        tvInfoErr = (TextView) findViewById(R.id.info_err);
        mRL.setWaveColor(Color.argb(255, 197, 103, 199));
        mRL.setColorSchemeColors(Color.WHITE, Color.argb(255, 197, 103, 199), Color.argb(255, 222, 90, 224));
//        mBtnGet.setOnClickListener(this);
        mRL.setOnRefreshListener(this);
        mYear.setOnItemSelectedListener(this);
        mMonth.setOnItemSelectedListener(this);
        progressGenerator = new ProgressGenerator(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        Toast.makeText(this, "请选择年份月份", Toast.LENGTH_LONG).show();
    }

    void getInfo(){
        String year = mYear.getSelectedItem().toString();
        String month = mMonth.getSelectedItem().toString();
        year=year.replace("年","");
        month=month.replace("月","");
        URL = "http://www.bilibili.tv/index/bangumi/"+year+"-"+month+".json";
        List<AnimeBean> DataList=new ArrayList<>();
        loadTask=new LoadTask();
        loadTask.execute(URL);
    }


//    List<AnimeBean> getJson(String url){
//        List<AnimeBean> list = new ArrayList<>();
//        try {
//            AnimeBean bean;
//            String jsonString = Read(new URL(url).openStream());
//            JSONObject jsonObject;
//            JSONArray jsonArray  = new JSONArray(jsonString);
//            for(int i=0;i<jsonArray.length();i++){
//                if(loadTask.isCancelled()){
//                    return null;
//                }
//                jsonObject = jsonArray.getJSONObject(i);
//                bean = new AnimeBean();
//                bean.IconUrl=jsonObject.getString("cover");
//                bean.spid = jsonObject.getInt("spid");
//                bean.typeid = jsonObject.getInt("typeid");
//                bean.Title = jsonObject.getString("title");
//                bean.weekInfo=jsonObject.getString("weekday");
//                list.add(bean);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

    String Read(InputStream is){
        InputStreamReader Isr;
        String res="";
        try {
            String line="";
            Isr = new InputStreamReader(is,"utf-8");
            BufferedReader bfr = new BufferedReader(Isr);
            while((line=bfr.readLine())!=null){
                if(loadTask.isCancelled()){
                    return null;
                }
                res+=line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("xys",res);
        return res;
    }


    @Override
    public void onRefresh() {
        getInfo();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(loadTask!=null && loadTask.getStatus()== AsyncTask.Status.RUNNING){
            loadTask.cancel(true);
        }
        getInfo();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        return;
    }

    @Override
    public void onComplete() {

    }


    class LoadTask extends AsyncTask<String,Void,List<AnimeBean>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mBtnGet.setMode(ActionProcessButton.Mode.PROGRESS);
            mProgressbar.setVisibility(View.VISIBLE);
//            progressGenerator.start(mBtnGet);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<AnimeBean> doInBackground(String... params) {
            List<AnimeBean> list = new ArrayList<>();
            try {
                AnimeBean bean;
                String jsonString = Read(new URL(params[0]).openStream());
                JSONObject jsonObject;
                JSONArray jsonArray  = new JSONArray(jsonString);
                for(int i=0;i<jsonArray.length();i++){
                    if(loadTask.isCancelled()){
                        return null;
                    }
                    jsonObject = jsonArray.getJSONObject(i);
                    bean = new AnimeBean();
                    bean.IconUrl=jsonObject.getString("cover");
                    bean.spid = jsonObject.getInt("spid");
                    bean.typeid = jsonObject.getInt("typeid");
                    bean.Title = jsonObject.getString("title");
                    bean.weekInfo=jsonObject.getString("weekday");
                    list.add(bean);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(loadTask.isCancelled()){
                return null;
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<AnimeBean> animeBeans) {
            super.onPostExecute(animeBeans);
            if(mRL.isRefreshing()){
                mRL.setRefreshing(false);
            }
            mProgressbar.setVisibility(View.INVISIBLE);
            InfoAdapter adapter= new InfoAdapter(AnimeInfo.this,animeBeans);
            mList.setAdapter(adapter);
            if(mList.getCount()==0){
                tvInfoErr.setVisibility(View.VISIBLE);
            }else {
                tvInfoErr.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(loadTask!=null && loadTask.getStatus()== AsyncTask.Status.RUNNING){
            loadTask.cancel(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadTask!=null && loadTask.getStatus()== AsyncTask.Status.RUNNING){
            loadTask.cancel(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(loadTask!=null && loadTask.getStatus()== AsyncTask.Status.RUNNING){
            loadTask.cancel(true);
        }
    }
}
