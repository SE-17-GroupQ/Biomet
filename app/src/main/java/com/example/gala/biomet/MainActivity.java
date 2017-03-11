package com.example.gala.biomet;


import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.gala.biomet.R.id.captureImage;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String Kairos_webapi = "https://api.kairos.com";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    static final String KAIROS_DOMAIN = "https://api.kairos.com";
    private static final String IMAGE = "image";
    private static final String GALLERY_NAME = "gallery_name";
    private static final String GALLERY_VAL = "se17";
    private static final String ENROLL = "enroll";
    private static final String SUBJECT_ID = "subject_id";
    private static final String APP_ID_VALUE = "01ed2a33";
    private static final String APP_KEY_VALUE = "b6043be30810c2de07daefe36e89cb20";

    private static final String CONTENT = "Content-Type";
    private static final String CONTENT_TYPE = "application/json";
    private static final String APP_ID = "app_id";
    private static final String APP_KEY = "app_key";
    private static final String ERRORS = "Errors";
    private static final String MESSAGE = "Message";
    private static final String IMAGES = "images";
    private static final String TRANSACTION = "transaction";
    private static final String STATUS = "status";
    private static final String CONNECTION_PROBLEM = "Connection problem. Please try again.";

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
        subjectText = (EditText) findViewById(R.id.unity_id);
        Button markAttendance = (Button) findViewById(R.id.markAttendance);

        Button registerButton = (Button) findViewById(R.id.registerButton);

        imageView = (ImageView) findViewById(R.id.imageView);

        /*markAttendance.setOnClickListener(new View.OnClickListener() {
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

                *//*Button dialogButtonClose = (Button)dialog.findViewById(R.id.closePopup);
                dialogButtonClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            dialog.dismiss();
                    }
                });*//*
                dialog.show();
            }
        });*/

        //Button selfie = new Button.findViewById()


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*LayoutInflater is a class used to instantiate layout XML file into its corresponding
                 view objects which can be used in java programs.*/
                LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                /**/
                final Dialog dialog = new Dialog(MainActivity.this);

                View popupView = layoutInflater.inflate(R.layout.activity_popup_register, null);

                dialog.setContentView(R.layout.activity_popup_register);

                dialog.setTitle("Registeration");
                //Add text view and image view

                Button dialogButtonCapture = (Button)dialog.findViewById(captureImage);
                dialogButtonCapture.setEnabled(true);
                /*dialogButtonCapture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        registerButton.setEnabled(true);
                    }
                });*/

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
        //alertDialog = AlertDialogHelper.buildAlertDialog(MainActivity.this, "Alert", "OK");
    }

    //Check if the user has the camera
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //Launching the camera
    public void launchCamera(View view) {
        Log.i(TAG, ((Button)view).getText().toString());
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

    /*public class ApiRelated extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {
            //This is the return message in the end of this message
            String returnMsg = "";
            //
            String base64Photo = params[0];
            JSONObject jsonObject = new JSONObject();
            Log.i(TAG, "Kairos API call :\nSERVICE : "+apiService+" GALLERY : "+GALLERY_VAL);
            try {
                jsonObject.putOpt(IMAGE, base64Photo);
                jsonObject.putOpt(GALLERY_NAME,GALLERY_VAL);
                if(apiService.equals(ENROLL)){
                    Log.i(TAG, "SUBJECT : "+subject_id);
                    jsonObject.putOpt(SUBJECT_ID, subject_id);
                }
            }
            catch(JSONException jerror){
                Log.i(TAG, "Problem in adding parameters to JSON");
                jerror.printStackTrace();
            }
            RequestBody requestBody = RequestBody.create(JSON,jsonObject.toString());
            Request request = new Request.Builder().url(KAIROS_DOMAIN+"/"+apiService).
                    addHeader(APP_ID,APP_ID_VALUE).addHeader(APP_KEY,APP_KEY_VALUE)
                    .addHeader(CONTENT,CONTENT_TYPE).post(requestBody).build();

            Response response = null;
            try{
                response = client.newCall(request).execute();
                if(response != null && response.isSuccessful()) {
                    Log.i(TAG, "Response successful");
                    String responseJsonString = response.body().string();
                    try{
                        JSONObject responseJson = new JSONObject(responseJsonString);
                        Log.i(TAG, "Response JSON : "+responseJsonString);
                        if(responseJson.has(ERRORS)) {
                            String apiError = ((JSONObject)responseJson.getJSONArray(ERRORS).get(0)).getString(MESSAGE);
                            Log.i(TAG, "Kairos Error : "+apiError);
                            returnMsg = apiService+" : "+apiError;
                        }
                        else {
                            JSONObject transaction =((JSONObject)responseJson.getJSONArray(IMAGES).get(0))
                                    .getJSONObject(TRANSACTION);
                            String kairosStatus = transaction.getString(STATUS);
                            String subject = transaction.getString(SUBJECT_ID);
                            Log.i(TAG, "Kairos Status : "+kairosStatus);
                            returnMsg = apiService+" "+subject+" : "+kairosStatus;
                        }
                    } catch(JSONException j) {
                        Log.i(TAG, "Problem in parsing response JSON");
                        j.printStackTrace();
                    }
                }
                else {
                    Log.i(TAG, "Response failed : "+response.code());
                }
            } catch(IOException i) {
                Log.i(TAG, "Problem in :\n1. Executing Kairon POST request\nOR\n2.Getting body of response");
                returnMsg = CONNECTION_PROBLEM;
                i.printStackTrace();
            }

            return returnMsg;
        }
        @Override
        protected void onPostExecute(String s) {
            subjectText.setText("");
            status.setText(s);
            super.onPostExecute(s);
        }

    }*/
}