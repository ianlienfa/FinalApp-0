package com.example.finalapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import nctu.fintech.appmate.Table;
import nctu.fintech.appmate.Tuple;

public class RecommendActivity extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener {
    int k = 0;
    int m = 0;
    int j=0;
    ViewPager pager;
    ArrayList<View> pagerList;

    private GoogleMap mMap;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000;
    protected LocationManager locationManager;
    protected MyLocationListener locationListener;

    String keyname;
    Table mTable2;
    LinearLayout view1;
    LinearLayout view2;
    LinearLayout view3;
    LinearLayout view4;
    LinearLayout view5;
    ImageButton add1;
    ImageButton add2;
    ImageButton add3;
    ImageButton add4;
    ImageButton add5;
    String tempname[] = new String[5];
    String tempMRTlocation[] = new String[5];
    String tempexit[] = new String[5];
    String temppicture[] = new String[5];
    String temptext[] = new String[5];
    TextView viewname1;
    TextView viewlocation1;
    ImageView viewimage1;
    ImageView viewimage2;
    ImageView viewimage3;
    ImageView viewimage4;
    ImageView viewimage5;
    TextView viewtext1;
    TextView viewname2;
    TextView viewlocation2;
    TextView viewtext2;
    TextView viewname3;
    TextView viewlocation3;
    TextView viewtext3;
    TextView viewname4;
    TextView viewlocation4;
    TextView viewtext4;
    TextView viewname5;
    TextView viewlocation5;
    TextView viewtext5;
    ImageButton navigate;
    ImageView viewstar1;
    ImageView viewstar2;
    ImageView viewstar3;
    ImageView viewstar4;
    ImageView viewstar5;
    Button maptag1;
    Button maptag2;
    Button maptag3;
    Button maptag4;
    Button maptag5;
    Button viewtag1;
    Button viewtag2;
    Button viewtag3;
    Button viewtag4;
    Button viewtag5;
    String temptag[] = new String[5];
    String temptag1;
    String temptag2;
    String tempstar[] = new String[5];
    String mrtstation;
    String templongitude[]=new String[5];
    String templatitude[]=new String[5];
    String username;
    double longitude;
    double latitude;
    boolean found[]=new boolean[5];

