package com.art4muslim.zedalmouhajer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.art4muslim.zedalmouhajer.models.Association;
import com.art4muslim.zedalmouhajer.session.Constants;
import com.art4muslim.zedalmouhajer.utils.AlertDialogManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutAssociationFragment extends Fragment {


    View v;
    TextView _txt_title, txt_description;
    BaseApplication app;
    Association association;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_about_association, container, false);
        app = (BaseApplication) getActivity().getApplicationContext();
        association = (Association) getArguments().getSerializable("ASSOCIATION");
        initFields();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView)   toolbar.getRootView().findViewById(R.id.txtTitle);
        mTitle.setText( R.string.about_ass);
       // getActivity().setTitle(getString(R.string.about_ass));

        Log.e("About ass ", "Association name = "+association.getName());
        Log.e("About ass ", "Association description = "+association.getAbout());
        _txt_title.setText(Html.fromHtml(association.getName()));
        txt_description.setText(Html.fromHtml(association.getAbout()));
        return v;
    }

    private void initFields(){
        _txt_title = v.findViewById(R.id.txt_title);
        txt_description = v.findViewById(R.id.txt_description);

    }


}
