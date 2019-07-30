package com.example.finalapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import nctu.fintech.appmate.Table;
import nctu.fintech.appmate.Tuple;

public class Taipei101Activity extends AppCompatActivity {
    String tempname[]=new String[8];
    String temppicture[]=new String[8];
    View layout;
    ImageView face2;
    int k=0;
    int m=0;
    Table mTable;
    String tempstar;

    private ListView lv1;
    int[] imagesId={R.drawable.person2,R.drawable.person4,R.drawable.person6,R.drawable.person5,R.drawable.person1,R.drawable.person3,R.drawable.person7,R.drawable.person8};
    String[] names=new String[8];
    //int[] images={R.drawable.park2,R.drawable.park3,R.drawable.park4,R.drawable.park5,R.drawable.park6,R.drawable.park1,R.drawable.park,R.drawable.park7};
    private int[] starimages=new int [8];
    Bitmap images[]=new Bitmap[8];


    private Runnable r1 = new Runnable(){
        public void run()
        {
            try {
                Tuple tuple_get[] = mTable.get();
                for(int i=0; i<tuple_get.length; i++){
                    if (tuple_get[i].get("Landmark").equals("台北101")){
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
                        k+=1;
                        if (k==8)
                            break;
                    }
                }
                for (m=0;m<k;m++) {
                    new AsyncTask<String, Void, Bitmap>() {
                        @Override
                        protected Bitmap doInBackground(String... params) {
                            String url = params[0];
                            return getBitmapFromURL(url);
                        }

                        @Override
                        protected void onPostExecute(Bitmap result) {
                            //viewpicture. setImageBitmap (result);
                            images[m] = result;
                            super.onPostExecute(result);
                        }
                    }.execute(temppicture[m]);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taipei101);
        mTable = new Table("http://172.20.10.7:8000/api", "comment", "Secondteam", "secondteam12345");

        lv1 = (ListView) findViewById(R.id.listView3);

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
            layout=View.inflate(Taipei101Activity.this, R.layout.list_item2, null);
            ImageView face = (ImageView)layout.findViewById(R.id.face);
            TextView name =(TextView)layout.findViewById(R.id.name);
            face2 = (ImageView)layout.findViewById(R.id.face2);
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
