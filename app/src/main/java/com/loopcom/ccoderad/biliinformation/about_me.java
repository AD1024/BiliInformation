package com.loopcom.ccoderad.biliinformation;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class about_me extends AppCompatActivity {


    private TextView mName;
    private TextView mDescription;
    private SimpleDraweeView mAvatar;
    private InfoFecher fecher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_about_me);
        setTitle("关于作者");
        mName = (TextView) findViewById(R.id.text_my_name);
        mDescription = (TextView) findViewById(R.id.text_my_info);
        mAvatar = (SimpleDraweeView) findViewById(R.id.my_avatar);
        fecher = new InfoFecher();
        fecher.execute("http://api.bilibili.cn/userinfo?user=AD1024");
    }

    public void RendData(MyDataBean bean){
        mName.setText(bean.Name);
        mDescription.setText(bean.Description);
        Uri uri = Uri.parse(bean.URl);
        mAvatar.setImageURI(uri);
    }

    class MyDataBean{
        public String Name;
        public String Description;
        public String URl;
        public boolean successful;
        public MyDataBean(){
            successful=false;
        }
    }

    class InfoFecher extends AsyncTask<String,Void,MyDataBean>{

        @Override
        protected MyDataBean doInBackground(String... params) {
            MyDataBean bean=new MyDataBean();
            try {
                InputStream is = new URL(params[0]).openStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String jsonString = "";
                String result="";
                while((jsonString=br.readLine())!=null){
                    result+=jsonString;
                }
                JSONObject jsonObject = new JSONObject(result);
                bean.Name = jsonObject.getString("name");
                bean.Description = jsonObject.getString("sign");
                bean.URl = jsonObject.getString("face");
                bean.successful=true;
                return bean;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MyDataBean myDataBean) {
            super.onPostExecute(myDataBean);
            RendData(myDataBean);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(fecher!=null && fecher.getStatus()== AsyncTask.Status.RUNNING){
            fecher.cancel(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(fecher!=null && fecher.getStatus()== AsyncTask.Status.RUNNING){
            fecher.cancel(true);
        }
    }
}
