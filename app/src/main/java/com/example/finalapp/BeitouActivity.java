package com.example.finalapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import nctu.fintech.appmate.Table;
import nctu.fintech.appmate.Tuple;

public class BeitouActivity extends AppCompatActivity {
    ViewPager pager;
    ArrayList<View> pagerList;
    TextView eptitle;
    TextView eptext;
    boolean write;
    Table mTable;
    String location;
    String text;
    String picture[]=new String [3];
    int len;
    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView stamp;

    private Runnable r1 = new Runnable() {
        public void run() {
            try {
                Tuple tuple_get[] = mTable.get();
                len=tuple_get.length-1;
                location=tuple_get[len].get("Location");
                text=tuple_get[len].get("Text");
                picture[0]=tuple_get[len].get("Picture1");
                picture[1]=tuple_get[len].get("Picture2");
                picture[2]=tuple_get[len].get("Picture3");
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
                    eptitle.setText(location);
                    eptext.setText(text);
                    if (location.equals("台北車站")){
                        stamp.setImageResource(R.drawable.p1);
                    }
                    else if (location.equals("北投站")){
                        stamp.setImageResource(R.drawable.p2);
                    }
                    else{
                        stamp.setImageResource(R.drawable.p3);
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
                            image1.setImageBitmap (result);
                            super.onPostExecute(result);
                        }
                    }.execute(picture[0]);
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
                            image2.setImageBitmap (result);
                            super.onPostExecute(result);
                        }
                    }.execute(picture[1]);
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
                            image3.setImageBitmap (result);
                            super.onPostExecute(result);
                        }
                    }.execute(picture[2]);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beitou);
        mTable = new Table("http://172.20.10.7:8000/api", "memory", "Secondteam", "secondteam12345");
        pager = (ViewPager) findViewById(R.id.pager);

        LayoutInflater li = getLayoutInflater().from(this);
        View v1 = li.inflate(R.layout.fragment_beitou, null);
        View v2 = li.inflate(R.layout.fragment_taipei, null);
        pagerList = new ArrayList<View>();
        pagerList.add(v1);
        pagerList.add(v2);

        Bundle bundle=getIntent().getExtras();
        write=bundle.getBoolean("Write");
        //pager.setAdapter(new myViewPagerAdapter(pagerList));
        //pager.setCurrentItem(0);
        if (write) {
            View v3=li.inflate(R.layout.fragment_emptymemory,null);
            pagerList.add(v3);
            eptitle=v3.findViewById(R.id.emptytitle);
            eptext=v3.findViewById(R.id.emptytext);
            image1=v3.findViewById(R.id.emptyimage);
            image2=v3.findViewById(R.id.emptyimage2);
            image3=v3.findViewById(R.id.emptyimage3);
            stamp=v3.findViewById(R.id.emptyimage4);
            pager.setAdapter(new myViewPagerAdapter(pagerList));
            pager.setCurrentItem(2);
            Thread t1=new Thread(r1);
            t1.start();
        }
        else {
            pager.setAdapter(new myViewPagerAdapter(pagerList));
            pager.setCurrentItem(0);
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
