package com.art4muslim.zedalmouhajer.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.art4muslim.zedalmouhajer.session.Constants;
import com.art4muslim.zedalmouhajer.utils.AlertDialogManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.art4muslim.zedalmouhajer.session.SessionManager.Key_UserID;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllInfosAssFragment extends Fragment {
    private static String TAG = AllInfosAssFragment.class.getSimpleName();
    BaseApplication app;
    ProgressBar _progressBar;
    TextView _txt_name,_txt_phone, _txt_email, _txt_about;
    ImageView _img_gplus, _img_youtube, _img_insta, _img_twitter , _img_fcb, img_profile_image;
    String fcbUrl, instaUrl, twitterUrl, gPlusUrl, youtubeUrl;
    RelativeLayout relativeLayout;
    View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_all_infos_ass, container, false);
        app = (BaseApplication) getActivity().getApplicationContext();
        initFields();
        getAllInfoAssociation();


        _img_fcb.setOnClickListener(view -> {
            if (fcbUrl != null && !fcbUrl.isEmpty())
                openUrl(fcbUrl);
            else Toast.makeText(getActivity(),getString(R.string.msg_not_available), Toast.LENGTH_LONG).show();
        });

        _img_insta.setOnClickListener(view -> {
            if (instaUrl != null && !instaUrl.isEmpty())
                openUrl(instaUrl);
            else Toast.makeText(getActivity(),getString(R.string.msg_not_available), Toast.LENGTH_LONG).show();



        });

        _img_gplus.setOnClickListener(view -> {
            if (gPlusUrl != null && !gPlusUrl.isEmpty())
                openUrl(gPlusUrl);
            else Toast.makeText(getActivity(),getString(R.string.msg_not_available), Toast.LENGTH_LONG).show();

        });

        _img_twitter.setOnClickListener(view -> {
            if (twitterUrl != null && !twitterUrl.isEmpty())
                openUrl(twitterUrl);
            else Toast.makeText(getActivity(),getString(R.string.msg_not_available), Toast.LENGTH_LONG).show();

        });

        _img_youtube.setOnClickListener(view -> {

            if (youtubeUrl != null && !youtubeUrl.isEmpty())
                openUrl(youtubeUrl);
            else Toast.makeText(getActivity(),getString(R.string.msg_not_available), Toast.LENGTH_LONG).show();

        });
        return v;
    }

    private void initFields() {

        _progressBar = v.findViewById(R.id.progressBar);
        _img_fcb = v.findViewById(R.id.img_fcb);
        _img_gplus = v.findViewById(R.id.img_gplus);
        _img_youtube = v.findViewById(R.id.img_youtube);
        _img_insta = v.findViewById(R.id.img_insta);
        _img_twitter = v.findViewById(R.id.img_twitter);
        img_profile_image = v.findViewById(R.id.img_profile_image);
        _txt_email = v.findViewById(R.id.txt_email);
        _txt_about = v.findViewById(R.id.txt_about);
        _txt_name = v.findViewById(R.id.txt_name);
        _txt_phone = v.findViewById(R.id.txt_phone);
        relativeLayout = v.findViewById(R.id.relativeLayout);

    }

    private void getAllInfoAssociation() {
        String url = Constants.GET_INFO_ASS+app.getAssociation().getId();

         Log.e(TAG, "getAllInfoAssociation url "+url);
        _progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
         StringRequest hisRequest = new StringRequest(Request.Method.GET, url, response -> {
         Gson gson = new Gson();
         Log.e(TAG, "getAllInfoAssociation response === "+response.toString());
         _progressBar.setVisibility(View.GONE);
         relativeLayout.setVisibility(View.VISIBLE);

         JSONObject resJsonObj= null;

            try {
                resJsonObj = new JSONObject(response);

                Association association = gson.fromJson(String.valueOf(resJsonObj), Association.class);
                Log.e(TAG, "getAllAssociation ass name === "+association.getName());

                _txt_phone.setText(association.getMobile());
                _txt_about.setText(Html.fromHtml(association.getAbout()));
                _txt_name.setText(association.getName());
                _txt_email.setText(association.getEmail());
                youtubeUrl = association.getYoutube();
                fcbUrl = association.getFacebook();
                twitterUrl = association.getTwitter();
                instaUrl = association.getInstagram();
                gPlusUrl = association.getGoogleplus();

                Picasso.with(getActivity())
                        .load(association.getImage())
                        .fit()
                        .into(img_profile_image);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            _progressBar.setVisibility(View.GONE);
             relativeLayout.setVisibility(View.VISIBLE);
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
