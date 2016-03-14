package com.loopcom.ccoderad.biliinformation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @ViewById(R.id.card_1)
    CardView cd_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
    }

    @Click
    void card_1() {
        startActivity(new Intent(this, AnimeInfo.class));
    }

    @Click
    void card_2() {
        startActivity(new Intent(this, WeekRank.class));
    }

    @Click
    void card_3() {
        startActivity(new Intent(this, live_list.class));
    }

    @Click
    void card_4() {
        startActivity(new Intent(this, FindUser.class));
    }
    @Click
    void card_5(){
        startActivity(new Intent(this,AnimeIndex.class));
    }
//
//    private long time = 0;
//
//    @Override
//    public void onBackPressed() {
//        long i = System.currentTimeMillis();
//        if (time - i > 2000) {
//            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
//            time = System.currentTimeMillis();
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            long i = System.currentTimeMillis();
//            if (time - i > 2000) {
//                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
//                time = System.currentTimeMillis();
//                return true;
//            } else {
//                return super.onKeyDown(keyCode, event);
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
