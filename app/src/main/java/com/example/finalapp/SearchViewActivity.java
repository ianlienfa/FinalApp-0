package com.example.finalapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class SearchViewActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    Button tag1;
    Button tag2;
    Button tag3;
    Button tag4;
    Button tag5;
    ImageButton add1;
    ImageButton add2;
    ImageButton add3;
    ImageButton navigate;
    String username;
    ViewPager pager;
    ArrayList<View> pagerList;

    private GoogleMap mMap;

    LatLng nowPos1 = new LatLng(25.167761, 121.445679);
    LatLng flowerPos=new LatLng(25.071031, 121.524948);
    LatLng pos2=new LatLng(25.137880, 121.486878);

    ImageView image1;
    ImageView image2;
    TextView viewname1;
    TextView viewname2;
    TextView viewlocation1;
    TextView viewlocation2;
    TextView viewtext1;
    TextView viewtext2;
    ImageView star1;
    ImageView star2;
    ImageButton ok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        Bundle bundle=getIntent().getExtras();
        username=bundle.getString("username");
        pager = (ViewPager) findViewById(R.id.pager);

        LayoutInflater li = getLayoutInflater().from(this);
        View v1 = li.inflate(R.layout.fragment_map, null);
        View v2 = li.inflate(R.layout.fragment_viewrecommend, null);
        pagerList = new ArrayList<View>();
        pagerList.add(v1);
        pagerList.add(v2);

        pager.setAdapter(new myViewPagerAdapter(pagerList));
        pager.setCurrentItem(1);
        tag1=(Button)v2.findViewById(R.id.tamsuitag1);
        tag1.setOnTouchListener(touchlistener);
        tag2=(Button)v2.findViewById(R.id.tamsuitag2);
        tag2.setOnTouchListener(touchlistener);
        tag2.setOnClickListener(this);
        tag3=(Button)v2.findViewById(R.id.tamsuitag3);
        tag3.setOnTouchListener(touchlistener);
        tag4=(Button)v2.findViewById(R.id.tamsuitag4);
        tag4.setOnTouchListener(touchlistener);
        tag4.setOnClickListener(this);
        tag5=(Button)v2.findViewById(R.id.tamsuitag5);
        tag5.setOnTouchListener(touchlistener);
        navigate=(ImageButton)v1.findViewById(R.id.navigate);
        navigate.setOnTouchListener(touchlistener);

        add1=(ImageButton)v2.findViewById(R.id.view_add1);
        add1.setOnTouchListener(touchlistener);
        add1.setOnClickListener(this);
        add2=(ImageButton)v2.findViewById(R.id.view_add2);
        add2.setOnTouchListener(touchlistener);
        add2.setOnClickListener(this);
        add3=(ImageButton)v2.findViewById(R.id.view_add3);
        add3.setOnTouchListener(touchlistener);
        add3.setOnClickListener(this);

        image1=v2.findViewById(R.id.tamsuiview1);
        image2=v2.findViewById(R.id.tamsuiview2);
        viewname1=v2.findViewById(R.id.tamsuiviewname1);
        viewname2=v2.findViewById(R.id.tamsuiviewname2);
        viewlocation1=v2.findViewById(R.id.tamsuiviewlocation1);
        viewlocation2=v2.findViewById(R.id.tamsuiviewlocation2);
        viewtext1=v2.findViewById(R.id.tamsuiviewtext1);
        viewtext2=v2.findViewById(R.id.tamsuiviewtext2);
        star1=v2.findViewById(R.id.tamsuistar1);
        star2=v2.findViewById(R.id.tamsuistar2);
        ok=v2.findViewById(R.id.ok);
        ok.setOnTouchListener(touchlistener);


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

        ok.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.view_add1:
                mMap.addMarker(new MarkerOptions().position(flowerPos).title("花博公園"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(flowerPos, 10));
                Toast.makeText(SearchViewActivity.this, "已加入 花博公園", Toast.LENGTH_SHORT).show();
                break;
            case R.id.view_add2:
                mMap.addMarker(new MarkerOptions().position(pos2).title("落雨松小徑"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos2, 10));
                Toast.makeText(SearchViewActivity.this, "已加入 落雨松小徑", Toast.LENGTH_SHORT).show();
                break;
            case R.id.view_add3:
                mMap.addMarker(new MarkerOptions().position(nowPos1).title("淡水老街"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPos1, 10));
                Toast.makeText(SearchViewActivity.this, "已加入 淡水老街", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tamsuitag2:
                image1.setImageResource(R.drawable.tamsui3);
                image2.setImageResource(R.drawable.tamsui2);
                viewname1.setText("花博公園");
                viewname2.setText("落雨松小徑");
                viewlocation1.setText("圓山站1號出口");
                viewlocation2.setText("復興崗站");
                viewtext1.setText("#自然 #親子");
                viewtext2.setText("#自然 #在地阿嬤在地推薦");
                star1.setImageResource(R.drawable.star3);
                star2.setImageResource(R.drawable.star4);
                break;
            case R.id.tamsuitag4:
                image1.setImageResource(R.drawable.taipeistation);
                image2.setImageResource(R.drawable.beitouimage);
                viewname1.setText("台北車站商圈");
                viewname2.setText("北投溫泉");
                viewlocation1.setText("台北車站");
                viewlocation2.setText("北投站");
                viewtext1.setText("#休閒 #吃貨");
                viewtext2.setText("#親子 #休閒");
                star1.setImageResource(R.drawable.star4);
                star2.setImageResource(R.drawable.star5);
                break;
        }
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
                    case R.id.tamsuitag1:
                        tag1.startAnimation(animation);
                        break;
                    case R.id.tamsuitag2:
                        tag2.startAnimation(animation);
                        break;
                    case R.id.tamsuitag3:
                        tag3.startAnimation(animation);
                        break;
                    case R.id.tamsuitag4:
                        tag4.startAnimation(animation);
                        break;
                    case R.id.tamsuitag5:
                        tag5.startAnimation(animation);
                        break;
                    case R.id.view_add1:
                        add1.startAnimation(animation);
                        break;
                    case R.id.view_add2:
                        add2.startAnimation(animation);
                        break;
                    case R.id.view_add3:
                        add3.startAnimation(animation);
                        break;
                    case R.id.ok:
                        ok.startAnimation(animation);
                        break;

                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Animation animation= AnimationUtils.loadAnimation(SearchViewActivity.this, R.anim.pressup);
                switch(v.getId()){
                    case R.id.tamsuitag1:
                        tag1.startAnimation(animation);
                        break;
                    case R.id.tamsuitag2:
                        tag2.startAnimation(animation);
                        break;
                    case R.id.tamsuitag3:
                        tag3.startAnimation(animation);
                        break;
                    case R.id.tamsuitag4:
                        tag4.startAnimation(animation);
                        break;
                    case R.id.tamsuitag5:
                        tag5.startAnimation(animation);
                        break;
                    case R.id.view_add1:
                        add1.startAnimation(animation);
                        break;
                    case R.id.view_add2:
                        add2.startAnimation(animation);
                        break;
                    case R.id.view_add3:
                        add3.startAnimation(animation);
                        break;
                    case R.id.ok:
                        ok.startAnimation(animation);
                        break;
                }

            }
            return false;
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        // 取得最後得知道provider資訊

// 得知GPS位置時，根據取得的經緯度標上 marker
        LatLng nowPos=new LatLng(25.021709, 121.535296);
        mMap.addMarker(new MarkerOptions().position(nowPos1).title("淡水老街"));
        mMap.addMarker(new MarkerOptions().position(nowPos).title(username));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPos, 10));

    }

}
