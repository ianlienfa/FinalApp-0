package com.example.finalapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import nctu.fintech.appmate.Table;
import nctu.fintech.appmate.Tuple;

public class ChatRoom extends AppCompatActivity {

    ImageButton b1;
    ImageButton b2;
    EditText et1;
    TextView tv1;
    Table mTable;
    String word2send;
    String s1;
    String username;

    private Runnable r1 = new Runnable(){
        public void run()
        {
            Tuple tuple_add = new Tuple();
            tuple_add.put("Text", word2send);
            tuple_add.put("Name", username);
            try {
                mTable.add(tuple_add);
            }catch (IOException e) {
                Log.e("Error", "Fail to put");
            }
        }
    };

    private TimerTask task = new TimerTask(){
        public void run() {
            Thread t2 = new Thread(r2);
            t2.start();
        }
    };

    private Runnable r2 = new Runnable(){
        public void run()
        {
            try {
                Tuple tuple_get[] = mTable.get();
                s1 = "";
                for(int i=0; i<tuple_get.length; i++){
                    String tempString = tuple_get[i].get("Text");
                    String tempnameString=tuple_get[i].get("Name");
                    if (tempnameString==null||tempnameString.equals("")){
                        tempnameString="匿名";
                    }
                    if(tempString!=null && !tempString.equals("")) {
                        s1 = tempnameString+":"+tempString  + "\n" + s1;
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
                    tv1.setText(s1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        b1 = (ImageButton)findViewById(R.id.send);
        b2 = (ImageButton)findViewById(R.id.record);
        et1 = (EditText)findViewById(R.id.editText);
        tv1 = (TextView)findViewById(R.id.textView);
        mTable = new Table("http://172.20.10.7:8000/api", "chatroom", "Secondteam", "secondteam12345");
        Bundle bundle = getIntent().getExtras();
        username=bundle.getString("Username");

        b1.setOnTouchListener(touchlistener);
        b2.setOnTouchListener(touchlistener);

        b1.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                word2send = et1.getText().toString();
                et1.setText("");
                Thread t1 = new Thread(r1);
                t1.start();
            }
        });

        b2.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                //設定辨識語言(這邊設定的是繁體中文)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh-TW");
                //設定語音辨識視窗的內容
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Listening...");
                startActivityForResult(intent, 1);

            }
        });

        Timer timer01 = new Timer();
        timer01.schedule(task, 0, 1000);
    }

    ImageButton.OnTouchListener touchlistener = new ImageButton.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                switch(v.getId()){
                    case R.id.send:
                        Animation animation= AnimationUtils.loadAnimation(ChatRoom.this, R.anim.pressdown);
                        b1.startAnimation(animation);
                        break;
                    case R.id.record:
                        Animation animation1= AnimationUtils.loadAnimation(ChatRoom.this, R.anim.pressdown);
                        b2.startAnimation(animation1);
                        break;
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                switch(v.getId()){
                    case R.id.send:
                        Animation animation= AnimationUtils.loadAnimation(ChatRoom.this, R.anim.pressup);
                        b1.startAnimation(animation);
                        break;
                    case R.id.record:
                        Animation animation1= AnimationUtils.loadAnimation(ChatRoom.this, R.anim.pressup);
                        b2.startAnimation(animation1);
                        break;
                }

            }
            return false;
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        //用來儲存最後的辨識結果
        String firstMatch;
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //取出多個辨識結果並儲存在String的ArrayList中
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            firstMatch = (String) result.get(0);
            et1.setText(firstMatch);
        } else {
            firstMatch = "無法辨識";
        }
    }
}
