package com.example.finalapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import nctu.fintech.appmate.Table;
import nctu.fintech.appmate.Tuple;

public class MemoryWriteActivity extends AppCompatActivity implements View.OnClickListener {
    String location;
    String temptext;
    EditText text;
    ImageButton photo;
    ImageButton photo2;
    ImageButton photo3;
    ImageButton send;
    TextView title;
    Uri uri;
    int count=0;
    String bitmap[]={null,null,null};
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_write);
        Bundle bundle=getIntent().getExtras();
        location=bundle.getString("Location");
        username=bundle.getString("Username");
        title=(TextView)findViewById(R.id.memorywriteTitle);
        title.setText("撰寫-"+location);
        text=(EditText)findViewById(R.id.memorywritetext);
        photo=(ImageButton)findViewById(R.id.memoryselectphoto);
        photo.setOnClickListener(this);
        photo.setOnTouchListener(touchlistener);
        photo2=(ImageButton)findViewById(R.id.memoryselectphoto2);
        photo2.setOnClickListener(this);
        photo2.setOnTouchListener(touchlistener);
        photo3=(ImageButton)findViewById(R.id.memoryselectphoto3);
        photo3.setOnClickListener(this);
        photo3.setOnTouchListener(touchlistener);
        send=(ImageButton)findViewById(R.id.memorysendwrite);
        send.setOnClickListener(this);
        send.setOnTouchListener(touchlistener);
    }

    ImageButton.OnTouchListener touchlistener = new ImageButton.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Animation animation = AnimationUtils.loadAnimation(MemoryWriteActivity.this, R.anim.pressdown);
                switch (v.getId()) {
                    case R.id.memoryselectphoto:
                        photo.startAnimation(animation);
                        break;
                    case R.id.memorysendwrite:
                        send.startAnimation(animation);
                        break;
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Animation animation = AnimationUtils.loadAnimation(MemoryWriteActivity.this, R.anim.pressup);
                switch (v.getId()) {
                    case R.id.memoryselectphoto:
                        photo.startAnimation(animation);
                        break;
                    case R.id.memorysendwrite:
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
                .start(MemoryWriteActivity.this);
    }

    //拍照完畢或選取圖片後呼叫此函式
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if (count == 1) {
                    Uri imageUri = result.getUri();
                    photo.setImageURI(imageUri);
                    bitmap[0]=imageUri.toString();
                }
                else if (count==2){
                    Uri imageUri2 = result.getUri();
                    photo2.setImageURI(imageUri2);
                    bitmap[1]=imageUri2.toString();
                }else if (count==3){
                    Uri imageUri3 = result.getUri();
                    photo3.setImageURI(imageUri3);
                    bitmap[2]=imageUri3.toString();
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.memoryselectphoto:
                count=1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(MemoryWriteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MemoryWriteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        BringImagePicker();
                    }
                } else {
                    BringImagePicker();
                }
                break;
            case R.id.memoryselectphoto2:
                count=2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(MemoryWriteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MemoryWriteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        BringImagePicker();
                    }
                } else {
                    BringImagePicker();
                }
                break;
            case R.id.memoryselectphoto3:
                count=3;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(MemoryWriteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MemoryWriteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        BringImagePicker();
                    }
                } else {
                    BringImagePicker();
                }
                break;
            case R.id.memorysendwrite:
                temptext=text.getText().toString();
                Intent intent=new Intent(MemoryWriteActivity.this,BeitouActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("Write",true);
                bundle2.putString("Location",location);
                bundle2.putString("Text",temptext);
                bundle2.putStringArray("Bitmap",bitmap);
                intent.putExtras(bundle2);
                startActivity(intent);
                break;
        }
    }
}
