package com.art4muslim.zedalmouhajer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.art4muslim.zedalmouhajer.BaseApplication;
import com.art4muslim.zedalmouhajer.R;
import com.art4muslim.zedalmouhajer.session.Constants;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by macbook on 21/12/2017.
 */

public class SavePhotoTask extends AsyncTask<SavePhotoParams, Void, Void> {

    Context cnx;
    String name;
    String youtubelink;
    String details;
    String ass_id;
    Bitmap image;
    LinearLayout _linearLayout;
    ProgressBar _progressBar;
    private static String TAG = SavePhotoTask.class.getSimpleName();

    @Override
    protected Void doInBackground(SavePhotoParams... myTaskParams) {

        cnx   = myTaskParams[0].cnx;
        image = myTaskParams[0].image;
        name  = myTaskParams[0].name;
        youtubelink  = myTaskParams[0].youtubelink;
        details  = myTaskParams[0].details;
        ass_id  = myTaskParams[0].ass_id;
        _linearLayout  = myTaskParams[0]._linearLayout;
        _progressBar  = myTaskParams[0]._progressBar;
        saveImage();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.e(TAG,"save photo done");
    }

    private void saveImage() {

     //   final String requestBody = jsonObject.toString();

        String url = Constants.INSERT_NEW_SUBJECT;
        Log.e(TAG,"url  = "+url);
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //    String resultResponse = new String(response.data);
                Log.e("response !!!!!!!! ", "saveImage response #######" + response.toString());
                JSONObject jsonObject  ;
                boolean status = false;
                try {
                    jsonObject = new JSONObject(response);
                    status = jsonObject.getBoolean("status");
                    String msg = jsonObject.getString("message");
                    if (status) {
                        AlertDialogManager.showAlertDialog(cnx,cnx.getResources().getString(R.string.app_name),msg,true,0);


                    } else {


                        _linearLayout.setVisibility(View.VISIBLE);
                        AlertDialogManager.showAlertDialog(cnx,cnx.getResources().getString(R.string.app_name),msg,false,0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (status) {

                }
                _progressBar.setVisibility(View.GONE);
                _linearLayout.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    Log.e("Error result", result);

                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {

         /*   @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                String accessId = BaseApplication.session.getAccessToken();
                if(Constants.androiStudioMode.equals("debug")){
                    Log.v("accessid", accessId);}
                headers.put("X-Auth-Token", "" + accessId);
                return headers;
            }
            */
         @Override
         protected Response<String> parseNetworkResponse(NetworkResponse response) {
             try {
                 String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                 return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
             } catch (UnsupportedEncodingException e) {
                 return Response.error(new ParseError(e));
             }
         }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                params.put("photo", new DataPart("image_new.jpg", Utils.getFileDataFromDrawable(cnx, image), "image/jpeg"));

                return params;
            }
           @Override
            protected Map<String,String> getParams(){
               Map<String, String> params = new HashMap<>();
               params.put("name", name);
               params.put("youtubelink", youtubelink);
               params.put("details", ""+ details);
               params.put("ass_id", ""+ ass_id);
               Log.e(TAG,"getParams name = "+name);
               Log.e(TAG,"getParams youtubelink = "+youtubelink);
               Log.e(TAG,"getParams ass_id = "+ass_id);
               Log.e(TAG,"getParams details = "+details);

                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return super.getBody();
            }
        };

        BaseApplication.getInstance().addToRequestQueue(multipartRequest);
    }

}
