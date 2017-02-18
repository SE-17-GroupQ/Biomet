package com.example.gala.biomet;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.content.pm.PackageInfo;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE =1;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button captureButton = (Button) findViewById(R.id.captureButton);
        imageView = (ImageView) findViewById(R.id.imageView);

        //Disable the button if the user does not have a camera
        if(!hasCamera())
            captureButton.setEnabled(false);
    }

    //Check if the user has the camera
    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //Launching the camera
    public void launchCamera(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Take a picture and pass results along to onActivityResults
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
    }
    //If you want to return the image taken

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            //Get the photo
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            imageView.setImageBitmap(photo);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
    }
}