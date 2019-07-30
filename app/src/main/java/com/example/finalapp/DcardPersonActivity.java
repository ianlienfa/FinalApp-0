package com.example.finalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DcardPersonActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv1;
    Button follow;
    private int[] imagesId={R.drawable.person4,R.drawable.person4,R.drawable.person4,R.drawable.person4,R.drawable.person4,R.drawable.person4,R.drawable.person4,R.drawable.person4};
    private	String[] names={"台北動物園攻略","倒數101煙火","公館美食推薦","小巨蛋追星","台大一日遊","狗狗猩猩大冒險","大象好可愛","動物沒熱死,我先死了!" };
    private int[] images={R.drawable.panda2,R.drawable.firework,R.drawable.gg23,R.drawable.eggstar,R.drawable.taida,R.drawable.zoopic3,R.drawable.elephant,R.drawable.zoopic4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcard_person);
        lv1 = (ListView) findViewById(R.id.listView1);
        follow=(Button)findViewById(R.id.follow);
        follow.setOnClickListener(this);
        follow.setOnTouchListener(touchlistener);

        BaseAdapter adapter = new BaseAdapter() {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO 自动生成的方法存根
                View layout=View.inflate(DcardPersonActivity.this, R.layout.list_item, null);
                ImageView face = (ImageView)layout.findViewById(R.id.face);
                TextView name =(TextView)layout.findViewById(R.id.name);
                ImageView face2 = (ImageView)layout.findViewById(R.id.face2);


                face.setImageResource(imagesId[position]);
                name.setText(names[position]);
                face2.setImageResource(images[position]);
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
                return names.length;
            }
        };///new BaseAdapter()

        lv1.setAdapter(adapter);

        /*lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
               ListView listView = (ListView) arg0;
                Toast.makeText(
                        MainActivity.this,
                        "ID：" + arg3 +
                                "   選單文字："+ listView.getItemAtPosition(arg2).toString(),
                        Toast.LENGTH_LONG).show();
                if (arg3 == 0) {
                    Intent intent = new Intent(DcardPersonActivity.this, DcardPostActivity.class);
                    startActivity(intent);
                }


            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.follow:
                follow.setText("已追蹤");
                follow.setBackgroundResource(R.drawable.pressfollow_shape);
                break;
        }
    }

    ImageButton.OnTouchListener touchlistener = new ImageButton.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                switch (v.getId()) {
                    case R.id.follow:
                        Animation animation = AnimationUtils.loadAnimation(DcardPersonActivity.this, R.anim.pressdown);
                        follow.startAnimation(animation);
                        break;
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                switch (v.getId()) {
                    case R.id.follow:
                        Animation animation = AnimationUtils.loadAnimation(DcardPersonActivity.this, R.anim.pressup);
                        follow.startAnimation(animation);
                        break;
                }

            }
            return false;
        }
    };
}
