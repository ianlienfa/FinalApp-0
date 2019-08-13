package com.example.finalapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

public class TimeLimitEventActivity extends AppCompatActivity {
    Button gogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_limit_event);
        gogo=findViewById(R.id.GoGoButton);
        gogo.setOnTouchListener(touchlistener);
        gogo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("https://www.mocataipei.org.tw/tw/ExhibitionAndEvent/Info/%E5%B0%91%E5%B9%B4%E7%95%B6%E4%BB%A3%E2%94%80%E6%9C%AA%E7%B5%82%E7%B5%90%E7%9A%84%E9%81%8E%E5%8E%BB%E9%80%B2%E8%A1%8C%E5%BC%8F");
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
    }

    Button.OnTouchListener touchlistener = new ImageButton.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Animation animation= AnimationUtils.loadAnimation(TimeLimitEventActivity.this, R.anim.pressdown);
                switch(v.getId()){
                    case R.id.GoGoButton:
                        gogo.startAnimation(animation);
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Animation animation= AnimationUtils.loadAnimation(TimeLimitEventActivity.this, R.anim.pressup);
                switch(v.getId()){
                    case R.id.GoGoButton:
                        gogo.startAnimation(animation);
                }

            }
            return false;
        }
    };
}
