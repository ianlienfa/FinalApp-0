package com.example.finalapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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
    String location;
    String text;
    String picture[]=new String [3];
    int len;
    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView stamp;
    Uri uri[]=new Uri[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beitou);
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
            eptitle.setText(bundle.getString("Location"));
            eptext.setText(bundle.getString("Text"));
            picture=bundle.getStringArray("Bitmap");
            for (int i=0;i<3;i++){
                if (picture[i]!=null) {
                    uri[i] = Uri.parse(picture[i]);
                }
            }
            image1.setImageURI(uri[0]);
            image2.setImageURI(uri[1]);
            image3.setImageURI(uri[2]);
            if (eptitle.getText().toString().equals("台北車站")){
                stamp.setImageResource(R.drawable.p1);
            }
            else if (eptitle.getText().toString().equals("北投站")){
                stamp.setImageResource(R.drawable.p2);
            }
            else{
                stamp.setImageResource(R.drawable.p3);
            }
            pager.setAdapter(new myViewPagerAdapter(pagerList));
            pager.setCurrentItem(2);
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

}
