package com.example.finalapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import nctu.fintech.appmate.Table;
import nctu.fintech.appmate.Tuple;

public class IntroductionActivity extends AppCompatActivity {
    String name;
    String location;
    String exit;
    String star;
    String tag;
    String picture;
    ImageView viewpicture;
    TextView viewname;
    TextView viewlocation;
    TextView viewtag;
    ImageView viewstar;
    String tempname[]=new String[8];
    int k=0;
    int m=0;
    Table mTable;
    String tempstar;
    String temppicture[]=new String[8];

    private ListView lv1;
    int[] imagesId={R.drawable.person2,R.drawable.person4,R.drawable.person6,R.drawable.person5,R.drawable.person1,R.drawable.person3,R.drawable.person7,R.drawable.person8};
    String[] names=new String[8];
    //int[] images={R.drawable.park2,R.drawable.park3,R.drawable.park4,R.drawable.park5,R.drawable.park6,R.drawable.park1,R.drawable.park,R.drawable.park7};
    private int[] starimages=new int [8];
    Bitmap images[]=new Bitmap[8];
    AsyncTask []task=new AsyncTask[8];


    private Runnable r1 = new Runnable(){
        public void run()
        {
            try {
                Tuple tuple_get[] = mTable.get();
                for(int i=0; i<tuple_get.length; i++){
                    if (tuple_get[i].get("Landmark").equals(name)){
                        tempname[k]=tuple_get[i].get("Username");
                        names[k]=tuple_get[i].get("Comment");
                        tempstar=tuple_get[i].get("Star");
                        if (tempstar.equals("2")){
                            starimages[k]=R.drawable.star2;
                        }
                        else if (tempstar.equals("3")){
                            starimages[k]=R.drawable.star3;
                        }
                        else if (tempstar.equals("4")){
                            starimages[k]=R.drawable.star4;
                        }
                        else if (tempstar.equals("5")){
                            starimages[k]=R.drawable.star5;
                        }
                        temppicture[k]=tuple_get[i].get("Picture1");
                        try
                        {
                            URL url = new URL(temppicture[k]);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(input);
                            images[k]=bitmap;
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        k+=1;
                        if (k==5)
                            break;
                    }
                }
                Message msg = new Message();
                msg.what = 1;
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
                case 1:
                    lv1.setAdapter(adapter);
                    break;
            }
        }
    };

    private void delay(int ms){
        try {
            Thread.currentThread();
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        Bundle bundle=getIntent().getExtras();
        name=bundle.getString("Name");
        location=bundle.getString("MRTLocation");
        exit=bundle.getString("MRTExit");
        star=bundle.getString("Star");
        tag=bundle.getString("Tag");
        picture=bundle.getString("Picture");

        mTable = new Table("http://172.20.10.7:8000/api", "comment", "Secondteam", "secondteam12345");

        viewname=(TextView)findViewById(R.id.introname);
        viewlocation=(TextView)findViewById(R.id.introMRTlocation);
        viewtag=(TextView)findViewById(R.id.introtag);
        viewstar=(ImageView)findViewById(R.id.introstar);
        viewpicture=(ImageView)findViewById(R.id.intropicture);
        viewname.setText(name);
        if (exit != null && !exit.equals("")) {
            viewlocation.setText(location+exit+"號出口");
        }
        else{
            viewlocation.setText(location);
        }
        viewtag.setText(tag);
        if (star.equals("2")){
            viewstar.setImageResource(R.drawable.star2);
        }
        else if (star.equals("3")){
            viewstar.setImageResource(R.drawable.star3);
        }
        else if (star.equals("4")){
            viewstar.setImageResource(R.drawable.star4);
        }
        else if (star.equals("5")){
            viewstar.setImageResource(R.drawable.star5);
        }

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
                viewpicture.setImageBitmap (result);
                super.onPostExecute(result);
            }
        }.execute(picture);

        lv1 = (ListView) findViewById(R.id.listView2);

        Thread t1=new Thread (r1);
        t1.start();
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                if (arg3 == 0) {
                    //Intent intent = new Intent(IntroductionActivity.this, DcardPostActivity.class);
                    //startActivity(intent);
                }


            }
        });

    }

    BaseAdapter adapter = new BaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO 自动生成的方法存根
            View layout=View.inflate(IntroductionActivity.this, R.layout.list_item2, null);
            ImageView face = (ImageView)layout.findViewById(R.id.face);
            TextView name =(TextView)layout.findViewById(R.id.name);
            ImageView face2 = (ImageView)layout.findViewById(R.id.face2);
            ImageView star=(ImageView)layout.findViewById(R.id.list_star);
            TextView username=(TextView)layout.findViewById(R.id.username);

            username.setText(tempname[position]);
            face.setImageResource(imagesId[position]);
            name.setText(names[position]);
            //face2.setImageResource(images[position]);
            star.setImageResource(starimages[position]);
            face2.setImageBitmap(images[position]);
            return layout;
        }

        @Override
        public long getItemId(int position) {
            // TODO 自动生成的方法存根
            return position;
        }

        @Override
        public Object getItem(int position) {
            // TODO 自动生成的方法存根
            return names[position];
        }

        @Override
        public int getCount() {
            // TODO 自动生成的方法存根
            //return names.length;
            return k;
        }
    };///new BaseAdapter()
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
}
