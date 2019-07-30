package com.example.finalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ZooActivity extends AppCompatActivity {

    private ListView lv1;

    private String[] usernames={"Ian","謙謙","Gorilla","Penguin","毛毛","花花","泡泡","魔人啾啾"};
    private int[] imagesId={R.drawable.person8,R.drawable.person7,R.drawable.person6,R.drawable.person5,R.drawable.person4,R.drawable.person3,R.drawable.person2,R.drawable.person1};
    private	String[] names={"木柵動物園一日遊","家庭旅遊好去處","Taipei zoo","團團圓圓","木柵動物園旅遊攻略","猩猩大冒險","猩猩好可愛","動物沒熱死,我先死了!" };
    private int[] images={R.drawable.panda2,R.drawable.elephant,R.drawable.zoopic1,R.drawable.panda1,R.drawable.zoopic2,R.drawable.zoopic3,R.drawable.zoopic5,R.drawable.zoopic4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoo);
        lv1 = (ListView) findViewById(R.id.listView1);

        BaseAdapter adapter = new BaseAdapter() {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO 自动生成的方法存根
                View layout=View.inflate(ZooActivity.this, R.layout.list_item2, null);
                ImageView face = (ImageView)layout.findViewById(R.id.face);
                TextView name =(TextView)layout.findViewById(R.id.name);
                ImageView face2 = (ImageView)layout.findViewById(R.id.face2);
                TextView username=(TextView)layout.findViewById(R.id.username);

                username.setText(usernames[position]);
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

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
               /*ListView listView = (ListView) arg0;
                Toast.makeText(
                        MainActivity.this,
                        "ID：" + arg3 +
                                "   選單文字："+ listView.getItemAtPosition(arg2).toString(),
                        Toast.LENGTH_LONG).show();*/
                if (arg3 == 0) {
                    Intent intent = new Intent(ZooActivity.this, DcardPostActivity.class);
                    startActivity(intent);
                }


            }
        });
    }
}