package com.example.finalapp;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.PendingIntent;
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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

import nctu.fintech.appmate.Table;
import nctu.fintech.appmate.Tuple;

public class TamsuiActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    ViewPager pager;
    ArrayList<View> pagerList;

    private GoogleMap mMap;

    private BluetoothAdapter mBluetoothAdapter;
    private List<String> bluetoothdeviceslist = new ArrayList<String>();
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private String ID_target = "BR517484";
    double dis;
    boolean foundchat = false;
    boolean found = false;
    boolean foundruntext = false;
    Table mTable;
    String s1;
    AutoScrollTextView runText;
    ImageButton Taipei101Text;
    ImageButton GongGuanText;
    ImageButton ZooText;
    ImageButton EggText;
    ImageButton WaterText;
    ImageButton ChatText;
    ImageButton person4;
    ImageButton dcardsearch;
    ImageButton camera;
    ImageButton plus1;
    ImageButton plus2;
    ImageButton plus3;
    ImageButton question;
    ImageButton stampcollect;
    Button maptag1;
    Button maptag2;
    Button maptag3;
    Button maptag4;
    Button maptag5;
    EditText search_et;
    ImageButton search_bt;
    ImageView stamp1;
    ImageView stamp2;
    ImageView stamp3;
    ImageView arstamp;
    boolean foundstamp1=false;
    boolean foundstamp2=false;
    boolean foundstamp3=false;
    private NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;
    ImageButton navigate;
    String MRTname;
    String username;
    ImageView map;
    LatLng nowPos1;
    LatLng flowerPos;
    LatLng pos2;
    ImageButton place1;
    ImageButton place2;
    ImageButton place3;
    TextView placetext1;
    TextView placetext2;
    TextView placetext3;

    LinearLayout info;
    LinearLayout infolinear2;
    TextView currentlocation;
    TextView goal;
    TextView infoline1;
    TextView infoline2;
    TextView stop1;
    TextView stop2;
    TextView stopnum;
    TextView stopnum2;
    TextView exit;
    ImageButton closebutton;

    LinearLayout unlocked;
    ImageButton knowbutton;
    int collectcount=0;
    ImageView mrtview;

    //ian's new things starts from here
    ImageView first_nfc_alert_vis;
    //這個是故事黑黑的背景
    TextView place_name1_vis;
    ScrollView scrollView_for_nfc1_vis;
    //兩個小男孩
    ImageView cuteboy_nfc_vis1;
    ImageView cuteboy_nfc_vis2;
    Button story_closebutton1;


    private TimerTask task = new TimerTask() {
        public void run() {
            Thread t2 = new Thread(r2);
            t2.start();
        }
    };

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
        setContentView(R.layout.activity_tamsui);

        Bundle bundle=getIntent().getExtras();
        username=bundle.getString("username");

        mTable = new Table("http://172.20.10.7:8000/api", "chatroom", "Secondteam", "secondteam12345");

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            //nfc not support your device.
            return;
        }
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        pager = (ViewPager) findViewById(R.id.pager);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBluetoothPermission();
        SearchBluetooth();
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();

        LayoutInflater li = getLayoutInflater().from(this);
        View v1 = li.inflate(R.layout.fragment_stamp, null);
        View v2 = li.inflate(R.layout.fragment_map2, null);
        View v3 = li.inflate(R.layout.fragment_section2, null);
        pagerList = new ArrayList<View>();
        pagerList.add(v1);
        pagerList.add(v2);
        pagerList.add(v3);

        pager.setAdapter(new myViewPagerAdapter(pagerList));
        pager.setCurrentItem(1);

        //ian's new thing starts from here
        first_nfc_alert_vis  = v1.findViewById(R.id.first_nfc_alert_vis);
        place_name1_vis = v1.findViewById(R.id.place_name1_vis);
        scrollView_for_nfc1_vis = v1.findViewById(R.id.scrollView_for_nfc1_vis);
        cuteboy_nfc_vis1 = v1.findViewById(R.id.cuteboy_nfc_vis1);
        cuteboy_nfc_vis2 = v1.findViewById(R.id.cuteboy_nfc_vis2);
        story_closebutton1 = v1.findViewById(R.id.story_closebutton1);
        //stops here

        //stamp
        unlocked=v1.findViewById(R.id.unlockedgroup);
        knowbutton=v1.findViewById(R.id.knowbutton);
        knowbutton.setOnClickListener(this);
        knowbutton.setOnTouchListener(touchlistener);
        stamp1 = (ImageView) v1.findViewById(R.id.stamp1);
        stamp2 = (ImageView) v1.findViewById(R.id.stamp2);
        stamp3 = (ImageView) v1.findViewById(R.id.stamp3);
        arstamp=(ImageView)v1.findViewById(R.id.arstamp);
        camera=(ImageButton)v1.findViewById(R.id.camera);
        camera.setOnClickListener(this);
        camera.setOnTouchListener(touchlistener);
        plus1=(ImageButton)v1.findViewById(R.id.plus1);
        plus1.setOnClickListener(this);
        plus1.setOnTouchListener(touchlistener);
        plus2=(ImageButton)v1.findViewById(R.id.plus2);
        plus2.setOnClickListener(this);
        plus2.setOnTouchListener(touchlistener);
        plus3=(ImageButton)v1.findViewById(R.id.plus3);
        plus3.setOnClickListener(this);
        plus3.setOnTouchListener(touchlistener);
        question=(ImageButton)v1.findViewById(R.id.button_question);
        question.setOnClickListener(this);
        question.setOnTouchListener(touchlistener);
        stampcollect=(ImageButton)v1.findViewById(R.id.stampcollect);
        stampcollect.setOnClickListener(this);
        stampcollect.setOnTouchListener(touchlistener);
        person4 = (ImageButton) v3.findViewById(R.id.button_person4);
        person4.setOnClickListener(this);
        person4.setOnTouchListener(touchlistener);


        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //map
        place1=v2.findViewById(R.id.place1);
        place1.setOnClickListener(this);
        place1.setOnTouchListener(touchlistener);
        place2=v2.findViewById(R.id.place2);
        place2.setOnClickListener(this);
        place2.setOnTouchListener(touchlistener);
        place3=v2.findViewById(R.id.place3);
        place3.setOnClickListener(this);
        place3.setOnTouchListener(touchlistener);
        placetext1=v2.findViewById(R.id.placetext1);
        placetext2=v2.findViewById(R.id.placetext2);
        placetext3=v2.findViewById(R.id.placetext3);
        placetext1.setText("花博公園");
        placetext2.setText("落雨松小徑");
        placetext3.setText("淡水老街");
        info=v2.findViewById(R.id.information);
        currentlocation=v2.findViewById(R.id.currentlocation);
        goal=v2.findViewById(R.id.goal);
        infoline1=v2.findViewById(R.id.infoline1);
        infoline2=v2.findViewById(R.id.infoline2);
        stopnum=v2.findViewById(R.id.stopnum);
        stopnum2=v2.findViewById(R.id.stopnum2);
        stop1=v2.findViewById(R.id.stop1);
        stop2=v2.findViewById(R.id.stop2);
        infolinear2=v2.findViewById(R.id.infolinear2);
        closebutton=v2.findViewById(R.id.infoclose_bt);
        closebutton.setOnClickListener(this);
        closebutton.setOnTouchListener(touchlistener);
        mrtview=v2.findViewById(R.id.mrtview);
        exit=v2.findViewById(R.id.infoexit);

        //section2
        Taipei101Text = (ImageButton) v3.findViewById(R.id.button_Taipei101);
        Taipei101Text.setOnClickListener(this);
        Taipei101Text.setOnTouchListener(touchlistener);
        GongGuanText = (ImageButton) v3.findViewById(R.id.button_GongGuan);
        GongGuanText.setOnClickListener(this);
        GongGuanText.setOnTouchListener(touchlistener);
        ZooText = (ImageButton) v3.findViewById(R.id.button_Zoo);
        ZooText.setOnClickListener(this);
        ZooText.setOnTouchListener(touchlistener);
        EggText = (ImageButton) v3.findViewById(R.id.button_Egg);
        EggText.setOnClickListener(this);
        EggText.setOnTouchListener(touchlistener);
        WaterText = (ImageButton) v3.findViewById(R.id.button_Water);
        WaterText.setOnClickListener(this);
        WaterText.setOnTouchListener(touchlistener);
        ChatText = (ImageButton) v3.findViewById(R.id.chat_bt);
        ChatText.setOnClickListener(this);
        ChatText.setOnTouchListener(touchlistener);
        dcardsearch = (ImageButton) v3.findViewById(R.id.iv_default);
        dcardsearch.setOnClickListener(this);
        dcardsearch.setOnTouchListener(touchlistener);
        search_et = (EditText) v3.findViewById(R.id.et_search);
        runText = (AutoScrollTextView) v3.findViewById(R.id.runtext);
        runText.setMarqueeRepeatLimit(-1);



    }

    private void delay(int ms){
        try {
            Thread.currentThread();
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        nowPos1 = new LatLng(25.167761, 121.445679);
        flowerPos=new LatLng(25.071031, 121.524948);
        pos2=new LatLng(25.137880, 121.486878);
        mMap.addMarker(new MarkerOptions().position(flowerPos).title("花博公園"));
        mMap.addMarker(new MarkerOptions().position(pos2).title("落雨松小徑"));
        mMap.addMarker(new MarkerOptions().position(nowPos1).title("淡水老街"));
        mMap.addMarker(new MarkerOptions().position(flowerPos).title(username));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(flowerPos, 10));
    }

    ImageButton.OnTouchListener touchlistener = new ImageButton.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Animation animation = AnimationUtils.loadAnimation(TamsuiActivity.this, R.anim.pressdown);
                switch (v.getId()) {
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
                    case R.id.camera:
                        camera.startAnimation(animation);
                        break;
                    case R.id.plus1:
                        plus1.startAnimation(animation);
                        break;
                    case R.id.plus2:
                        plus2.startAnimation(animation);
                        break;
                    case R.id.plus3:
                        plus3.startAnimation(animation);
                        break;
                    case R.id.button_question:
                        question.startAnimation(animation);
                        break;
                    case R.id.stampcollect:
                        stampcollect.startAnimation(animation);
                        break;
                    case R.id.button_person4:
                        person4.startAnimation(animation);
                        break;
                    case R.id.place1:
                        place1.startAnimation(animation);
                        break;
                    case R.id.place2:
                        place2.startAnimation(animation);
                        break;
                    case R.id.place3:
                        place3.startAnimation(animation);
                        break;
                    case R.id.knowbutton:
                        knowbutton.startAnimation(animation);
                        break;
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Animation animation = AnimationUtils.loadAnimation(TamsuiActivity.this, R.anim.pressup);
                switch (v.getId()) {
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
                    case R.id.camera:
                        camera.startAnimation(animation);
                        break;
                    case R.id.plus1:
                        plus1.startAnimation(animation);
                        break;
                    case R.id.plus2:
                        plus2.startAnimation(animation);
                        break;
                    case R.id.plus3:
                        plus3.startAnimation(animation);
                        break;
                    case R.id.button_question:
                        question.startAnimation(animation);
                        break;
                    case R.id.stampcollect:
                        stampcollect.startAnimation(animation);
                        break;
                    case R.id.button_person4:
                        person4.startAnimation(animation);
                        break;
                    case R.id.place1:
                        place1.startAnimation(animation);
                        break;
                    case R.id.place2:
                        place2.startAnimation(animation);
                        break;
                    case R.id.place3:
                        place3.startAnimation(animation);
                        break;
                    case R.id.knowbutton:
                        knowbutton.startAnimation(animation);
                        break;
                }

            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_Taipei101:
                Intent Taipei101Intent = new Intent(TamsuiActivity.this, Taipei101Activity.class);
                Taipei101Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(Taipei101Intent);
                //overridePendingTransition(0,0);
                break;
            case R.id.button_GongGuan:
                Intent GongGuanIntent = new Intent(TamsuiActivity.this, GongGuanActivity.class);
                GongGuanIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(GongGuanIntent);
                //overridePendingTransition(0,0);
                break;
            case R.id.button_Zoo:
                Intent ZooIntent = new Intent(TamsuiActivity.this, ZooActivity.class);
                ZooIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ZooIntent);
                //overridePendingTransition(0,0);
                break;
            case R.id.chat_bt:
                if (foundchat == true) {
                    Intent chatIntent = new Intent(TamsuiActivity.this, ChatRoom.class);
                    chatIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = new Bundle();
                    bundle.putString("ID_target", "BR517484");
                    chatIntent.putExtras(bundle);
                    startActivity(chatIntent);
                    //overridePendingTransition(0,0);
                }
                break;
            case R.id.camera:
                Intent mIntent=getPackageManager().getLaunchIntentForPackage("com.AR.bird");
                if (mIntent!=null){
                    startActivity(mIntent);
                }
                delay(3000);
                collectcount++;
                if (collectcount==4){
                    unlocked.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_character));
                    unlocked.setVisibility(View.VISIBLE);
                }
                arstamp.setImageResource(R.drawable.donestamp);
                break;
            case R.id.stampcollect:
                Intent collectIntent = new Intent(TamsuiActivity.this, CollectionActivity.class);
                collectIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle2=new Bundle();
                bundle2.putString("name", username);
                collectIntent.putExtras(bundle2);
                startActivity(collectIntent);
                break;
            case R.id.plus1:
                Intent writeIntent=new Intent(TamsuiActivity.this,MemoryWriteActivity.class);
                writeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundleplus1=new Bundle();
                bundleplus1.putString("Username",username);
                bundleplus1.putString("Location","圓山站");
                writeIntent.putExtras(bundleplus1);
                startActivity(writeIntent);
                break;
            case R.id.plus2:
                Intent write2Intent=new Intent(TamsuiActivity.this,MemoryWriteActivity.class);
                write2Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundleplus2=new Bundle();
                bundleplus2.putString("Username",username);
                bundleplus2.putString("Location","復興崗站");
                write2Intent.putExtras(bundleplus2);
                startActivity(write2Intent);
                break;
            case R.id.plus3:
                Intent write3Intent=new Intent(TamsuiActivity.this,MemoryWriteActivity.class);
                write3Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundleplus3=new Bundle();
                bundleplus3.putString("Username",username);
                bundleplus3.putString("Location","淡水站");
                write3Intent.putExtras(bundleplus3);
                startActivity(write3Intent);
                break;
            case R.id.button_person4:
                Intent personIntent = new Intent(TamsuiActivity.this, DcardPersonActivity.class);
                personIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(personIntent);
                break;
            case R.id.waterMRT2:
                map.setImageResource(R.drawable.mapkutin);
                break;
            case R.id.infoclose_bt:
                closebutton.setVisibility(View.INVISIBLE);
                info.setVisibility(View.INVISIBLE);
                break;
            case R.id.place1:
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(flowerPos, 15));
                currentlocation.setText("現在位置 : 台電大樓站");
                goal.setText("圓山站");
                infoline1.setText("松山新店線");
                int greencolor=Color.parseColor("#67C6A6");
                infoline1.setTextColor(greencolor);
                infoline2.setText("淡水信義線");
                stopnum.setText("2");
                stopnum2.setText("6");
                stop1.setText("中正紀念堂站");
                stop2.setText("圓山站");
                exit.setVisibility(View.VISIBLE);
                mrtview.setImageResource(R.drawable.metrotaipei2);
                infolinear2.setVisibility(View.VISIBLE);
                closebutton.setVisibility(View.VISIBLE);
                info.setVisibility(View.VISIBLE);
                info.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_character));
                closebutton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_character));
                break;
            case R.id.place2:
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos2, 15));
                currentlocation.setText("現在位置 : 圓山站");
                goal.setText("復興崗站");
                infoline1.setText("淡水信義線");
                int color= Color.parseColor("#EC7A8E");
                infoline1.setTextColor(color);
                stopnum.setText("9");
                stop1.setText("復興崗站");
                mrtview.setImageResource(R.drawable.metrotaipei);
                infolinear2.setVisibility(View.INVISIBLE);
                info.setVisibility(View.VISIBLE);
                exit.setVisibility(View.INVISIBLE);
                closebutton.setVisibility(View.VISIBLE);
                info.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_character));
                closebutton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_character));
                break;
            case R.id.place3:
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPos1, 15));
                currentlocation.setText("現在位置 : 復興崗站");
                goal.setText("淡水站");
                infoline1.setText("淡水信義線");
                int redcolor= Color.parseColor("#EC7A8E");
                infoline1.setTextColor(redcolor);
                stopnum.setText("5");
                stop1.setText("淡水站");
                mrtview.setImageResource(R.drawable.metrotaipei3);
                infolinear2.setVisibility(View.INVISIBLE);
                info.setVisibility(View.VISIBLE);
                exit.setVisibility(View.VISIBLE);
                closebutton.setVisibility(View.VISIBLE);
                info.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_character));
                closebutton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_character));
                break;
            case R.id.knowbutton:
                unlocked.setVisibility(View.INVISIBLE);
        }
    }

    public class myViewPagerAdapter extends PagerAdapter {
        private ArrayList<View> mListViews;

        public myViewPagerAdapter(ArrayList<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
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


    @Override
    protected void onResume() { //讓Activity啟動時啟動NfcAdapter支持前景模式下處理NFC Intent
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }

    @Override
    protected void onPause() { //暫停時關閉NfcAdapter的前景模式
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) { //辨識卡號
        String cardID = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
        if (cardID.equals("04B0F359210000")) {
            if (!foundstamp1) {

                first_nfc_alert_vis.setVisibility(View.VISIBLE);
                place_name1_vis.setVisibility(View.VISIBLE);
                scrollView_for_nfc1_vis.setVisibility(View.VISIBLE);
                cuteboy_nfc_vis1.setVisibility(View.VISIBLE);
                cuteboy_nfc_vis2.setVisibility(View.VISIBLE);
                story_closebutton1.setVisibility(View.VISIBLE);

                first_nfc_alert_vis.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show));
                place_name1_vis.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show));
                scrollView_for_nfc1_vis.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show));
                cuteboy_nfc_vis1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_character));
                cuteboy_nfc_vis2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_character_b));
                story_closebutton1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_character));

                story_closebutton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        first_nfc_alert_vis.setVisibility(View.INVISIBLE);
                        place_name1_vis.setVisibility(View.INVISIBLE);
                        scrollView_for_nfc1_vis.setVisibility(View.INVISIBLE);
                        cuteboy_nfc_vis1.setVisibility(View.INVISIBLE);
                        cuteboy_nfc_vis2.setVisibility(View.INVISIBLE);
                        story_closebutton1.setVisibility(View.INVISIBLE);
                    }
                });

                stamp1.setImageResource(R.drawable.p1);
                ObjectAnimator animTxtAlpha =
                        ObjectAnimator.ofFloat(stamp1, "alpha", 0, 1);
                animTxtAlpha.setDuration(2000);
                animTxtAlpha.setRepeatCount(0);
                animTxtAlpha.setRepeatMode(ObjectAnimator.REVERSE);
                animTxtAlpha.setInterpolator(new LinearInterpolator());
                animTxtAlpha.start();
                foundstamp1=true;
                collectcount++;
                if (collectcount==4){
                    unlocked.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_character));
                    unlocked.setVisibility(View.VISIBLE);
                }
            }
        }
        if (cardID.equals("047CFB59210000")) {
            if (!foundstamp2) {
                stamp2.setImageResource(R.drawable.p2);
                ObjectAnimator animTxtAlpha =
                        ObjectAnimator.ofFloat(stamp2, "alpha", 0, 1);
                animTxtAlpha.setDuration(2000);
                animTxtAlpha.setRepeatCount(0);
                animTxtAlpha.setRepeatMode(ObjectAnimator.REVERSE);
                animTxtAlpha.setInterpolator(new LinearInterpolator());
                animTxtAlpha.start();
                foundstamp2=true;
                collectcount++;
                if (collectcount==4){
                    unlocked.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_character));
                    unlocked.setVisibility(View.VISIBLE);
                }
            }
        }
        if (cardID.equals("044A7001210000")) {
            if (!foundstamp3) {
                stamp3.setImageResource(R.drawable.p3);
                ObjectAnimator animTxtAlpha =
                        ObjectAnimator.ofFloat(stamp3, "alpha", 0, 1);
                animTxtAlpha.setDuration(2000);
                animTxtAlpha.setRepeatCount(0);
                animTxtAlpha.setRepeatMode(ObjectAnimator.REVERSE);
                animTxtAlpha.setInterpolator(new LinearInterpolator());
                animTxtAlpha.start();
                foundstamp3=true;
                collectcount++;
                if (collectcount==4){
                    unlocked.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_character));
                    unlocked.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private String ByteArrayToHexString(byte[] inarray) { //轉10進位
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F"};
        String out = "";
        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    public void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
    }

    public void SearchBluetooth(){
        if(mBluetoothAdapter == null){ //沒找到
            Toast.makeText(this,"not find the bluetooth",Toast.LENGTH_SHORT).show();
            finish();
        }
        if(!mBluetoothAdapter.isEnabled()){
            //藍芽未開 跳出視窗提示使用者是否開啟藍芽
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,1);
            Set<BluetoothDevice> myDevices = mBluetoothAdapter.getBondedDevices();
            if(myDevices.size() > 0) {
                for(BluetoothDevice device : myDevices)
                    bluetoothdeviceslist.add(device.getName()+":"+device.getAddress()+"\n"); //藍芽連接的裝置資訊
            }
        }
        //註冊BroadcastReceiver: 用來接收搜尋到的結果
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(myreceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(myreceiver, filter);
    }

    final BroadcastReceiver myreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //收到的廣播類型
            String action = intent.getAction();
            //發現設備的廣播
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //從intent中獲取設備
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                double txPower = -59;
                double ratio = rssi*1.0/txPower;
                if (ratio < 1.0) {
                    dis = Math.pow(ratio,10);
                }
                else {
                    dis =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;
                }
                try {
                    if (device.getName().equals(ID_target)) {
                        foundchat=true;
                        if (!foundruntext){
                            Thread t1=new Thread (r2);
                            t1.start();
                            foundruntext=true;
                            mBluetoothAdapter.cancelDiscovery();
                        }
                    }
                    /*if (device.getName().equals(ID_target)){
                        if (!found) {
                            final Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            AlertDialog a = new AlertDialog.Builder(TamsuiActivity.this).setTitle("博愛座需求提醒")

                                    .setIcon(R.drawable.seat)

                                    .setMessage("周遭有博愛座需求者")

                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //mBluetoothAdapter.cancelDiscovery();
                                            vibrator.cancel();
                                        }
                                    }).show();
                            found=true;

                        }
                    }*/
                }
                catch(Exception e){
                }
            }
        }
    };
}
