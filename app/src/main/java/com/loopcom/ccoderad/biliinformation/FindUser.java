package com.loopcom.ccoderad.biliinformation;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopcom.ccoderad.biliinformation.Beans.ResultBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FindUser extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private TextView mName;
    private TextView mSex;
    public TextView mUpid;
    public TextView mVerti;
    public TextView mFans;
    public TextView mAttention;
    public TextView mLocation;
    public TextView mArticle;
    public TextView mBirth;
    private TextView mDescription;
    private SearchView mSearchView;
    private Button btnAtt;
    private List<ResultBean> mData;
    private List<String> attentions;
    private SimpleDraweeView mFace;
    SQLiteDatabase mDataBase;
    SearchTask mTask;
    String searchItem;

    @Override
    public boolean onQueryTextSubmit(String query) {
//        AddSuggestion(query);
        searchItem = query;
        //TODO:getInformation From Bilibili
        boolean typeFlag = true;
        for (int i = 0; i < query.length(); i++) {
            if (query.charAt(i) <= '9' && query.charAt(i) >= '0') {
                continue;
            } else {
                typeFlag = false;
                break;
            }
        }
        String searchUrl = "http://api.bilibili.cn/userinfo?";
        if (typeFlag) {
            searchUrl += "mid=" + query;
        } else {
            searchUrl += "user=" + query;
        }
        mTask = new SearchTask();
        mTask.execute(searchUrl);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void RendData(ResultBean bean) {
        if (bean.up_name == null) {
            Toast.makeText(FindUser.this, "呜呜呜,好像没找到！", Toast.LENGTH_SHORT).show();
            setTitle("没找到呢...");
            return;
        }
        if (bean.up_name.equals("AD1024")) {
            Toast.makeText(this, "哈哈，你找到我啦！", Toast.LENGTH_SHORT).show();
            setTitle("这就是我O！！");
        }
        mName.setText(bean.up_name);
        mSex.setText(bean.sex);
        mUpid.setText(bean.up_id);
        mLocation.setText(bean.location);
        mBirth.setText(bean.birth);
        mVerti.setText(bean.verti);
        mFans.setText(Integer.toString(bean.fans));
        mAttention.setText(Integer.toString(bean.attention));
        mDescription.setText(bean.sign);
        mArticle.setText(Integer.toString(bean.article));
        Uri uri = Uri.parse(bean.face);
        mFace.setImageURI(uri);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        mSearchView = (SearchView) findViewById(R.id.searchView);
        mName = (TextView) findViewById(R.id.tv_search_up_name);
        mSex = (TextView) findViewById(R.id.search_up_sex);
        mUpid = (TextView) findViewById(R.id.search_up_id);
        mVerti = (TextView) findViewById(R.id.search_up_vertification);
        mFans = (TextView) findViewById(R.id.search_up_fans);
        mArticle = (TextView) findViewById(R.id.search_up_artical);
        mAttention = (TextView) findViewById(R.id.search_up_follow);
        mDescription = (TextView) findViewById(R.id.search_up_description);
        mLocation = (TextView) findViewById(R.id.search_up_place);
        mBirth = (TextView) findViewById(R.id.search_up_birth);
        btnAtt = (Button) findViewById(R.id.btnSearchAttention);
        mFace = (SimpleDraweeView) findViewById(R.id.search_up_face);
        attentions = new ArrayList<>();
        Cursor cursor = getCur();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.suggestion_item, cursor,
                new String[]{"adv"}, new int[]{R.id.search_suggestion_tv}, 0);
        mSearchView.setSuggestionsAdapter(adapter);
        mSearchView.setOnQueryTextListener(this);
        btnAtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attentions.size() == 0) {
                    Snackbar.make(v, "这人谁也没关注!", Snackbar.LENGTH_SHORT).show();
                } else {
                    final String[] resource = new String[attentions.size()];
                    for (int i = 0; i < attentions.size(); i++) {
                        resource[i] = attentions.get(i);
                    }
                    new AlertDialog.Builder(FindUser.this).setTitle("选择关注者").setIcon(R.drawable.ic_get_av18dp).setItems(resource, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String taskurl="http://api.bilibili.cn/userinfo?mid="+resource[which];
                            new SearchTask().execute(taskurl);
                        }
                    }).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    public class SearchTask extends AsyncTask<String, Void, ResultBean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setTitle("寻找" + searchItem + "中...");
        }

        @Override
        protected void onPostExecute(ResultBean resultBean) {
            super.onPostExecute(resultBean);
            if (resultBean == null) {
                Toast.makeText(FindUser.this, "呜呜呜,好像没找到！", Toast.LENGTH_SHORT).show();
                setTitle("没找到呢...");
                return;
            }
            setTitle("已搜索到此人!");
            RendData(resultBean);
        }

        @Override
        protected ResultBean doInBackground(String... params) {
            ResultBean bean = new ResultBean();
            attentions.clear();
            try {
                InputStream is = new URL(params[0]).openStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String tmp = "";
                String result = "";
                while ((tmp = br.readLine()) != null) {
                    result += tmp;
                }
//                if(result.equals("")){
//                    return null;
//                }
                JSONObject json = new JSONObject(result);
                bean.up_name = json.getString("name");
                bean.up_id = json.getString("mid");
                bean.attention = json.getInt("attention");
                bean.birth = json.getString("birthday");
                bean.location = json.getString("place");
                bean.sex = json.getString("sex");
                bean.fans = json.getInt("fans");
                bean.face = json.getString("face");
                bean.article = json.getInt("article");
                bean.sign = json.getString("sign");
                if (json.getString("description").equals("")) {
                    bean.verti = "无认证信息";
                } else {
                    bean.verti = json.getString("description");
                }
                JSONArray jsonArray = json.getJSONArray("attentions");
                for (int i = 0; i < jsonArray.length(); i++) {
                    if(mTask.isCancelled()){
                        return null;
                    }
                    attentions.add(Integer.toString(jsonArray.getInt(i)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return bean;
        }
    }

    private void AddSuggestion(String adv) {
        mDataBase = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir() + "/suggestion.db", null);
        String query = "select * from suggestions";
        ContentValues cv = new ContentValues();
        Cursor res = mDataBase.query("suggestions", null, null, null, null, null, null);
        for (int i = 0; i < res.getCount(); i++) {
            if (res.getString(1).equals(adv)) {
                return;
            }
        }
        cv.put("adv", adv);
        mDataBase.insert("suggestions", null, cv);
        res = mDataBase.rawQuery(query, null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.suggestion_item, res,
                new String[]{"adv"}, new int[]{R.id.search_suggestion_tv}, 0);
        mSearchView.setSuggestionsAdapter(adapter);
    }

    private Cursor getCur() {
        mDataBase = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir() + "/suggestion.db", null);
        Cursor cur;
        try {
            String query = "select * from suggestions";
            cur = mDataBase.rawQuery(query, null);
            mDataBase.close();
        } catch (Exception e) {
            String sql = "create table suggestions (_id integer primary key autoincrement,adv text)";
            String query = "select * from suggestions";
            mDataBase.execSQL(sql);
            cur = mDataBase.rawQuery(query, null);
        }
        return cur;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mTask!=null && mTask.getStatus()== AsyncTask.Status.RUNNING){
            mTask.cancel(true);
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
