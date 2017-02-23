package com.example.gala.biomet;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String Kairos_webapi = "https://api.kairos.com";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    private ImageView imageView;
    private String apiService;
    private Bitmap bitmapPhoto;

    static final String TAG = "Yo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button markAttendance = (Button) findViewById(R.id.markAttendance);
        Button registerButton = (Button) findViewById(R.id.registerButton);
        imageView = (ImageView) findViewById(R.id.imageView);

        markAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.activity_popup_verify);
                dialog.setTitle("Mark Attendance");
                //Add text view and image view

                Button dialogButtonCapture = (Button)dialog.findViewById(R.id. captureImage);
                dialogButtonCapture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                /*Button dialogButtonClose = (Button)dialog.findViewById(R.id.opup);
                dialogButtonClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            dialog.dismiss();
                    }
                });*/
                dialog.show();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.activity_popup_register);
                dialog.setTitle("Registeration");
                //Add text view and image view

                Button dialogButtonCapture = (Button)dialog.findViewById(R.id. captureImage);
                dialogButtonCapture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

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

        //Disable the button if the user does not have a camera
        if (!hasCamera()) {
            markAttendance.setEnabled(false);
            registerButton.setEnabled(false);
        }
    }

    //Check if the user has the camera
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //Launching the camera
    public void launchCamera(View view) {
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
            //new ApiStuff().execute(base64Photo);
        }

    }
    //Conversion from Bitmap to Base64
    private String bitmapToBase64(Bitmap photo) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

   /* private class ApiStuff extends AsyncTask<String, String, String>
    {
        protected String backgroundStuff(String... params)
        {
            String base64Photo = params[0];
            JSONObject jsonObject = new JSONObject();
            try
            {
                jsonObject.putOpt("image",base64Photo);

            }
            catch (JSONException j){
                Log.i(TAG, "json : "+j.toString());
            }
        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }*/

}