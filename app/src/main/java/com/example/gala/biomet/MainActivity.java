package com.example.gala.biomet;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    static final String API_LINK = "https://api.kairos.com";
    private static final String IMAGE = "image";
    private static final String GALLERY_NAME = "gallery_name";
    private static final String GALLERY_VAL = "se17";
    private static final String REGISTER_STUDENT = "register student";


    private static final String EMPTY_UNITY_ID = "Unity Id cannot be blank";
    private ImageView imageView;
    private String apiService;
    private Bitmap bitmapPhoto;
    private TextView status;
    private EditText subjectText;
    private String subject_id;

    private AlertDialog alertDialog;

    static final String TAG = "Yo Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button markAttendanceButton = (Button) findViewById(R.id.markAttendanceButton);
        Button registerButton = (Button) findViewById(R.id.registerButton);

        imageView = (ImageView) findViewById(R.id.imageView);
        //subjectText = (EditText) findViewById(R.id.unity_id);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                final Dialog dialog = new Dialog(MainActivity.this);
                Log.w(TAG,"1");
                //View popupView = layoutInflater.inflate(R.layout.activity_popup_register, null);
                dialog.setContentView(R.layout.activity_popup_register);
                dialog.setTitle("Registration");
                Log.w(TAG,"2");
                apiService = ((Button)v).getText().toString().toLowerCase();
                Log.w(TAG,"3");
                Log.w(TAG,apiService);
                subjectText = (EditText) dialog.findViewById(R.id.unity_id);

                if(apiService.equals(REGISTER_STUDENT))
                {
                    Log.w(TAG,"5");

                    Log.w(TAG,"6");
                    //subject_id = subjectText.getText().toString();
                    subject_id = subjectText.getText().toString();
                    Log.w(TAG,"7");
                    /*if(subject_id.matches(""))
                    {
                        Log.w(TAG,"ealfjjbwefkjwjbs");
                        alertDialog.setMessage(EMPTY_UNITY_ID);
                        alertDialog.show();
                        return;
                    }*/
                    Log.w(TAG,"8");
                    /*Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(photoIntent,REQUEST_IMAGE_CAPTURE);*/
                }
                Button dialogButtonClose = (Button)dialog.findViewById(R.id.popupClose);
                dialogButtonClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        if (!hasCamera()) {
            markAttendanceButton.setEnabled(false);
            registerButton.setEnabled(false);
        }
    }

    //Check if the user has the camera
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //Launching the camera
    public void launchCamera(View view) {
        Log.i(TAG, ((Button)view).getText().toString());
        Log.i(TAG, subjectText.getText().toString());
        apiService = ((Button) view).getText().toString().toLowerCase();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Take a picture and pass results along to onActivityResults
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }
    //If you want to return the image taken

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Get the photo
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            imageView.setImageBitmap(photo);//Just for reference
            bitmapPhoto = photo;

            String base64Photo = bitmapToBase64(photo);
        }

    }
    //Conversion from Bitmap to Base64
    private String bitmapToBase64(Bitmap photo) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}