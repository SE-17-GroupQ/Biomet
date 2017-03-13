package com.example.gala.biomet;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Gala on 3/6/2017.
 */


public class ApiRelated extends AsyncTask<String, String, String> {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private final OkHttpClient client = new OkHttpClient();

    static final String TAG = "Yo ApiRelated Activity";

    private AlertDialog alertDialog;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final String CONTENT = "Content-Type";
    private static final String CONTENT_TYPE = "application/json";
    private static final String CONNECTION_PROBLEM = "Connection problem. Please try again.";

    //Kaisros Api Constants
    private static final String API_LINK = "https://api.kairos.com";
    public static final String RECOGNIZE = "recognize";
    private static final String ENROLL = "enroll";
    private static final String APP_ID = "app_id";
    private static final String APP_KEY = "app_key";
    private static final String IMAGE = "image";
    private static final String SUBJECT_ID = "subject_id";
    private static final String GALLERY_NAME = "gallery_name";
    private static final String IMAGES = "images";
    private static final String ERRORS = "Errors";
    private static final String MESSAGE = "Message";
    private static final String TRANSACTION = "transaction";
    private static final String STATUS = "status";
    public static final String SUCCESS = "success";
    public static final String MESSAGE_FAILURE = "message";

    private static final String GALLERY_VAL = "se17";
    private static final String APP_ID_VALUE = "01ed2a33";
    private static final String APP_KEY_VALUE = "b6043be30810c2de07daefe36e89cb20";

    //Kaisros Api Constants

    private static final String EMPTY_UNITY_ID = "Unity Id cannot be blank";
    private String subject_id;
    //private String apiService;
    //private EditText subjectText;
    private TextView status;


    protected String doInBackground(String... params) {
        //This is the return message in the end of this message
        String returnMsg = "";
        String base64Photo = params[0];
        JSONObject jsonObject = new JSONObject();
        Log.i(TAG, "API call :\nSERVICE : " + MainActivity.apiService + " \n GALLERY : " + GALLERY_VAL);
        try {
            jsonObject.putOpt(IMAGE, base64Photo);
            jsonObject.putOpt(GALLERY_NAME, GALLERY_VAL);
            if (MainActivity.apiService.equals(ENROLL)) {
                Log.i(TAG, "SUBJECT : " + subject_id);
                jsonObject.putOpt(SUBJECT_ID, subject_id);
            }
        } catch (JSONException jerror) {
            Log.i(TAG, "Problem in adding parameters to JSON");
            jerror.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder().url(API_LINK + "/" + MainActivity.apiService).
                addHeader(APP_ID, APP_ID_VALUE).addHeader(APP_KEY, APP_KEY_VALUE)
                .addHeader(CONTENT, CONTENT_TYPE).post(requestBody).build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                Log.i(TAG, "Response successful");
                String responseJsonString = response.body().string();
                try {
                    JSONObject responseJson = new JSONObject(responseJsonString);
                    Log.i(TAG, "Response JSON : " + responseJsonString);
                    if (responseJson.has(ERRORS)) {
                        String apiError = ((JSONObject) responseJson.getJSONArray(ERRORS).get(0)).getString(MESSAGE);
                        Log.i(TAG, "Kairos Error : " + apiError);
                        returnMsg = MainActivity.apiService + " : " + apiError;
                    } else {
                        JSONObject transaction = ((JSONObject) responseJson.getJSONArray(IMAGES).get(0))
                                .getJSONObject(TRANSACTION);
                        String kairosStatus = transaction.getString(STATUS);
                        String subject = transaction.getString(SUBJECT_ID);
                        Log.i(TAG, "Kairos Status : " + kairosStatus);
                        returnMsg = MainActivity.apiService + " " + subject + " : " + kairosStatus;
                    }
                } catch (JSONException j) {
                    Log.i(TAG, "Problem in parsing response JSON");
                    j.printStackTrace();
                }
            } else {
                Log.i(TAG, "Response failed : " + response.code());
            }
        } catch (IOException i) {
            Log.i(TAG, "Problem in :\n1. Executing Kairon POST request\nOR\n2.Getting body of response");
            returnMsg = CONNECTION_PROBLEM;
            i.printStackTrace();
        }

        return returnMsg;
    }

    @Override
    protected void onPostExecute(String s) {
        /*MainActivity.subjectText.setText("");
        status.setText(s);
        super.onPostExecute(s);*/
    }

}
    /*public void clickPhoto(View v)
    {
        MainActivity.apiService = ((Button)v).getText().toString().toLowerCase();
        if(MainActivity.apiService.equals(ENROLL))
        {
            subject_id = subjectText.getText().toString();
            if(subject_id.matches(""))
            {
                alertDialog.setMessage(EMPTY_UNITY_ID);
                alertDialog.show();
                return;
            }
            Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        }
    }*/