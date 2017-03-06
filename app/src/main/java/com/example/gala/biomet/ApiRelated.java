package com.example.gala.biomet;

import android.app.VoiceInteractor;
import android.os.AsyncTask;
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

public class ApiRelated extends AsyncTask<String, String, String>{

    private final OkHttpClient client = new OkHttpClient();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
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


    private String subject_id;
    private String apiService;
    private EditText subjectText;
    private TextView status;

    protected String doInBackground(String... params) {
        //This is the return message in the end of this message
        String returnMsg = "";
        //
        String base64Photo = params[0];
        JSONObject jsonObject = new JSONObject();
        //Log.i(TAG, "Kairos API call :\nSERVICE : "+apiService+" GALLERY : "+GALLERY_VALUE);
        try {
            jsonObject.putOpt(IMAGE, base64Photo);
            jsonObject.putOpt(GALLERY_NAME,GALLERY_VAL);
            if(apiService.equals(ENROLL)){
               // Log.i(TAG, "SUBJECT : "+subject_id);
                jsonObject.putOpt(SUBJECT_ID, subject_id);
            }
        }
        catch(JSONException jerror){
            //Log.i(TAG, "Problem in adding parameters to JSON");
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
                //Log.i(TAG, "Response successful");
                String responseJsonString = response.body().string();
                try{
                    JSONObject responseJson = new JSONObject(responseJsonString);
                    //Log.i(TAG, "Response JSON : "+responseJsonString);
                    if(responseJson.has(ERRORS)) {
                        String apiError = ((JSONObject)responseJson.getJSONArray(ERRORS).get(0)).getString(MESSAGE);
                        //Log.i(TAG, "Kairos Error : "+kairosError);
                        returnMsg = apiService+" : "+apiError;
                    }
                    else {
                        JSONObject transaction =((JSONObject)responseJson.getJSONArray(IMAGES).get(0))
                                .getJSONObject(TRANSACTION);
                        String kairosStatus = transaction.getString(STATUS);
                        String subject = transaction.getString(SUBJECT_ID);
                        //Log.i(TAG, "Kairos Status : "+kairosStatus);
                        returnMsg = apiService+" "+subject+" : "+kairosStatus;
                    }
                } catch(JSONException j) {
                    //Log.i(TAG, "Problem in parsing response JSON");
                    j.printStackTrace();
                }
            }
            else {
                //Log.i(TAG, "Response failed : "+response.code());
            }
        } catch(IOException i) {
            //Log.i(TAG, "Problem in :\n1. Executing Kairon POST request\nOR\n2.Getting body of response");
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
}
