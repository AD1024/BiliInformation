package com.loopcom.ccoderad.biliinformation;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class detiails_rank extends AppCompatActivity {

    private TextView author;
    private TextView Title;
    private TextView play;
    private TextView fav;
    private TextView comment;
    private TextView date;
    ImageView img;
    private TextView type;
    private TextView description;
    private LayoutInflater mInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detiails_rank);
        Intent i =getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final RankBean result = (RankBean) i.getSerializableExtra("Data");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) detiails_rank.this.getSystemService(detiails_rank.this.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("AVNum",result.av);
                clipboardManager.setPrimaryClip(clipData);
                Snackbar.make(view, "已复制AV号", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mInflater = LayoutInflater.from(detiails_rank.this);
        View v = findViewById(R.id.include_rank);
        author = (TextView) v.findViewById(R.id.info_up_name);
        Title = (TextView) v.findViewById(R.id.info_title);
        play = (TextView) v.findViewById(R.id.info_play);
        fav = (TextView) v.findViewById(R.id.info_fav);
        type= (TextView) v.findViewById(R.id.info_type);
//        img = (ImageView) v.findViewById(R.id.rank_info_img);

        comment = (TextView) v.findViewById(R.id.info_comment);
        type.setText(result.type);
        date = (TextView) v.findViewById(R.id.info_up_date);
        description = (TextView) v.findViewById(R.id.info_description);
        Log.i("rinfo", result.author);
        author.setText(result.author);
        Title.setText(result.Title);
        play.setText(Integer.toString(result.play));
        fav.setText(Integer.toString(result.fav));
        setTitle(result.Title);
        comment.setText(Integer.toString(result.comment));
        date.setText(result.createDate);
        description.setText(result.description);
//        InfoImageLoader infoImageLoader = new InfoImageLoader();
//        infoImageLoader.startLoad(result.pic,img);
    }
}
