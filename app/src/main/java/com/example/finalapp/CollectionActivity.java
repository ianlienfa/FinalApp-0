package com.example.finalapp;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {
    ViewPager pager;
    ArrayList<View> pagerList;

    ImageButton collect1;
    ImageButton collect2;
    ImageButton collect3;
    ImageButton collect4;
    ImageButton collect5;
    TextView nametitle;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        Bundle bundle=getIntent().getExtras();
        username=bundle.getString("name");

        nametitle=(TextView)findViewById(R.id.tv1);
        nametitle.setText("嗨 ! "+username);

        collect1=(ImageButton)findViewById(R.id.collect1);
        collect1.setOnTouchListener(touchlistener);
        collect2=(ImageButton)findViewById(R.id.collect2);
        collect2.setOnTouchListener(touchlistener);
        collect3=(ImageButton)findViewById(R.id.collect3);
        collect3.setOnTouchListener(touchlistener);
        collect4=(ImageButton)findViewById(R.id.collect4);
        collect4.setOnTouchListener(touchlistener);
        collect5=(ImageButton)findViewById(R.id.collect5);
        collect5.setOnTouchListener(touchlistener);

        collect3.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent BeitouIntent = new Intent(CollectionActivity.this, BeitouActivity.class);
                BeitouIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundlebei=new Bundle();
                bundlebei.putBoolean("Write",false);
                BeitouIntent.putExtras(bundlebei);
                startActivity(BeitouIntent);
            }
        });

    }

    ImageButton.OnTouchListener touchlistener = new ImageButton.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                switch(v.getId()){
                    case R.id.collect1:
                        Animation animation= AnimationUtils.loadAnimation(CollectionActivity.this, R.anim.pressdown);
                        collect1.startAnimation(animation);
                        break;
                    case R.id.collect2:
                        Animation animation2= AnimationUtils.loadAnimation(CollectionActivity.this, R.anim.pressdown);
                        collect2.startAnimation(animation2);
                        break;
                    case R.id.collect3:
                        Animation animation3= AnimationUtils.loadAnimation(CollectionActivity.this, R.anim.pressdown);
                        collect3.startAnimation(animation3);
                        break;
                    case R.id.collect4:
                        Animation animation4= AnimationUtils.loadAnimation(CollectionActivity.this, R.anim.pressdown);
                        collect4.startAnimation(animation4);
                        break;
                    case R.id.collect5:
                        Animation animation5= AnimationUtils.loadAnimation(CollectionActivity.this, R.anim.pressdown);
                        collect5.startAnimation(animation5);
                        break;
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                switch(v.getId()){
                    case R.id.collect1:
                        Animation animation= AnimationUtils.loadAnimation(CollectionActivity.this, R.anim.pressup);
                        collect1.startAnimation(animation);
                        break;
                    case R.id.collect2:
                        Animation animation2= AnimationUtils.loadAnimation(CollectionActivity.this, R.anim.pressup);
                        collect2.startAnimation(animation2);
                        break;
                    case R.id.collect3:
                        Animation animation3= AnimationUtils.loadAnimation(CollectionActivity.this, R.anim.pressup);
                        collect3.startAnimation(animation3);
                        break;
                    case R.id.collect4:
                        Animation animation4= AnimationUtils.loadAnimation(CollectionActivity.this, R.anim.pressup);
                        collect4.startAnimation(animation4);
                        break;
                    case R.id.collect5:
                        Animation animation5= AnimationUtils.loadAnimation(CollectionActivity.this, R.anim.pressup);
                        collect5.startAnimation(animation5);
                        break;
                }

            }
            return false;
        }
    };
}
