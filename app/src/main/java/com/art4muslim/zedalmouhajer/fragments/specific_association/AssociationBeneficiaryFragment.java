package com.art4muslim.zedalmouhajer.fragments.specific_association;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;
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
import com.art4muslim.zedalmouhajer.adapters.CustomListAdapter;
import com.art4muslim.zedalmouhajer.fragments.CommunicateWithAssociationFragment;
import com.art4muslim.zedalmouhajer.fragments.contactus.TabContactUsFragment;
import com.art4muslim.zedalmouhajer.fragments.contactus.TabFragment1;
import com.art4muslim.zedalmouhajer.fragments.contactus.TabFragment2;
import com.art4muslim.zedalmouhajer.fragments.join_application.ActiveRequestsFragment;
import com.art4muslim.zedalmouhajer.menu.Beneficiar;
import com.art4muslim.zedalmouhajer.models.Association;
import com.art4muslim.zedalmouhajer.session.Constants;
import com.art4muslim.zedalmouhajer.session.SessionManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.art4muslim.zedalmouhajer.session.SessionManager.Key_UserID;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssociationBeneficiaryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,ViewTreeObserver.OnScrollChangedListener{


    private static final String TAG = AssociationBeneficiaryFragment.class.getSimpleName();

    private  boolean isActive = true;
    private static String url = "";
    private ProgressDialog pDialog;
    private List<Beneficiar> beneficairesList = new ArrayList<Beneficiar>();

    private ListView listView;
    private CustomListAdapter adapter;
    SessionManager sessionman;
    private SwipeRefreshLayout swipeRefreshLayout;

    BaseApplication app;
    View v;
    Association association;
    FragmentTransaction fragmentTransaction;
    TextView txtNoData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_association_beneficiary, container, false);


        app = (BaseApplication) getActivity().getApplicationContext();

        if (getArguments() != null)
            association = (Association) getArguments().getSerializable("ASSOCIATION");
        else association = app.getAssociation();


        isActive = true;

        txtNoData = (TextView) v.findViewById(R.id.txt_no_data);
        listView = (ListView) v.findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        fragmentTransaction =  getActivity().getSupportFragmentManager().beginTransaction();
        adapter = new CustomListAdapter(getActivity(), beneficairesList,"BEN",fragmentTransaction);

        listView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);

                fetchAllBene();
            }
        });

        return v;
    }

    @Override
    public void onRefresh() {
        Log.e(TAG,"onRefresh");
        beneficairesList.clear();
        adapter.clear();
        fetchAllBene();
    }
    @Override
    public void onScrollChanged() {
        if (listView.getFirstVisiblePosition() == 0) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }
    }
    @Override
    public void onStart() {
        Log.e(TAG,"onStart");
        super.onStart();
        beneficairesList.clear();
        adapter.clear();
        listView.getViewTreeObserver().addOnScrollChangedListener(this);

        // fetchnewOrders();
    }

    @Override
    public void onStop() {
        Log.e(TAG,"onStop");
        super.onStop();
        isActive = false;
        listView.getViewTreeObserver().removeOnScrollChangedListener(this);

    }
    private void fetchAllBene() {

        swipeRefreshLayout.setRefreshing(true);
        String url;
        if (association != null)
           url = Constants.GET_ALL_BENEF+association.getId();
        else url = Constants.GET_ALL_BENEF+BaseApplication.session.getUserDetails().get(Key_UserID);
        //+KEY_API_TOKEN+"="+ BaseApplication.session.getAccessToken();

        Log.e(TAG, "fetchAllBene url "+url);

        StringRequest hisRequest = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Log.e(TAG, "fetchAllBene response === "+response.toString());

                try {
                    JSONArray resJsonObj = new JSONArray(response);
                    if (resJsonObj.length() == 0){
                        txtNoData.setVisibility(View.VISIBLE);
                    }else {

                        for (int i = 0; i < resJsonObj.length(); i++) {

                            JSONObject adrJsonObj = resJsonObj.getJSONObject(i);
                            Beneficiar beneficiar = gson.fromJson(String.valueOf(adrJsonObj), Beneficiar.class);


                            beneficairesList.add(beneficiar);

                        }
                        adapter.notifyDataSetChanged();
                    }

                        swipeRefreshLayout.setRefreshing(false);


                } catch (JSONException e) {
                    swipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
                Log.e("/////// VOLLEY  ///// ", error.toString());
                // AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.usernameandpassword),false,3);

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onResume() {
        super.onResume();
        isActive =true;

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
    }
}



