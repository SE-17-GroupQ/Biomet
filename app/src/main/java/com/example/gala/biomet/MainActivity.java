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

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final String ENROLL = "enroll";

    private static final String EMPTY_UNITY_ID = "Unity Id cannot be blank";
    private ImageView imageView;
    public static String apiService;
    private Bitmap bitmapPhoto;
    private TextView status;
    public static EditText subjectText;
    private String subject_id;

    private AlertDialog alertDialog;

    static final String TAG = "Yo Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button markAttendanceButton = (Button) findViewById(R.id.markAttendanceButton);
        Button registerButton = (Button) findViewById(R.id.registerButton);
        Button captureImage = (Button) findViewById(R.id.captureImage);

        imageView = (ImageView) findViewById(R.id.imageView);
        //subjectText = (EditText) findViewById(R.id.unity_id);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                final Dialog dialog = new Dialog(MainActivity.this);
                Log.w(TAG,"1");
                //View popupView = layoutInflater.inflate(R.layout.activity_popup_register, null);
                //The view is been set up by the setContentView
                dialog.setContentView(R.layout.activity_popup_register);
                dialog.setTitle("Registration");
                Log.w(TAG,"2");
                apiService = ((Button)v).getText().toString().toLowerCase();
                Log.w(TAG,"3");
                Log.w(TAG,apiService);
                subjectText = (EditText) dialog.findViewById(R.id.unity_id);

                if(apiService.equals(ENROLL))
                {
                    Log.w(TAG,"5");

                    Log.w(TAG,"6");
                    //subject_id = subjectText.getText().toString();
                    subject_id = subjectText.getText().toString();

                    if(subject_id.matches("")) {
                        Log.i(TAG, "not null, silver");
                    }
                    Log.w(TAG,"7");
                }
                Button dialogButtonClose = (Button)dialog.findViewById(R.id.popupClose);
                dialogButtonClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Log.w(TAG,"before dialog");
                dialog.show();
            }
        });

        if (!hasCamera()) {
            markAttendanceButton.setEnabled(false);
            registerButton.setEnabled(false);
        }
        alertDialog = AlertDialogHelper.buildAlertDialog(MainActivity.this, "Alert", "OK");
    }

    //Check if the user has the camera
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //Launching the camera
    public void launchCamera(View view) {
        Log.i(TAG, ((Button)view).getText().toString());
        if(subject_id.matches("")) {
            Log.i(TAG, "not null, silver");
            Log.i(TAG, subjectText.getText().toString());
        }

        subject_id = subjectText.getText().toString();
        if(apiService.matches(ENROLL)) {
            if (subject_id.matches("")) {
                Log.w(TAG, "ealfjjbwefkjwjbs");
                alertDialog.setMessage(EMPTY_UNITY_ID);
                alertDialog.show();
                return;
            }
            Log.i(TAG, subject_id);
        }
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
            //imageView.setImageBitmap(photo);//Just for reference
            bitmapPhoto = photo;

            String base64Photo = bitmapToBase64(photo);

            try {
                subjectText.setText("");
                alertDialog.setMessage(apiService + " in progress..");
                /*for(Button b : buttons) {
                    b.setEnabled(false);
                }*/
                Log.w(TAG,"before alert dialog");
                alertDialog.show();
                new ApiRelated().execute(base64Photo);
            } catch(Exception e) {
                Log.i(TAG, "ApiRelated problem : "+e.getMessage());
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //Conversion from Bitmap to Base64
    private String bitmapToBase64(Bitmap photo) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}