package com.art4muslim.zedalmouhajer.fragments.contactus;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.art4muslim.zedalmouhajer.BaseApplication;
import com.art4muslim.zedalmouhajer.R;
import com.art4muslim.zedalmouhajer.models.Association;
import com.art4muslim.zedalmouhajer.models.InfoApp;
import com.art4muslim.zedalmouhajer.session.Constants;
import com.art4muslim.zedalmouhajer.utils.AlertDialogManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment2 extends Fragment {


    Association association;
    View v;
    TextView _txt_title, txt_mobile, txt_text;
    LinearLayout _linear_for_ass;
    ImageView _img_gplus, _img_youtube, _img_insta, _img_twitter , _img_fcb;
    EditText _edt_name, _edtphone, edt_text;
    Button _btn_send;
    BaseApplication app;
    ProgressBar _progressBar;
    LinearLayout _linearLayout;
    String from = "ASSOCIATION";
    String fcbUrl, instaUrl, twitterUrl, gPlusUrl, youtubeUrl;
    private static final String TAG = TabFragment2.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tab_fragment2, container, false);
        app = (BaseApplication) getActivity().getApplicationContext();
        initFields();

        from = getArguments().getString("FROM");
        Log.e("wowow"," from what? "+from);
        if (from != null)
            if (from.equals("BEN")) {
                association = (Association) getArguments().getSerializable("ASSOCIATION");
            }

        Log.e("TAAAA"," from what? "+from);




        initFields();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView)   toolbar.getRootView().findViewById(R.id.txtTitle);

        if (from.equals("BEN")) {
            mTitle.setText( association.getName());
            getActivity().setTitle(association.getName());

            _txt_title.setText(association.getName());
            txt_mobile.setText(association.getMobile());
            txt_text.setText(association.getEmail());

            fcbUrl = association.getFacebook();
            instaUrl = association.getInstagram();
            twitterUrl = association.getTwitter();
            gPlusUrl = association.getGoogleplus();
            youtubeUrl = association.getYoutube();

        }  else{

            _linear_for_ass.setVisibility(View.GONE);
            // TODO get app info
            if (app.getInfoApp() == null)
                getInfo();
            else {

                txt_mobile.setText(app.getInfoApp().getMob());
                txt_text.setText(app.getInfoApp().getSiteemail());

                fcbUrl = app.getInfoApp().getFacebook();
                instaUrl = app.getInfoApp().getInstagram();
                twitterUrl = app.getInfoApp().getTwitter();
                gPlusUrl = app.getInfoApp().getGoogleplus();
                youtubeUrl = app.getInfoApp().getYoutube();
            }
        }

        _btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSendMessage();
            }
        });


        _img_fcb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fcbUrl != null && !fcbUrl.isEmpty())
                    openUrl(fcbUrl);
                else Toast.makeText(getActivity(),getString(R.string.msg_not_available), Toast.LENGTH_LONG).show();
            }
        });

        _img_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (instaUrl != null && !instaUrl.isEmpty())
                    openUrl(instaUrl);
                else Toast.makeText(getActivity(),getString(R.string.msg_not_available), Toast.LENGTH_LONG).show();



            }
        });

        _img_gplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gPlusUrl != null && !gPlusUrl.isEmpty())
                    openUrl(gPlusUrl);
                else Toast.makeText(getActivity(),getString(R.string.msg_not_available), Toast.LENGTH_LONG).show();

            }
        });

        _img_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (twitterUrl != null && !twitterUrl.isEmpty())
                    openUrl(twitterUrl);
                else Toast.makeText(getActivity(),getString(R.string.msg_not_available), Toast.LENGTH_LONG).show();

            }
        });

        _img_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (youtubeUrl != null && !youtubeUrl.isEmpty())
                    openUrl(youtubeUrl);
                else Toast.makeText(getActivity(),getString(R.string.msg_not_available), Toast.LENGTH_LONG).show();

            }
        });
        return v;
    }


    private void initFields() {

        _edt_name = v.findViewById(R.id.edt_name);
        _edtphone = v.findViewById(R.id.etphone);
        edt_text = v.findViewById(R.id.edt_text);
        _btn_send = v.findViewById(R.id.btn_send);
        _progressBar=(ProgressBar) v.findViewById(R.id.progressBar);
        _linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);

    }


    private void sendMessage(final String name, final String phone, final String text ) {

        String url = Constants.INSERT_COMPLAINT;
        //+"phone="+phone+"&password="+password;

        Log.e(TAG, "sendMessage url "+url);


        StringRequest LoginFirstRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.v("sendMessage","response == " +response);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean _status = jsonObject.getBoolean("status");
                    String msg = jsonObject.getString("message");
                    if (_status) {
                        AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),msg,true,0);


                    } else {
                        _edt_name.setText("");
                        edt_text.setText("");
                        _edtphone.setText("");

                        _linearLayout.setVisibility(View.VISIBLE);
                        AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),msg,false,0);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                _progressBar.setVisibility(View.GONE);
                _linearLayout.setVisibility(View.VISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _progressBar.setVisibility(View.GONE);
                _linearLayout.setVisibility(View.VISIBLE);

                if (error instanceof AuthFailureError) {

                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.networkerror),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("mobile", phone);
                params.put("details", ""+ text);

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


    private void attemptSendMessage() {

        _edt_name.setError(null);
        _edtphone.setError(null);
        edt_text.setError(null);
        String name = _edt_name.getText().toString();
        String phone = _edtphone.getText().toString();
        String text = edt_text.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            _edt_name.setError(getString(R.string.error_field_required));
            focusView = _edt_name;
            cancel = true;
        }
        if (TextUtils.isEmpty(text)) {
            edt_text.setError(getString(R.string.error_field_required));
            focusView = edt_text;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone)) {
            _edtphone.setError(getString(R.string.error_field_required));
            focusView = _edtphone;
            cancel = true;
        }


        if (!Patterns.PHONE.matcher(phone).matches()){
            _edtphone.setError(getString(R.string.invalidePhoneNumber));
            focusView = _edtphone;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            _linearLayout.setVisibility(View.GONE);
            _progressBar.setVisibility(View.VISIBLE);
            sendMessage(name,phone,text);
        }

    }


    private void getInfo(){

        String url = Constants.GET_CONTACT_INFO;
        Log.e(TAG, "getInfo url "+url);
        _progressBar.setVisibility(View.VISIBLE);
        StringRequest hisRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "getInfo response === "+response.toString());
                _progressBar.setVisibility(View.GONE);
                try {
                    JSONArray resJsonArr = new JSONArray(response);

                    JSONObject resJsonObj =  resJsonArr.getJSONObject(0);
                    String siteemail = resJsonObj.getString("siteemail");
                    String mob = resJsonObj.getString("mob");
                    String facebook = resJsonObj.getString("facebook");
                    String twitter = resJsonObj.getString("twitter");
                    String youtube = resJsonObj.getString("youtube");
                    String instagram = resJsonObj.getString("instagram");
                    String googleplus = resJsonObj.getString("googleplus");

                    InfoApp infoApp = new InfoApp(siteemail,mob, facebook, twitter, youtube, instagram, googleplus);
                    app.setInfoApp(infoApp);
                    txt_mobile.setText(app.getInfoApp().getMob());
                    txt_text.setText(app.getInfoApp().getSiteemail());

                    fcbUrl = app.getInfoApp().getFacebook();
                    instaUrl = app.getInfoApp().getInstagram();
                    twitterUrl = app.getInfoApp().getTwitter();
                    gPlusUrl = app.getInfoApp().getGoogleplus();
                    youtubeUrl = app.getInfoApp().getYoutube();


                } catch (JSONException e) {
                    _progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _progressBar.setVisibility(View.GONE);
                Log.e("/////// VOLLEY  ///// ", error.toString());
                AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.usernameandpassword),false,3);

                if (error instanceof AuthFailureError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);
                }

            }
        }) {
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
        BaseApplication.getInstance().addToRequestQueue(hisRequest);
    }

    private void openUrl(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

}
