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
import android.widget.ImageView;

import java.util.ArrayList;

public class SearchViewActivity extends AppCompatActivity {
    Button tag1;
    Button tag2;
    Button tag3;
    Button tag4;
    Button tag5;
    ImageView route;
    ImageButton navigate;
    String username;
    ViewPager pager;
    ArrayList<View> pagerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        Bundle bundle=getIntent().getExtras();
        username=bundle.getString("username");
        pager = (ViewPager) findViewById(R.id.pager);

        LayoutInflater li = getLayoutInflater().from(this);
        View v1 = li.inflate(R.layout.search_mrtview, null);
        View v2 = li.inflate(R.layout.fragment_viewrecommend, null);
        pagerList = new ArrayList<View>();
        pagerList.add(v1);
        pagerList.add(v2);

        pager.setAdapter(new myViewPagerAdapter(pagerList));
        pager.setCurrentItem(0);
        tag1=(Button)v1.findViewById(R.id.watertag1);
        tag1.setOnTouchListener(touchlistener);
        tag2=(Button)v1.findViewById(R.id.watertag2);
        tag2.setOnTouchListener(touchlistener);
        tag3=(Button)v1.findViewById(R.id.watertag3);
        tag3.setOnTouchListener(touchlistener);
        tag4=(Button)v1.findViewById(R.id.watertag4);
        tag4.setOnTouchListener(touchlistener);
        tag5=(Button)v1.findViewById(R.id.watertag5);
        tag5.setOnTouchListener(touchlistener);
        navigate=(ImageButton)v1.findViewById(R.id.navigatewater);
        navigate.setOnTouchListener(touchlistener);
        route=(ImageView)v1.findViewById(R.id.waterMRT);

        tag2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                route.setImageResource(R.drawable.map2);
            }
        });

        tag3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                route.setImageResource(R.drawable.map);
            }
        });

        navigate.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(SearchViewActivity.this, TamsuiActivity.class);
                Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle1 = new Bundle();
                bundle1.putString("username", username);
                Intent.putExtras(bundle1);
                startActivity(Intent);
            }
        });

    }

    public class myViewPagerAdapter extends PagerAdapter {
        private ArrayList<View> mListViews;

        public myViewPagerAdapter(ArrayList<View> mListViews){
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object){
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position){
            View view = mListViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    ImageButton.OnTouchListener touchlistener = new ImageButton.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Animation animation= AnimationUtils.loadAnimation(SearchViewActivity.this, R.anim.pressdown);
                switch(v.getId()){
                    case R.id.watertag1:
                        tag1.startAnimation(animation);
                        break;
                    case R.id.watertag2:
                        tag2.startAnimation(animation);
                        break;
                    case R.id.watertag3:
                        tag3.startAnimation(animation);
                        break;
                    case R.id.watertag4:
                        tag4.startAnimation(animation);
                        break;
                    case R.id.watertag5:
                        tag5.startAnimation(animation);
                        break;
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Animation animation= AnimationUtils.loadAnimation(SearchViewActivity.this, R.anim.pressup);
                switch(v.getId()){
                    case R.id.watertag1:
                        tag1.startAnimation(animation);
                        break;
                    case R.id.watertag2:
                        tag2.startAnimation(animation);
                        break;
                    case R.id.watertag3:
                        tag3.startAnimation(animation);
                        break;
                    case R.id.watertag4:
                        tag4.startAnimation(animation);
                        break;
                    case R.id.watertag5:
                        tag5.startAnimation(animation);
                        break;
                }

            }
            return false;
        }
    };
}
