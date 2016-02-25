package com.loopcom.ccoderad.biliinformation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;

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
    void card_1(){
        startActivity(new Intent(this, AnimeInfo.class));
    }
    @Click
    void card_2(){
        startActivity(new Intent(this,WeekRank.class));
    }
}
