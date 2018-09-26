package com.art4muslim.zedalmouhajer.firebase;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.art4muslim.zedalmouhajer.BaseApplication;
import com.art4muslim.zedalmouhajer.R;
import com.art4muslim.zedalmouhajer.session.Constants;
import com.art4muslim.zedalmouhajer.utils.AlertDialogManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.art4muslim.zedalmouhajer.session.Constants.CONSTANT_ASSOCIATION;
import static com.art4muslim.zedalmouhajer.session.Constants.CONSTANT_BEN;
import static com.art4muslim.zedalmouhajer.session.SessionManager.Key_UserID;

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

        String url = Constants.GET_TOKEN_BEN;
        if (BaseApplication.session.isLoggedIn()){
            if (BaseApplication.session.getIsFrom().equals(CONSTANT_ASSOCIATION)){
                url = Constants.GET_TOKEN_ASS;

            }
            sendToken(BaseApplication.session.getUserDetails().get(Key_UserID), token, url);
        }

    }



    private void sendToken(final String id, final String token, String url ) {



        Log.e(TAG, "url "+url);
        Log.e(TAG, "id   "+id);
        Log.e(TAG, "token "+token);


        StringRequest LoginFirstRequest = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.v(TAG,"response send token to server == " +response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof AuthFailureError) {

                   // AlertDialogManager.showAlertDialog(activity,activity.getResources().getString(R.string.app_name),activity.getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                  //  AlertDialogManager.showAlertDialog(activity,activity.getResources().getString(R.string.app_name),activity.getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                   // AlertDialogManager.showAlertDialog(activity,activity.getResources().getString(R.string.networkerror),activity.getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                   // AlertDialogManager.showAlertDialog(activity,activity.getResources().getString(R.string.app_name),activity.getResources().getString(R.string.timeouterror),false,3);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("token", token);

                return params;
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {

                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };


        // Adding request to request queue
        BaseApplication.getInstance().addToRequestQueue(LoginFirstRequest);


    }
}
