package com.example.finalapp;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

import nctu.fintech.appmate.Table;
import nctu.fintech.appmate.Tuple;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
    ViewPager pager;
    ArrayList<View> pagerList;

    Table mTable;
    String s1;
    ImageButton person4;
    ImageButton Taipei101Text;
    ImageButton GongGuanText;
    ImageButton ZooText;
    ImageButton EggText;
    ImageButton WaterText;
    ImageButton ChatText;
    ImageButton dcardsearch;
    ImageButton pen;

    ImageButton recommend1;
    ImageButton recommend2;
    ImageButton recommend3;
    ImageButton limited1;
    ImageButton limited2;

    EditText search_et;
    ImageButton search_bt;
    EditText search_et1;
    AutoScrollTextView runText;
    String username;
    TextView maintitle;
    TextView maintitle2;
    String currentlocation;

    //shake-ian
    private SensorManager mSensorManager;   //體感(Sensor)使用管理
    private Sensor mSensor;                 //體感(Sensor)類別
    private float mLastX;                    //x軸體感(Sensor)偏移
    private float mLastY;                    //y軸體感(Sensor)偏移
    private float mLastZ;                    //z軸體感(Sensor)偏移
    private double mSpeed;                 //甩動力道數度
    private long mLastUpdateTime;           //觸發時間
    //甩動力道數度設定值 (數值越大需甩動越大力，數值越小輕輕甩動即會觸發)
    private static final int SPEED_SHRESHOLD = 3000;
    //觸發間隔時間
    private static final int UPTATE_INTERVAL_TIME = 70;

    private Runnable r2 = new Runnable() {
        public void run() {
            try {
                Tuple tuple_get[] = mTable.get();
                s1 = "";
                for (int i = 0; i < tuple_get.length; i++) {
                    String tempString = tuple_get[i].get("Text");
                    String tempnameString = tuple_get[i].get("Name");
                    if (tempnameString == null || tempnameString.equals("")) {
                        tempnameString = "匿名";
                    }
                    if (tempString != null && !tempString.equals("")) {
                        s1 = tempnameString + ":" + tempString + "     " + s1;
                    }
                }
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            } catch (IOException e) {
                Log.e("Net", "Fail to get");
            }
        }
    };


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    runText.setText(s1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main2);

        Bundle bundle=getIntent().getExtras();
        username=bundle.getString("name");

        mTable = new Table("http://172.20.10.7:8000/api", "chatroom", "Secondteam", "secondteam12345");
        pager = (ViewPager) findViewById(R.id.pager);

        LayoutInflater li = getLayoutInflater().from(this);
        View v1 = li.inflate(R.layout.fragment_section1, null);
        View v2 = li.inflate(R.layout.fragment_section2, null);
        pagerList = new ArrayList<View>();
        pagerList.add(v1);
        pagerList.add(v2);

        pager.setAdapter(new myViewPagerAdapter(pagerList));
        pager.setCurrentItem(0);


        //section1
        search_bt = (ImageButton) v1.findViewById(R.id.iv_default1);
        search_bt.setOnTouchListener(touchlistener);
        search_et1 = (EditText) v1.findViewById(R.id.et_search1);
        maintitle=(TextView)v1.findViewById(R.id.MainTitle);
        maintitle.setText(username+" ! 你好");
        maintitle2=(TextView)v1.findViewById(R.id.MainTitle2);
        maintitle2.setText("你現在位於 國立臺灣大學綜合體育館");
        recommend1=v1.findViewById(R.id.recommend1);
        recommend1.setOnClickListener(this);
        recommend1.setOnTouchListener(touchlistener);
        recommend2=v1.findViewById(R.id.recommend2);
        recommend2.setOnClickListener(this);
        recommend2.setOnTouchListener(touchlistener);
        recommend3=v1.findViewById(R.id.recommend3);
        recommend3.setOnClickListener(this);
        recommend3.setOnTouchListener(touchlistener);
        limited1=v1.findViewById(R.id.limited1);
        limited1.setOnClickListener(this);
        limited1.setOnTouchListener(touchlistener);
        limited2=v1.findViewById(R.id.limited2);
        limited2.setOnClickListener(this);
        limited2.setOnTouchListener(touchlistener);

        search_bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_et1.getText().toString().equals("淡水")){
                    Intent searchIntent=new Intent(Main2Activity.this,SearchViewActivity.class);
                    searchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundleview=new Bundle();
                    bundleview.putString("username",username);
                    searchIntent.putExtras(bundleview);
                    search_et1.setText("");
                    startActivity(searchIntent);
                }else {
                    Intent searchmapIntent = new Intent(Main2Activity.this, MRTStationActivity.class);
                    searchmapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = new Bundle();
                    bundle.putString("key", search_et1.getText().toString());
                    bundle.putString("username", username);
                    searchmapIntent.putExtras(bundle);
                    search_et1.setText("");
                    startActivity(searchmapIntent);
                }
            }
        });

        person4 = (ImageButton) v2.findViewById(R.id.button_person4);
        person4.setOnClickListener(this);
        person4.setOnTouchListener(touchlistener);
        Taipei101Text = (ImageButton) v2.findViewById(R.id.button_Taipei101);
        Taipei101Text.setOnClickListener(this);
        Taipei101Text.setOnTouchListener(touchlistener);
        GongGuanText = (ImageButton) v2.findViewById(R.id.button_GongGuan);
        GongGuanText.setOnClickListener(this);
        GongGuanText.setOnTouchListener(touchlistener);
        ZooText = (ImageButton) v2.findViewById(R.id.button_Zoo);
        ZooText.setOnClickListener(this);
        ZooText.setOnTouchListener(touchlistener);
        EggText = (ImageButton) v2.findViewById(R.id.button_Egg);
        EggText.setOnClickListener(this);
        EggText.setOnTouchListener(touchlistener);
        WaterText = (ImageButton) v2.findViewById(R.id.button_Water);
        WaterText.setOnClickListener(this);
        WaterText.setOnTouchListener(touchlistener);
        ChatText = (ImageButton) v2.findViewById(R.id.chat_bt);
        ChatText.setOnClickListener(this);
        ChatText.setOnTouchListener(touchlistener);
        dcardsearch = (ImageButton) v2.findViewById(R.id.iv_default);
        dcardsearch.setOnClickListener(this);
        dcardsearch.setOnTouchListener(touchlistener);
        search_et = (EditText) v2.findViewById(R.id.et_search);
        runText = (AutoScrollTextView) v2.findViewById(R.id.runtext);
        runText.setMarqueeRepeatLimit(-1);
        Thread t1=new Thread (r2);
        t1.start();
        pen= (ImageButton) v2.findViewById(R.id.button_pen);
        pen.setOnClickListener(this);
        pen.setOnTouchListener(touchlistener);

        //shake-ian
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(SensorListener, mSensor,SensorManager.SENSOR_DELAY_GAME);

    }

    private SensorEventListener SensorListener= new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent mSensorEvent) {

            // 當前觸發時間
            long mCurrentUpdateTime = System.currentTimeMillis();
            // 觸發間隔時間 = 當前觸發時間 - 上次觸發時間
            long mTimeInterval = mCurrentUpdateTime - mLastUpdateTime;
            // 若觸發間隔時間< 70 則return;
            if (mTimeInterval < UPTATE_INTERVAL_TIME)
                return;

            mLastUpdateTime = mCurrentUpdateTime;

            // 取得xyz體感(Sensor)偏移
            float x = mSensorEvent.values[0];
            float y = mSensorEvent.values[1];
            float z = mSensorEvent.values[2];
            // 甩動偏移速度 = xyz體感(Sensor)偏移 - 上次xyz體感(Sensor)偏移
            float mDeltaX = x - mLastX;
            float mDeltaY = y - mLastY;
            float mDeltaZ = z - mLastZ;
            mLastX = x;
            mLastY = y;
            mLastZ = z;

            // 體感(Sensor)甩動力道速度公式
            mSpeed = Math.sqrt(mDeltaX * mDeltaX + mDeltaY * mDeltaY + mDeltaZ * mDeltaZ) / mTimeInterval * 10000;
            // 若體感(Sensor)甩動速度大於等於甩動設定值則進入 (達到甩動力道及速度)

            if (mSpeed >= SPEED_SHRESHOLD) {
                // 達到搖一搖甩動後要做的事情
                //Toast.makeText(getApplicationContext(), "shake", Toast.LENGTH_SHORT).show();
                Intent GiveRecommendationIntent = new Intent();
                GiveRecommendationIntent.setClass(Main2Activity.this, TamsuiActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                GiveRecommendationIntent.putExtras(bundle);
                startActivity(GiveRecommendationIntent);
            }
            else{
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(SensorListener);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        //用來儲存最後的辨識結果
        String firstMatch;
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //取出多個辨識結果並儲存在String的ArrayList中
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            firstMatch = (String) result.get(0);
            search_et1.setText(firstMatch);
        } else {
            firstMatch = "無法辨識";
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_Taipei101:
                Intent Taipei101Intent = new Intent(Main2Activity.this, Taipei101Activity.class);
                Taipei101Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(Taipei101Intent);
                //overridePendingTransition(0,0);
                break;
            case R.id.button_GongGuan:
                Intent GongGuanIntent = new Intent(Main2Activity.this, GongGuanActivity.class);
                GongGuanIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(GongGuanIntent);
                //overridePendingTransition(0,0);
                break;
            case R.id.button_Zoo:
                Intent ZooIntent = new Intent(Main2Activity.this, ZooActivity.class);
                ZooIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ZooIntent);
                //overridePendingTransition(0,0);
                break;
            case R.id.chat_bt:
                Intent chatIntent = new Intent(Main2Activity.this, ChatRoom.class);
                chatIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("Username",username);
                chatIntent.putExtras(bundle);
                startActivity(chatIntent);
                //overridePendingTransition(0,0);
                break;
            case R.id.button_person4:
                Intent personIntent = new Intent(Main2Activity.this, DcardPersonActivity.class);
                personIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(personIntent);
                break;
            case R.id.button_pen:
                Intent penIntent = new Intent(Main2Activity.this, PostWriteActivity.class);
                penIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundlepen=new Bundle();
                bundlepen.putString("Username",username);
                penIntent.putExtras(bundlepen);
                startActivity(penIntent);
                break;
            case R.id.recommend2:
                Intent recommendIntent = new Intent(Main2Activity.this, RecommendActivity.class);
                recommendIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle recommendbundle=new Bundle();
                recommendbundle.putString("Username",username);
                recommendIntent.putExtras(recommendbundle);
                startActivity(recommendIntent);
                break;
            case R.id.limited1:
                Intent timelimitedevent = new Intent(Main2Activity.this, TimeLimitEventActivity.class);
                timelimitedevent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(timelimitedevent);
        }
    }

    ImageButton.OnTouchListener touchlistener = new ImageButton.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Animation animation= AnimationUtils.loadAnimation(Main2Activity.this, R.anim.pressdown);
                switch(v.getId()){
                    case R.id.button_Taipei101:
                        Taipei101Text.startAnimation(animation);
                        break;
                    case R.id.button_GongGuan:
                        GongGuanText.startAnimation(animation);
                        break;
                    case R.id.button_Zoo:
                        ZooText.startAnimation(animation);
                        break;
                    case R.id.chat_bt:
                        ChatText.startAnimation(animation);
                        break;
                    case R.id.button_Egg:
                        EggText.startAnimation(animation);
                        break;
                    case R.id.button_Water:
                        WaterText.startAnimation(animation);
                        break;
                    case R.id.iv_default:
                        dcardsearch.startAnimation(animation);
                        break;
                    case R.id.iv_default1:
                        search_bt.startAnimation(animation);
                        break;
                    case R.id.button_person4:
                        person4.startAnimation(animation);
                        break;
                    case R.id.button_pen:
                        pen.startAnimation(animation);
                        break;
                    case R.id.recommend1:
                        recommend1.startAnimation(animation);
                        break;
                    case R.id.recommend2:
                        recommend2.startAnimation(animation);
                        break;
                    case R.id.recommend3:
                        recommend3.startAnimation(animation);
                        break;
                    case R.id.limited1:
                        limited1.startAnimation(animation);
                        break;
                    case R.id.limited2:
                        limited2.startAnimation(animation);
                        break;
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Animation animation= AnimationUtils.loadAnimation(Main2Activity.this, R.anim.pressup);
                switch(v.getId()){
                    case R.id.button_Taipei101:
                        Taipei101Text.startAnimation(animation);
                        break;
                    case R.id.button_GongGuan:
                        GongGuanText.startAnimation(animation);
                        break;
                    case R.id.button_Zoo:
                        ZooText.startAnimation(animation);
                        break;
                    case R.id.chat_bt:
                        ChatText.startAnimation(animation);
                        break;
                    case R.id.button_Egg:
                        EggText.startAnimation(animation);
                        break;
                    case R.id.button_Water:
                        WaterText.startAnimation(animation);
                        break;
                    case R.id.iv_default:
                        dcardsearch.startAnimation(animation);
                        break;
                    case R.id.iv_default1:
                        search_bt.startAnimation(animation);
                        break;
                    case R.id.button_person4:
                        person4.startAnimation(animation);
                        break;
                    case R.id.button_pen:
                        pen.startAnimation(animation);
                        break;
                    case R.id.recommend1:
                        recommend1.startAnimation(animation);
                        break;
                    case R.id.recommend2:
                        recommend2.startAnimation(animation);
                        break;
                    case R.id.recommend3:
                        recommend3.startAnimation(animation);
                        break;
                    case R.id.limited1:
                        limited1.startAnimation(animation);
                        break;
                    case R.id.limited2:
                        limited2.startAnimation(animation);
                        break;
                }
            }
            return false;
        }
    };
}
