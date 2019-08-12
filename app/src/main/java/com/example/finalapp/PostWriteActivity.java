package com.example.finalapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.Date;

import nctu.fintech.appmate.Table;
import nctu.fintech.appmate.Tuple;

public class PostWriteActivity extends AppCompatActivity implements View.OnClickListener {
    Table mTable;
    EditText title;
    EditText text;
    ImageButton photo;
    ImageButton photo2;
    ImageButton photo3;
    ImageButton send;
    int count=0;
    RatingBar star;
    int ratingNumber;
    String username;
    String landmark;
    String comment;
    String Star;
    Uri uri;
    Bitmap bitmap[]=new Bitmap[3];
    boolean photofound[]={false,false,false};
    String filename="http://192.168.1.1:8000/media";

    private Runnable r1 = new Runnable(){
        public void run()
        {
            Tuple tuple_add = new Tuple();
            tuple_add.put("Username",username);
            tuple_add.put("Star",Star);
            tuple_add.put("Landmark",landmark);
            tuple_add.put("Comment",comment);
            if (photofound[0])
                tuple_add.put("Picture1",filename,bitmap[0]);
            if (photofound[1])
                tuple_add.put("Picture2",filename,bitmap[1]);
            if (photofound[2])
                tuple_add.put("Picture3",filename,bitmap[2]);
            try {
                mTable.add(tuple_add);
            }catch (IOException e) {
                Log.e("Error", "Fail to put");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);
        Bundle bundle=getIntent().getExtras();
        username=bundle.getString("Username");
        mTable = new Table("http://172.20.10.7:8000/api", "comment", "Secondteam", "secondteam12345");
        star = (RatingBar) findViewById(R.id.ratingBar); // initiate a rating bar
        title=(EditText)findViewById(R.id.writetitle);
        text=(EditText)findViewById(R.id.writetext);
        photo=(ImageButton)findViewById(R.id.selectphoto);
        photo.setOnClickListener(this);
        photo.setOnTouchListener(touchlistener);
        photo2=(ImageButton)findViewById(R.id.selectphoto2);
        photo2.setOnClickListener(this);
        photo2.setOnTouchListener(touchlistener);
        photo3=(ImageButton)findViewById(R.id.selectphoto3);
        photo3.setOnClickListener(this);
        photo3.setOnTouchListener(touchlistener);
        send=(ImageButton)findViewById(R.id.sendwrite);
        send.setOnClickListener(this);
        send.setOnTouchListener(touchlistener);
    }

    ImageButton.OnTouchListener touchlistener = new ImageButton.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Animation animation = AnimationUtils.loadAnimation(PostWriteActivity.this, R.anim.pressdown);
                switch (v.getId()) {
                    case R.id.selectphoto:
                        photo.startAnimation(animation);
                        break;
                    case R.id.selectphoto2:
                        photo2.startAnimation(animation);
                        break;
                    case R.id.selectphoto3:
                        photo3.startAnimation(animation);
                        break;
                    case R.id.sendwrite:
                        send.startAnimation(animation);
                        break;
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Animation animation = AnimationUtils.loadAnimation(PostWriteActivity.this, R.anim.pressup);
                switch (v.getId()) {
                    case R.id.selectphoto:
                        photo.startAnimation(animation);
                        break;
                    case R.id.selectphoto2:
                        photo2.startAnimation(animation);
                        break;
                    case R.id.selectphoto3:
                        photo3.startAnimation(animation);
                        break;
                    case R.id.sendwrite:
                        send.startAnimation(animation);
                        break;
                }

            }
            return false;
        }
    };

    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(PostWriteActivity.this);
    }

    //拍照完畢或選取圖片後呼叫此函式
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                if (count==1) {
                    photo.setImageURI(imageUri);
                    photofound[0]=true;
                    uri=imageUri;
                    if (uri != null) {
                        try {
                            bitmap[0] = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if (count==2){
                    photo2.setImageURI(imageUri);
                    photofound[1]=true;
                    uri=imageUri;
                    if (uri != null) {
                        try {
                            bitmap[1] = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if (count==3){
                    photo3.setImageURI(imageUri);
                    photofound[2]=true;
                    uri=imageUri;
                    if (uri != null) {
                        try {
                            bitmap[2] = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.selectphoto:
                count=1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(PostWriteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PostWriteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        BringImagePicker();
                    }
                } else {
                    BringImagePicker();
                }
                break;
            case R.id.selectphoto2:
                count=2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(PostWriteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PostWriteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        BringImagePicker();
                    }
                } else {
                    BringImagePicker();
                }
                break;
            case R.id.selectphoto3:
                count=3;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(PostWriteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PostWriteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        BringImagePicker();
                    }
                } else {
                    BringImagePicker();
                }
                break;
            case R.id.sendwrite:
                landmark=title.getText().toString();
                comment=text.getText().toString();
                ratingNumber = (int)star.getRating(); // get rating number from a rating bar
                Star=Integer.toString(ratingNumber);
                star.setRating(0);
                title.setText("");
                text.setText("");
                photo.setImageResource(R.drawable.addphoto);
                photo2.setImageResource(R.drawable.addphoto);
                photo3.setImageResource(R.drawable.addphoto);
                Thread t1=new Thread(r1);
                t1.start();
        }
    }
}
