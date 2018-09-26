package com.art4muslim.zedalmouhajer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.art4muslim.zedalmouhajer.adapters.CustomGrid;
import com.art4muslim.zedalmouhajer.models.Association;
import com.art4muslim.zedalmouhajer.session.Constants;
import com.art4muslim.zedalmouhajer.utils.AlertDialogManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.art4muslim.zedalmouhajer.session.SessionManager.Key_UserID;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssociationsGridFragment extends Fragment {


    View v;
  //  ArrayList<Category> cats = new ArrayList<Category>();
    private static final String TAG = AssociationsGridFragment.class.getSimpleName();
    CustomGrid adapter;
    ProgressBar _progressBar;
    boolean isRightToLeft  ;
    GridView grid;
    BaseApplication app;
    ArrayList<Association> associations = new ArrayList<Association>();

    TextView txtNoData;
    boolean isAdded = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_associations, container, false);
        txtNoData = (TextView) v.findViewById(R.id.txt_no_data);
        app = (BaseApplication) getActivity().getApplicationContext();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView)   toolbar.getRootView().findViewById(R.id.txtTitle);
        ImageView img_back = (ImageView)   toolbar.getRootView().findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG," img back clicked");
            }
        });


        isAdded = getArguments().getBoolean("ISADDED");

        if (isAdded){
            mTitle.setText( R.string.txt_added_association);
          //  getActivity().setTitle(getString(R.string.txt_added_association));
        }else{
            mTitle.setText( R.string.txt_all_association);
          //  getActivity().setTitle(getString(R.string.txt_all_association));
        }



        initFields();


        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        if (!isAdded) {
            if (app.getAssociations().size() == 0)
                adapter = new CustomGrid(getActivity(), associations, fragmentTransaction);
            else {
                associations = app.getAssociations();
                adapter = new CustomGrid(getActivity(), app.getAssociations(), fragmentTransaction);
            }
        } else {
            if (app.getAddedAssociations().size() == 0)
                adapter = new CustomGrid(getActivity(), associations, fragmentTransaction);
            else {
                associations = app.getAddedAssociations();
                adapter = new CustomGrid(getActivity(), app.getAddedAssociations(), fragmentTransaction);
            }
        }

        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              //  FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

                Fragment fragment;
                Association association = associations.get(i);
                Log.e(TAG, "Association ID = "+association.getId());
             //   if (!isAdded) {


                  //  fragment = new RegisterToAssociationFragment();


              //  } else {

                    fragment = new SelectedAssociationFragment();
              //  }
                Bundle args = new Bundle();
                args.putSerializable("ASSOCIATION", association);
                args.putBoolean("IS_ADDED", isAdded);
                fragment.setArguments(args);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment, "home Fragment");
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });


        if (!isAdded) {
            if (app.getAssociations().size() == 0)
                getAllAssociation();

        }else {
            if (app.getAddedAssociations().size() == 0)
                getAllAssociation();

        }
        return v;
    }

    private void getAllAssociation(){
        String url = Constants.GET_ALL_ASS;
        if (isAdded)
            url = Constants.GET_ADDED_ASSOS+BaseApplication.session.getUserDetails().get(Key_UserID);
        Log.e(TAG, "getAllAssociation url "+url);
        _progressBar.setVisibility(View.VISIBLE);
        StringRequest hisRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Log.e(TAG, "getAllAssociation response === "+response.toString());
                _progressBar.setVisibility(View.GONE);

                JSONArray resJsonObj= null;
                try {
                     resJsonObj = new JSONArray(response);

                    for (int i = 0; i<resJsonObj.length();i++) {

                        JSONObject adrJsonObj = resJsonObj.getJSONObject(i);
                        Association association = gson.fromJson(String.valueOf(adrJsonObj), Association.class);
                        Log.e(TAG, "getAllAssociation ass name === "+association.getName());
                        associations.add(association);
                        if (!isAdded)
                        app.setAssociations(associations);
                        else app.setAddedAssociations(associations);
                    }


                    if (isAdded && resJsonObj.length() == 0) {

                            txtNoData.setVisibility(View.VISIBLE);

                    }

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
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

    private void initFields(){
        grid=(GridView)v.findViewById(R.id.grid);
        _progressBar=(ProgressBar) v.findViewById(R.id.progressBar);
    }
}
