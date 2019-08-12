package com.example.finalapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import nctu.fintech.appmate.Table;
import nctu.fintech.appmate.Tuple;

public class EmptyPostActivity extends AppCompatActivity {
    ImageView locationpic;
    TextView location;
    TextView text;
    ImageView imagestar;
    String star;
    String landmark;
    ImageView image1;
    ImageView image2;
    ImageView image3;
    String picture[]=new String[3];
    Uri uri[]=new Uri[3];
    Table mTable;
    String temppicture;

    private Runnable r1 = new Runnable() {
        public void run() {
            try {
                Tuple tuple_get[] = mTable.get();
                for (int i=0;i<tuple_get.length;i++){
                    if (tuple_get[i].get("Name").equals(landmark)){
                        temppicture=tuple_get[i].get("Picture");
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
                            locationpic.setImageBitmap (result);
                            super.onPostExecute(result);
                        }
                    }.execute(temppicture);
                    break;
            }
        }
    };

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_post);
        mTable = new Table("http://172.20.10.7:8000/api", "landmark", "Secondteam", "secondteam12345");
        locationpic=findViewById(R.id.locationpic);
        location=findViewById(R.id.locationtitle);
        text=findViewById(R.id.locationtext);
        imagestar=findViewById(R.id.locationstar);
        image1=findViewById(R.id.postimage1);
        image2=findViewById(R.id.postimage2);
        image3=findViewById(R.id.postimage3);

        Bundle bundle=getIntent().getExtras();
        landmark=bundle.getString("Location");
        Thread t1=new Thread(r1);
        t1.start();
        location.setText(landmark);
        text.setText(bundle.getString("Text"));
        star=bundle.getString("Star");
        if (star.equals("2.0")){
            imagestar.setImageResource(R.drawable.star2);
        }
        else if (star.equals("3.0")){
            imagestar.setImageResource(R.drawable.star3);
        }
        else if (star.equals("4.0")){
            imagestar.setImageResource(R.drawable.star4);
        }
        else if (star.equals("5.0")){
            imagestar.setImageResource(R.drawable.star5);
        }
        picture=bundle.getStringArray("Picture");
        for (int i=0;i<3;i++){
            if (picture[i]!=null) {
                uri[i] = Uri.parse(picture[i]);
            }else{
                if (i==0)
                    image1.setVisibility(View.GONE);
                else if (i==1)
                    image2.setVisibility(View.GONE);
                else
                    image3.setVisibility(View.GONE);
            }
        }
        image1.setImageURI(uri[0]);
        image2.setImageURI(uri[1]);
        image3.setImageURI(uri[2]);
    }
}