    private Runnable r3 = new Runnable(){
        public void run()
        {
            try {
                Tuple tuple_get[] = mTable2.get();
                for(int i=0; i<tuple_get.length; i++){
                    if (tuple_get[i].get("Tag1").equals("第一次來台灣")||tuple_get[i].get("Tag2").equals("第一次來台灣")){
                        tempMRTlocation[k]=tuple_get[i].get("MRTLocation");
                        tempname[k]=tuple_get[i].get("Name");
                        templongitude[k]=tuple_get[i].get("Longitude");
                        templatitude[k]=tuple_get[i].get("Latitude");
                        tempexit[k]=tuple_get[i].get("Exit");
                        temppicture[k]=tuple_get[i].get("Picture");
                        temptag1=tuple_get[i].get("Tag1");
                        temptag2=tuple_get[i].get("Tag2");
                        if (m<5){
                            for (j=0;j<m;j++){
                                if (temptag1.equals(temptext[j])){
                                    break;
                                }
                            }
                            if (j==m) {
                                temptext[j] = temptag1;
                                m+=1;
                            }
                            if (m<5) {
                                for (j = 0; j < m; j++) {
                                    if (temptag2.equals(temptext[j])) {
                                        break;
                                    }
                                }
                                if (j == m) {
                                    temptext[j] = temptag2;
                                    m += 1;
                                }
                            }
                        }
                        tempstar[k]=tuple_get[i].get("Comment");
                        if (temptag2==null){
                            temptag[k]="#"+temptag1;
                        }
                        else{
                            temptag[k]="#"+temptag1+" #"+temptag2;
                        }
                        k+=1;
                    }
                    if (k==5){
                        break;
                    }
                }

                Message msg = new Message();
                msg.what = 2;
                mHandler.sendMessage(msg);
            }catch (IOException e){
                Log.e("Net", "Fail to get");
            }
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 2:
                    if (m>0){
                        maptag1.setVisibility(View.VISIBLE);
                        viewtag1.setVisibility(View.VISIBLE);
                        maptag1.setText(temptext[0]);
                        viewtag1.setText(temptext[0]);
                    }else{
                        maptag1.setVisibility(View.GONE);
                        viewtag1.setVisibility(View.GONE);
                    }
                    if (m>1){
                        maptag2.setVisibility(View.VISIBLE);
                        viewtag2.setVisibility(View.VISIBLE);
                        maptag2.setText(temptext[1]);
                        viewtag2.setText(temptext[1]);
                    }else{
                        maptag2.setVisibility(View.GONE);
                        viewtag2.setVisibility(View.GONE);
                    }
                    if (m>2){
                        maptag3.setVisibility(View.VISIBLE);
                        viewtag3.setVisibility(View.VISIBLE);
                        maptag3.setText(temptext[2]);
                        viewtag3.setText(temptext[2]);
                    }else{
                        maptag3.setVisibility(View.GONE);
                        viewtag3.setVisibility(View.GONE);
                    }
                    if (m>3){
                        maptag4.setVisibility(View.VISIBLE);
                        viewtag4.setVisibility(View.VISIBLE);
                        maptag4.setText(temptext[3]);
                        viewtag4.setText(temptext[3]);
                    }else{
                        maptag4.setVisibility(View.GONE);
                        viewtag4.setVisibility(View.GONE);
                    }
                    if (m>4){
                        maptag5.setVisibility(View.VISIBLE);
                        viewtag5.setVisibility(View.VISIBLE);
                        maptag5.setText(temptext[4]);
                        viewtag5.setText(temptext[4]);
                    }else{
                        maptag5.setVisibility(View.GONE);
                        viewtag5.setVisibility(View.GONE);
                    }
                    if (k>0){
                        viewname1.setText(tempname[0]);
                        viewtext1.setText(temptag[0]);
                        if (tempexit[0]!=null&&!tempexit[0].equals("")){
                            viewlocation1.setText(tempMRTlocation[0]+tempexit[0]+"號出口");
                        }
                        else{
                            viewlocation1.setText(tempMRTlocation[0]);
                        }
                        if (tempstar[0].equals("2")){
                            viewstar1.setImageResource(R.drawable.star2);
                        }
                        else if (tempstar[0].equals("3")){
                            viewstar1.setImageResource(R.drawable.star3);
                        }
                        else if (tempstar[0].equals("4")){
                            viewstar1.setImageResource(R.drawable.star4);
                        }
                        else if (tempstar[0].equals("5")){
                            viewstar1.setImageResource(R.drawable.star5);
                        }
                        //建立一個AsyncTask執行緒進行圖片讀取動作，並帶入圖片連結網址路徑
                        new AsyncTask<String, Void, Bitmap>()
                        {
                            @Override
                            protected Bitmap doInBackground(String... params)
                            {
                                String url = params[0];
                                return getBitmapFromURL(url);
                            }

                            @Override
                            protected void onPostExecute(Bitmap result)
                            {
                                viewimage1. setImageBitmap (result);
                                super.onPostExecute(result);
                            }
                        }.execute(temppicture[0]);
                    }else{
                        view1.setVisibility(View.GONE);
                    }
                    if (k>1){
                        viewname2.setText(tempname[1]);
                        viewtext2.setText(temptag[1]);
                        if (tempexit[1]!=null&&!tempexit[1].equals("")){
                            viewlocation2.setText(tempMRTlocation[1]+tempexit[1]+"號出口");
                        }
                        else{
                            viewlocation2.setText(tempMRTlocation[1]);
                        }
                        if (tempstar[1].equals("2")){
                            viewstar2.setImageResource(R.drawable.star2);
                        }
                        else if (tempstar[1].equals("3")){
                            viewstar2.setImageResource(R.drawable.star3);
                        }
                        else if (tempstar[1].equals("4")){
                            viewstar2.setImageResource(R.drawable.star4);
                        }
                        else if (tempstar[1].equals("5")){
                            viewstar2.setImageResource(R.drawable.star5);
                        }
                        //建立一個AsyncTask執行緒進行圖片讀取動作，並帶入圖片連結網址路徑
                        new AsyncTask<String, Void, Bitmap>()
                        {
                            @Override
                            protected Bitmap doInBackground(String... params)
                            {
                                String url = params[0];
                                return getBitmapFromURL(url);
                            }

                            @Override
                            protected void onPostExecute(Bitmap result)
                            {
                                viewimage2. setImageBitmap (result);
                                super.onPostExecute(result);
                            }
                        }.execute(temppicture[1]);
                    }else{
                        view2.setVisibility(View.GONE);
                    }
                    if (k>2){
                        viewname3.setText(tempname[2]);
                        viewtext3.setText(temptag[2]);
                        if (tempexit[2]!=null&&!tempexit[2].equals("")){
                            viewlocation3.setText(tempMRTlocation[2]+tempexit[2]+"號出口");
                        }
                        else{
                            viewlocation3.setText(tempMRTlocation[2]);
                        }
                        if (tempstar[2].equals("2")){
                            viewstar3.setImageResource(R.drawable.star2);
                        }
                        else if (tempstar[2].equals("3")){
                            viewstar3.setImageResource(R.drawable.star3);
                        }
                        else if (tempstar[2].equals("4")){
                            viewstar3.setImageResource(R.drawable.star4);
                        }
                        else if (tempstar[2].equals("5")){
                            viewstar3.setImageResource(R.drawable.star5);
                        }
                        //建立一個AsyncTask執行緒進行圖片讀取動作，並帶入圖片連結網址路徑
                        new AsyncTask<String, Void, Bitmap>()
                        {
                            @Override
                            protected Bitmap doInBackground(String... params)
                            {
                                String url = params[0];
                                return getBitmapFromURL(url);
                            }

                            @Override
                            protected void onPostExecute(Bitmap result)
                            {
                                viewimage3. setImageBitmap (result);
                                super.onPostExecute(result);
                            }
                        }.execute(temppicture[2]);
                    }else{
                        view3.setVisibility(View.GONE);
                    }
                    if (k>3){
                        viewname4.setText(tempname[3]);
                        viewtext4.setText(temptag[3]);
                        if (tempexit[3]!=null&&!tempexit[3].equals("")){
                            viewlocation4.setText(tempMRTlocation[3]+tempexit[3]+"號出口");
                        }
                        else{
                            viewlocation4.setText(tempMRTlocation[3]);
                        }
                        if (tempstar[3].equals("2")){
                            viewstar4.setImageResource(R.drawable.star2);
                        }
                        else if (tempstar[3].equals("3")){
                            viewstar4.setImageResource(R.drawable.star3);
                        }
                        else if (tempstar[3].equals("4")){
                            viewstar4.setImageResource(R.drawable.star4);
                        }
                        else if (tempstar[3].equals("5")){
                            viewstar4.setImageResource(R.drawable.star5);
                        }
                        //建立一個AsyncTask執行緒進行圖片讀取動作，並帶入圖片連結網址路徑
                        new AsyncTask<String, Void, Bitmap>()
                        {
                            @Override
                            protected Bitmap doInBackground(String... params)
                            {
                                String url = params[0];
                                return getBitmapFromURL(url);
                            }

                            @Override
                            protected void onPostExecute(Bitmap result)
                            {
                                viewimage4. setImageBitmap (result);
                                super.onPostExecute(result);
                            }
                        }.execute(temppicture[3]);
                    }else{
                        view4.setVisibility(View.GONE);
                    }
                    if (k>4){
                        viewname5.setText(tempname[4]);
                        viewtext5.setText(temptag[4]);
                        if (tempexit[4]!=null&&!tempexit[4].equals("")){
                            viewlocation5.setText(tempMRTlocation[4]+tempexit[4]+"號出口");
                        }
                        else{
                            viewlocation5.setText(tempMRTlocation[4]);
                        }
                        if (tempstar[4].equals("2")){
                            viewstar5.setImageResource(R.drawable.star2);
                        }
                        else if (tempstar[4].equals("3")){
                            viewstar5.setImageResource(R.drawable.star3);
                        }
                        else if (tempstar[4].equals("4")){
                            viewstar5.setImageResource(R.drawable.star4);
                        }
                        else if (tempstar[4].equals("5")){
                            viewstar5.setImageResource(R.drawable.star5);
                        }
                        //建立一個AsyncTask執行緒進行圖片讀取動作，並帶入圖片連結網址路徑
                        new AsyncTask<String, Void, Bitmap>()
                        {
                            @Override
                            protected Bitmap doInBackground(String... params)
                            {
                                String url = params[0];
                                return getBitmapFromURL(url);
                            }

                            @Override
                            protected void onPostExecute(Bitmap result)
                            {
                                viewimage5. setImageBitmap (result);
                                super.onPostExecute(result);
                            }
                        }.execute(temppicture[4]);
                    }else{
                        view5.setVisibility(View.GONE);
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        Bundle bundle = getIntent().getExtras();
        username=bundle.getString("username");
        pager = (ViewPager) findViewById(R.id.pager);
        mTable2 = new Table("http://172.20.10.7:8000/api", "landmark", "Secondteam", "secondteam12345");
        LayoutInflater li = getLayoutInflater().from(this);
        View v2 = li.inflate(R.layout.fragment_map, null);
        View v4 = li.inflate(R.layout.fragment_recommend, null);
        pagerList = new ArrayList<View>();
        pagerList.add(v2);
        pagerList.add(v4);

        pager.setAdapter(new myViewPagerAdapter(pagerList));
        pager.setCurrentItem(0);
        //map
        navigate = (ImageButton) v2.findViewById(R.id.navigate);
        navigate.setOnClickListener(this);
        navigate.setOnTouchListener(touchlistener);
        maptag1 = (Button) v2.findViewById(R.id.tag1);
        maptag1.setOnClickListener(this);
        maptag1.setOnTouchListener(touchlistener);
        maptag2 = (Button) v2.findViewById(R.id.tag2);
        maptag2.setOnClickListener(this);
        maptag2.setOnTouchListener(touchlistener);
        maptag3 = (Button) v2.findViewById(R.id.tag3);
        maptag3.setOnClickListener(this);
        maptag3.setOnTouchListener(touchlistener);
        maptag4 = (Button) v2.findViewById(R.id.tag4);
        maptag4.setOnClickListener(this);
        maptag4.setOnTouchListener(touchlistener);
        maptag5 = (Button) v2.findViewById(R.id.tag5);
        maptag5.setOnClickListener(this);
        maptag5.setOnTouchListener(touchlistener);

        for (int i=0;i<5;i++){
            found[i]=false;
        }

        //recommend
        viewtag1 = (Button) v4.findViewById(R.id.tag1);
        viewtag1.setOnClickListener(this);
        viewtag1.setOnTouchListener(touchlistener);
        viewtag2 = (Button) v4.findViewById(R.id.tag2);
        viewtag2.setOnClickListener(this);
        viewtag2.setOnTouchListener(touchlistener);
        viewtag3 = (Button) v4.findViewById(R.id.tag3);
        viewtag3.setOnClickListener(this);
        viewtag3.setOnTouchListener(touchlistener);
        viewtag4 = (Button) v4.findViewById(R.id.tag4);
        viewtag4.setOnClickListener(this);
        viewtag4.setOnTouchListener(touchlistener);
        viewtag5 = (Button) v4.findViewById(R.id.tag5);
        viewtag5.setOnClickListener(this);
        viewtag5.setOnTouchListener(touchlistener);
        viewstar1 = (ImageView) v4.findViewById(R.id.star1);
        viewstar2 = (ImageView) v4.findViewById(R.id.star2);
        viewstar3 = (ImageView) v4.findViewById(R.id.star3);
        viewstar4 = (ImageView) v4.findViewById(R.id.star4);
        viewstar5 = (ImageView) v4.findViewById(R.id.star5);
        add1=(ImageButton)v4.findViewById(R.id.button_add1);
        add1.setOnClickListener(this);
        add1.setOnTouchListener(touchlistener);
        add2=(ImageButton)v4.findViewById(R.id.button_add2);
        add2.setOnClickListener(this);
        add2.setOnTouchListener(touchlistener);
        add3=(ImageButton)v4.findViewById(R.id.button_add3);
        add3.setOnClickListener(this);
        add3.setOnTouchListener(touchlistener);
        add4=(ImageButton)v4.findViewById(R.id.button_add4);
        add4.setOnClickListener(this);
        add4.setOnTouchListener(touchlistener);
        add5=(ImageButton)v4.findViewById(R.id.button_add5);
        add5.setOnClickListener(this);
        add5.setOnTouchListener(touchlistener);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
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
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, // NETWORK_PROVIDER、PASSIVE_PROVIDER
                MINIMUM_TIME_BETWEEN_UPDATES, // 每隔多久update一次(ms)
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, // 被定位物件每移動幾公尺update一次(m)
                (LocationListener) locationListener
        );

        //recommend
        view1 = v4.findViewById(R.id.viewgroup1);
        view1.setOnClickListener(this);
        view1.setOnTouchListener(touchlistener);
        view2 = v4.findViewById(R.id.viewgroup2);
        view2.setOnClickListener(this);
        view2.setOnTouchListener(touchlistener);
        view3 = v4.findViewById(R.id.viewgroup3);
        view3.setOnClickListener(this);
        view3.setOnTouchListener(touchlistener);
        view4 = v4.findViewById(R.id.viewgroup4);
        view4.setOnClickListener(this);
        view4.setOnTouchListener(touchlistener);
        view5 = v4.findViewById(R.id.viewgroup5);
        view5.setOnClickListener(this);
        view5.setOnTouchListener(touchlistener);

        viewname1=(TextView)v4.findViewById(R.id.viewname1);
        viewlocation1=(TextView)v4.findViewById(R.id.viewlocation1);
        viewtext1=(TextView)v4.findViewById(R.id.viewtext1);
        viewimage1=(ImageView)v4.findViewById(R.id.view1);
        viewname2=(TextView)v4.findViewById(R.id.viewname2);
        viewlocation2=(TextView)v4.findViewById(R.id.viewlocation2);
        viewtext2=(TextView)v4.findViewById(R.id.viewtext2);
        viewimage2=(ImageView)v4.findViewById(R.id.view2);
        viewname3=(TextView)v4.findViewById(R.id.viewname3);
        viewlocation3=(TextView)v4.findViewById(R.id.viewlocation3);
        viewtext3=(TextView)v4.findViewById(R.id.viewtext3);
        viewimage3=(ImageView)v4.findViewById(R.id.view3);
        viewname4=(TextView)v4.findViewById(R.id.viewname4);
        viewlocation4=(TextView)v4.findViewById(R.id.viewlocation4);
        viewtext4=(TextView)v4.findViewById(R.id.viewtext4);
        viewimage4=(ImageView)v4.findViewById(R.id.view4);
        viewname5=(TextView)v4.findViewById(R.id.viewname5);
        viewlocation5=(TextView)v4.findViewById(R.id.viewlocation5);
        viewtext5=(TextView)v4.findViewById(R.id.viewtext5);
        viewimage5=(ImageView)v4.findViewById(R.id.view5);

        Thread t2=new Thread(r3);
        t2.start();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

// 得知GPS位置時，根據取得的經緯度標上 marker
        if (location != null) {
            LatLng nowPos = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(nowPos).title("Current"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPos, 15));
        } else {
            Toast.makeText(RecommendActivity.this, "null", Toast.LENGTH_SHORT).show();
        }

    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            // 每次地點更新時，會呼叫 onLocationChanged()方法
            /*LatLng nowPos = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.clear(); // 清理之前在地圖上 marker
            mMap.addMarker(new MarkerOptions().position(nowPos).title("Current"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPos, 20));*/
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(RecommendActivity.this, "Provider status changed",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(RecommendActivity.this, "Provider disabled by the user. GPS turned　off",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(RecommendActivity.this,"Provider enabled by the user. GPS turned on", Toast.LENGTH_SHORT).show();
        }

    }

    //讀取網路圖片，型態為Bitmap
    private static Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
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

    ImageButton.OnTouchListener touchlistener = new ImageButton.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Animation animation = AnimationUtils.loadAnimation(RecommendActivity.this, R.anim.pressdown);
                switch (v.getId()) {
                    case R.id.viewgroup1:
                        view1.startAnimation(animation);
                        break;
                    case R.id.viewgroup2:
                        view2.startAnimation(animation);
                        break;

                    case R.id.viewgroup3:
                        view3.startAnimation(animation);
                        break;

                    case R.id.viewgroup4:
                        view4.startAnimation(animation);
                        break;

                    case R.id.viewgroup5:
                        view5.startAnimation(animation);
                        break;
                    case R.id.navigate:
                        navigate.startAnimation(animation);
                        break;
                    case R.id.tag1:
                        maptag1.startAnimation(animation);
                        viewtag1.startAnimation(animation);
                        break;
                    case R.id.tag2:
                        maptag2.startAnimation(animation);
                        viewtag2.startAnimation(animation);
                        break;
                    case R.id.tag3:
                        maptag3.startAnimation(animation);
                        viewtag3.startAnimation(animation);
                        break;
                    case R.id.tag4:
                        maptag4.startAnimation(animation);
                        viewtag4.startAnimation(animation);
                        break;
                    case R.id.tag5:
                        maptag5.startAnimation(animation);
                        viewtag5.startAnimation(animation);
                        break;
                    case R.id.button_add1:
                        add1.startAnimation(animation);
                        break;
                    case R.id.button_add2:
                        add2.startAnimation(animation);
                        break;
                    case R.id.button_add3:
                        add3.startAnimation(animation);
                        break;
                    case R.id.button_add4:
                        add4.startAnimation(animation);
                        break;
                    case R.id.button_add5:
                        add5.startAnimation(animation);
                        break;
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Animation animation = AnimationUtils.loadAnimation(RecommendActivity.this, R.anim.pressdown);
                switch (v.getId()) {
                    case R.id.viewgroup1:
                        view1.startAnimation(animation);
                        break;
                    case R.id.viewgroup2:
                        view2.startAnimation(animation);
                        break;

                    case R.id.viewgroup3:
                        view3.startAnimation(animation);
                        break;

                    case R.id.viewgroup4:
                        view4.startAnimation(animation);
                        break;

                    case R.id.viewgroup5:
                        view5.startAnimation(animation);
                        break;
                    case R.id.navigate:
                        navigate.startAnimation(animation);
                        break;
                    case R.id.tag1:
                        maptag1.startAnimation(animation);
                        viewtag1.startAnimation(animation);
                        break;
                    case R.id.tag2:
                        maptag2.startAnimation(animation);
                        viewtag2.startAnimation(animation);
                        break;
                    case R.id.tag3:
                        maptag3.startAnimation(animation);
                        viewtag3.startAnimation(animation);
                        break;
                    case R.id.tag4:
                        maptag4.startAnimation(animation);
                        viewtag4.startAnimation(animation);
                        break;
                    case R.id.tag5:
                        maptag5.startAnimation(animation);
                        viewtag5.startAnimation(animation);
                        break;
                    case R.id.button_add1:
                        add1.startAnimation(animation);
                        break;
                    case R.id.button_add2:
                        add2.startAnimation(animation);
                        break;
                    case R.id.button_add3:
                        add3.startAnimation(animation);
                        break;
                    case R.id.button_add4:
                        add4.startAnimation(animation);
                        break;
                    case R.id.button_add5:
                        add5.startAnimation(animation);
                        break;
                }
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_add1:
                found[0]=true;
                LatLng nowPos1 = new LatLng(Double.parseDouble(templatitude[0]), Double.parseDouble(templongitude[0]));
                mMap.addMarker(new MarkerOptions().position(nowPos1).title(tempname[0]));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPos1, 15));
                Toast.makeText(RecommendActivity.this, "已加入 "+tempname[0], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_add2:
                found[1]=true;
                LatLng nowPos2 = new LatLng(Double.parseDouble(templatitude[1]), Double.parseDouble(templongitude[1]));
                mMap.addMarker(new MarkerOptions().position(nowPos2).title(tempname[1]));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPos2, 15));
                Toast.makeText(RecommendActivity.this, "已加入 "+tempname[1], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_add3:
                found[2]=true;
                LatLng nowPos3 = new LatLng(Double.parseDouble(templatitude[2]), Double.parseDouble(templongitude[2]));
                mMap.addMarker(new MarkerOptions().position(nowPos3).title(tempname[2]));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPos3, 15));
                Toast.makeText(RecommendActivity.this, "已加入 "+tempname[2], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_add4:
                found[3]=true;
                LatLng nowPos4 = new LatLng(Double.parseDouble(templatitude[3]), Double.parseDouble(templongitude[3]));
                mMap.addMarker(new MarkerOptions().position(nowPos4).title(tempname[3]));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPos4, 15));
                Toast.makeText(RecommendActivity.this, "已加入 "+tempname[3], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_add5:
                found[4]=true;
                LatLng nowPos5 = new LatLng(Double.parseDouble(templatitude[4]), Double.parseDouble(templongitude[4]));
                mMap.addMarker(new MarkerOptions().position(nowPos5).title(tempname[4]));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPos5, 15));
                Toast.makeText(RecommendActivity.this, "已加入 "+tempname[4], Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigate:
                Intent SearchMapIntent = new Intent(RecommendActivity.this, SearchMapActivity.class);
                SearchMapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle2 = new Bundle();
                bundle2.putString("username",username);
                bundle2.putStringArray("Longitude",templongitude);
                bundle2.putStringArray("Latitude",templatitude);
                bundle2.putStringArray("Name",tempname);
                bundle2.putInt("Count",k);
                bundle2.putString("MRTName",keyname);
                bundle2.putDouble("MRTLongitude",longitude);
                bundle2.putDouble("MRTLatitude",latitude);
                bundle2.putBooleanArray("Found",found);
                SearchMapIntent.putExtras(bundle2);
                startActivity(SearchMapIntent);
                break;
            //overridePendingTransition(0,0);
            case R.id.viewgroup1:
                Intent view1Intent = new Intent(RecommendActivity.this, IntroductionActivity.class);
                view1Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundleview1 = new Bundle();
                bundleview1.putString("Name",tempname[0]);
                bundleview1.putString("MRTLocation",tempMRTlocation[0]);
                bundleview1.putString("Picture",temppicture[0]);
                bundleview1.putString("MRTExit",tempexit[0]);
                bundleview1.putString("Star",tempstar[0]);
                bundleview1.putString("Tag",temptag[0]);
                view1Intent.putExtras(bundleview1);
                startActivity(view1Intent);
                break;
            case R.id.viewgroup2:
                Intent view2Intent = new Intent(RecommendActivity.this, IntroductionActivity.class);
                view2Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundleview2 = new Bundle();
                bundleview2.putString("Name",tempname[1]);
                bundleview2.putString("MRTLocation",tempMRTlocation[1]);
                bundleview2.putString("MRTExit",tempexit[1]);
                bundleview2.putString("Picture",temppicture[1]);
                bundleview2.putString("Star",tempstar[1]);
                bundleview2.putString("Tag",temptag[1]);
                view2Intent.putExtras(bundleview2);
                startActivity(view2Intent);
                break;
            case R.id.viewgroup3:
                Intent view3Intent = new Intent(RecommendActivity.this, IntroductionActivity.class);
                view3Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundleview3 = new Bundle();
                bundleview3.putString("Name",tempname[2]);
                bundleview3.putString("Picture",temppicture[2]);
                bundleview3.putString("MRTLocation",tempMRTlocation[2]);
                bundleview3.putString("MRTExit",tempexit[2]);
                bundleview3.putString("Star",tempstar[2]);
                bundleview3.putString("Tag",temptag[2]);
                view3Intent.putExtras(bundleview3);
                startActivity(view3Intent);
                break;
            case R.id.viewgroup4:
                Intent view4Intent = new Intent(RecommendActivity.this, IntroductionActivity.class);
                view4Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundleview4 = new Bundle();
                bundleview4.putString("Name",tempname[3]);
                bundleview4.putString("MRTLocation",tempMRTlocation[3]);
                bundleview4.putString("MRTExit",tempexit[3]);
                bundleview4.putString("Picture",temppicture[3]);
                bundleview4.putString("Star",tempstar[3]);
                bundleview4.putString("Tag",temptag[3]);
                view4Intent.putExtras(bundleview4);
                startActivity(view4Intent);
                break;
            case R.id.viewgroup5:
                Intent view5Intent = new Intent(RecommendActivity.this, IntroductionActivity.class);
                view5Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundleview5 = new Bundle();
                bundleview5.putString("Name",tempname[4]);
                bundleview5.putString("MRTLocation",tempMRTlocation[4]);
                bundleview5.putString("MRTExit",tempexit[4]);
                bundleview5.putString("Star",tempstar[4]);
                bundleview5.putString("Picture",temppicture[4]);
                bundleview5.putString("Tag",temptag[4]);
                view5Intent.putExtras(bundleview5);
                startActivity(view5Intent);
                break;
        }

    }
}
