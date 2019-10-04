package com.example.finalapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

public class TaipeiBeforeNavagationActivity extends AppCompatActivity implements View.OnClickListener {
    Button nanazui;
    Button thumb;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //status bar 透明化
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){ // 4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); // 確認取消半透明設置。
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 全螢幕顯示，status bar 不隱藏，activity 上方 layout 會被 status bar 覆蓋。
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE); // 配合其他 flag 使用，防止 system bar 改變後 layout 的變動。
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); // 跟系統表示要渲染 system bar 背景。
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_taipei_before_navagation);
        Bundle bundle=getIntent().getExtras();
        username=bundle.getString("Username");
        nanazui=findViewById(R.id.nanazui);
        nanazui.setOnClickListener(this);
        nanazui.setOnTouchListener(touchlistener);
        thumb=findViewById(R.id.thumbs_up);
        thumb.setOnTouchListener(touchlistener);
    }

    Button.OnTouchListener touchlistener = new ImageButton.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Animation animation = AnimationUtils.loadAnimation(TaipeiBeforeNavagationActivity.this, R.anim.pressdown);
                switch (v.getId()) {
                    case R.id.nanazui:
                        nanazui.startAnimation(animation);
                        break;
                    case R.id.thumbs_up:
                        thumb.startAnimation(animation);
                        break;
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Animation animation = AnimationUtils.loadAnimation(TaipeiBeforeNavagationActivity.this, R.anim.pressdown);
                switch (v.getId()) {
                    case R.id.nanazui:
                        nanazui.startAnimation(animation);
                        break;
                    case R.id.thumbs_up:
                        thumb.startAnimation(animation);
                        break;
                }
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.nanazui:
                Intent intent=new Intent(TaipeiBeforeNavagationActivity.this,NanazuiActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.thumbs_up:
                Intent intent2=new Intent(TaipeiBeforeNavagationActivity.this,RecommendActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle2=new Bundle();
                bundle2.putString("Username",username);
                intent2.putExtras(bundle2);
                startActivity(intent2);
                break;
        }
    }
}
