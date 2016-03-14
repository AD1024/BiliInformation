package com.loopcom.ccoderad.biliinformation;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopcom.ccoderad.biliinformation.Beans.RankBean;
import com.loopcom.ccoderad.biliinformation.utils.InfoImageLoader;

import java.util.Random;

public class detiails_rank extends AppCompatActivity implements View.OnLongClickListener, View.OnTouchListener {

    private TextView author;
    private TextView Title;
    private TextView play;
    private TextView fav;
    private TextView comment;
    private TextView date;
    ImageView img;
    private TextView type;
    private TextView description;
    private CardView mCardview;
    private LayoutInflater mInflater;
    private ClipData clipData;
    private ClipboardManager clipboardManager;
    private Random random;
    InfoImageLoader mInfoImageLoader;
    private CollapsingToolbarLayout mToolBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detiails_rank);
        mInfoImageLoader = new InfoImageLoader();
        Intent i =getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        toolbar.setSoundEffectsEnabled(true);
        random = new Random();
        int mR =getColor();
        int G =getColor();
        int B =getColor();
        mToolBarLayout.setBackgroundColor(Color.rgb(mR,G,B));
        final RankBean result = (RankBean) i.getSerializableExtra("Data");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundColor(Color.rgb(mR-45,G-45,B-45));
        fab.setOnLongClickListener(this);
        clipboardManager = (ClipboardManager) detiails_rank.this.getSystemService(detiails_rank.this.CLIPBOARD_SERVICE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipData = ClipData.newPlainText("AVNum",result.av);
                clipboardManager.setPrimaryClip(clipData);
                Snackbar.make(view, "已复制AV号", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mInflater = LayoutInflater.from(detiails_rank.this);
        View v = findViewById(R.id.include_rank);
        author = (TextView) v.findViewById(R.id.info_up_name);
        mCardview = (CardView) findViewById(R.id.info_description_card);
        Title = (TextView) v.findViewById(R.id.info_title);
        play = (TextView) v.findViewById(R.id.info_play);
        fav = (TextView) v.findViewById(R.id.info_fav);
        type= (TextView) v.findViewById(R.id.info_type);
        //InitImg
        img = (ImageView) v.findViewById(R.id.info_detail_img);
        img.setTag(result.pic);
        mInfoImageLoader.startLoad(result.pic,img);
        comment = (TextView) v.findViewById(R.id.info_comment);
        type.setText(result.type);
        date = (TextView) v.findViewById(R.id.info_up_date);
        description = (TextView) v.findViewById(R.id.info_description);
//        Log.i("rinfo", result.author);
        author.setText(result.author);
        Title.setText(result.Title);
        play.setText(Integer.toString(result.play));
        fav.setText(Integer.toString(result.fav));
        setTitle(result.Title);
        comment.setText(Integer.toString(result.comment));
        date.setText(result.createDate);
        description.setText(result.description);
        mCardview.setOnLongClickListener(this);
        mCardview.setOnTouchListener(this);
    }

    private int getColor(){
        return Math.abs(random.nextInt())%255;
    }

    @Override
    public boolean onLongClick(View v) {
        startActivity(new Intent(this,about_me.class));
        return false;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            mCardview.setRadius(10);
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            mCardview.setRadius(4);
        }
        return false;
    }
}
