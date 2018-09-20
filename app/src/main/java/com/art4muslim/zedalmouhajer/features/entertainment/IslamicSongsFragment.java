package com.art4muslim.zedalmouhajer.features.entertainment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.art4muslim.zedalmouhajer.adapters.FeedSoundsAdapter;
import com.art4muslim.zedalmouhajer.models.Sound;
import com.art4muslim.zedalmouhajer.session.Constants;
import com.art4muslim.zedalmouhajer.utils.Utils;
import com.art4muslim.zedalmouhajer.view.FeedContextMenuManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class IslamicSongsFragment extends Fragment {

    private static final String TAG = IslamicSongsFragment.class.getSimpleName();
    View v;
    RecyclerView rvFeed;
    BaseApplication app;
    private FeedSoundsAdapter feedAdapter;
    TextView txt_msg;
    ImageView img_internet;
    private ArrayList<Sound> sounds = new ArrayList<>();

    ProgressBar _progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_islamic_songs, container, false);
        initFields();
        setupFeed();
        return v;
    }

    private void initFields() {
        rvFeed =  v.findViewById(R.id.rvFeed);
        txt_msg =  v.findViewById(R.id.txt_msg);
        img_internet =  v.findViewById(R.id.img_internet);
        _progressBar=(ProgressBar) v.findViewById(R.id.progressBar);
    }

    private void setupFeed() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        rvFeed.setLayoutManager(linearLayoutManager);

        feedAdapter = new FeedSoundsAdapter(getActivity(), sounds);
        //   feedAdapter.setOnFeedItemClickListener(getActivity());
        rvFeed.setAdapter(feedAdapter);
        rvFeed.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                FeedContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
            }
        });
        //   rvFeed.setItemAnimator(new FeedItemAnimator());
        if (Utils.isOnline(getActivity()))
            startContentAnimation();
        else {

            txt_msg.setVisibility(View.VISIBLE);
            img_internet.setVisibility(View.VISIBLE);
            txt_msg.setText("لا يوجد اتصال بالإنترنت");
        }


    }


    private void startContentAnimation() {
        getSounds();
    }

    private void getSounds() {
        _progressBar.setVisibility(View.VISIBLE);
        String url = Constants.GET_ALL_ISLAMIC_SONGS;
        //+KEY_API_TOKEN+"="+ BaseApplication.session.getAccessToken();

        Log.e(TAG, "get islamic songs url "+url);

        StringRequest hisRequest = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                Log.e(TAG, "get islamic songs response === "+response.toString());

                try {
                    JSONArray resJsonObj = new JSONArray(response);

                    for (int i = 0; i<resJsonObj.length();i++) {

                        JSONObject adrJsonObj = resJsonObj.getJSONObject(i);
                        Sound sound = gson.fromJson(String.valueOf(adrJsonObj), Sound.class);
                        sounds.add(sound);
                    }


                    feedAdapter.updateItems(true,sounds);
                    txt_msg.setVisibility(View.GONE);
                    img_internet.setVisibility(View.GONE);

                    //    adapter.notifyDataSetChanged();
                    _progressBar.setVisibility(View.GONE);


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
                // AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.usernameandpassword),false,3);
                txt_msg.setVisibility(View.VISIBLE);
                img_internet.setVisibility(View.GONE);
                if (error instanceof AuthFailureError) {
                    // AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    //  AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    //   AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    //  AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);
                }

            }
        }) {



            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


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
        BaseApplication.getInstance().addToRequestQueue(hisRequest);
    }

}
