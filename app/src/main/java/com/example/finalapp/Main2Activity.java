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

public class Main2Activity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener{
    ViewPager pager;
    ArrayList<View> pagerList;

    private GoogleMap mMap;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000;
    protected LocationManager locationManager;
    //protected MyLocationListener locationListener;

    private BluetoothAdapter mBluetoothAdapter;
    private List<String> bluetoothdeviceslist = new ArrayList<String>();
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private String ID_target = "BR517484";
    double dis;
    boolean foundchat = false;
    boolean foundruntext = false;
    Table mTable;
    String s1;
    ImageButton person4;
    ImageButton Taipei101Text;
    ImageButton GongGuanText;
    ImageButton ZooText;
    ImageButton EggText;
    ImageButton WaterText;
    ImageButton ChatText;
    ImageButton collect;
    ImageButton dcardsearch;
    ImageButton pen;
    EditText search_et;
    ImageButton search_bt;
    EditText search_et1;
    AutoScrollTextView runText;
    String username;
    TextView maintitle;
    TextView maintitle2;
    String currentlocation;

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

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBluetoothPermission();
        SearchBluetooth();
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
        pager = (ViewPager) findViewById(R.id.pager);

        LayoutInflater li = getLayoutInflater().from(this);
        View v1 = li.inflate(R.layout.fragment_section1, null);
        View v2 = li.inflate(R.layout.fragment_section2, null);
        pagerList = new ArrayList<View>();
        pagerList.add(v1);
        pagerList.add(v2);

        pager.setAdapter(new myViewPagerAdapter(pagerList));
        pager.setCurrentItem(0);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, // NETWORK_PROVIDER、PASSIVE_PROVIDER
                MINIMUM_TIME_BETWEEN_UPDATES, // 每隔多久update一次(ms)
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, // 被定位物件每移動幾公尺update一次(m)
                (LocationListener) locationListener
        );*/


        //section1
        search_bt = (ImageButton) v1.findViewById(R.id.iv_default1);
        search_bt.setOnTouchListener(touchlistener);
        search_et1 = (EditText) v1.findViewById(R.id.et_search1);
        maintitle=(TextView)v1.findViewById(R.id.MainTitle);
        maintitle.setText(username+" ! 你好");
        maintitle2=(TextView)v1.findViewById(R.id.MainTitle2);
        //Date d = new Date();
        //CharSequence s  = DateFormat.format("yyyy-MM-dd", d.getTime());
        maintitle2.setText("你現在位於 國立臺灣大學綜合體育館");
        collect = (ImageButton) v1.findViewById(R.id.collect);
        collect.setOnTouchListener(touchlistener);

        search_bt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_et1.getText().toString().equals("淡水")){
                    Intent searchIntent=new Intent(Main2Activity.this,SearchViewActivity.class);
                    searchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundleview=new Bundle();
                    bundleview.putString("username",username);
                    searchIntent.putExtras(bundleview);
                    mBluetoothAdapter.cancelDiscovery();
                    startActivity(searchIntent);
                }else {
                    Intent searchmapIntent = new Intent(Main2Activity.this, MRTStationActivity.class);
                    searchmapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = new Bundle();
                    bundle.putString("key", search_et1.getText().toString());
                    bundle.putString("username", username);
                    searchmapIntent.putExtras(bundle);
                    search_et1.setText("");
                    mBluetoothAdapter.cancelDiscovery();
                    startActivity(searchmapIntent);
                }
            }
        });

        collect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent collectionIntent = new Intent(Main2Activity.this, CollectionActivity.class);
                collectionIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("name", username);
                collectionIntent.putExtras(bundle);
                startActivity(collectionIntent);
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
        pen= (ImageButton) v2.findViewById(R.id.button_pen);
        pen.setOnClickListener(this);
        pen.setOnTouchListener(touchlistener);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LatLng nowPos=new LatLng(25.021952, 121.535371);
        mMap.addMarker(new MarkerOptions().position(nowPos).title(username));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPos, 15));
        // 取得最後得知道provider資訊
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

// 得知GPS位置時，根據取得的經緯度標上 marker
        /*if (location != null) {
            //LatLng nowPos = new LatLng(location.getLatitude(), location.getLongitude());
            LatLng nowPos=new LatLng(25.021952, 121.535371);
            mMap.addMarker(new MarkerOptions().position(nowPos).title(username));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPos, 15));
        } else {
            Toast.makeText(Main2Activity.this, "null", Toast.LENGTH_SHORT).show();
        }*/

    }

    /*private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            // 每次地點更新時，會呼叫 onLocationChanged()方法
            LatLng nowPos = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.clear(); // 清理之前在地圖上 marker
            mMap.addMarker(new MarkerOptions().position(nowPos).title(username));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPos, 20));
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(Main2Activity.this, "Provider status changed",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(Main2Activity.this, "Provider disabled by the user. GPS turned　off",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(Main2Activity.this,"Provider enabled by the user. GPS turned on", Toast.LENGTH_SHORT).show();
        }

    }*/


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
                if (foundchat) {
                    Intent chatIntent = new Intent(Main2Activity.this, ChatRoom.class);
                    chatIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = new Bundle();
                    bundle.putString("Username",username);
                    chatIntent.putExtras(bundle);
                    startActivity(chatIntent);
                    //overridePendingTransition(0,0);
                }
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
                    case R.id.collect:
                        collect.startAnimation(animation);
                        break;
                    case R.id.button_person4:
                        person4.startAnimation(animation);
                        break;
                    case R.id.button_pen:
                        pen.startAnimation(animation);
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
                    case R.id.collect:
                        collect.startAnimation(animation);
                        break;
                    case R.id.button_person4:
                        person4.startAnimation(animation);
                        break;
                    case R.id.button_pen:
                        pen.startAnimation(animation);
                        break;

                }

            }
            return false;
        }
    };

    public void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {
            // Android M Permission check
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_COARSE_LOCATION);
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
                try{
                    if (device.getName().equals(ID_target)) {
                        foundchat=true;
                        if (!foundruntext){
                            Thread t1=new Thread(r2);
                            t1.start();
                            foundruntext=true;
                            mBluetoothAdapter.cancelDiscovery();
                        }
                    }
                    /*if (device.getName().equals(ID_target)){
                        if (!found) {
                            final Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            //vibrator.vibrate(new long []{500,500,500,500,500},0);
                            AlertDialog a = new AlertDialog.Builder(Main2Activity.this).setTitle("博愛座需求提醒")

                                    .setIcon(R.drawable.seat)

                                    .setMessage("周遭有博愛座需求者")

                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mBluetoothAdapter.cancelDiscovery();
                                            vibrator.cancel();
                                        }
                                    }).show();
                            //a.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            a.setCanceledOnTouchOutside(false);
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
