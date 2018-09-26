package com.art4muslim.zedalmouhajer.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.art4muslim.zedalmouhajer.features.LoginActivity;
import com.art4muslim.zedalmouhajer.fragments.specific_association.NewsBeneficAssociationFragment;
import com.art4muslim.zedalmouhajer.models.Association;
import com.art4muslim.zedalmouhajer.session.Constants;
import com.art4muslim.zedalmouhajer.utils.AlertDialogManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.art4muslim.zedalmouhajer.session.Constants.CONSTANT_BEN;
import static com.art4muslim.zedalmouhajer.session.SessionManager.KEY_NAME;
import static com.art4muslim.zedalmouhajer.session.SessionManager.Key_UserID;

/**
 * A simple {@link Fragment} subclass.
 **/
public class SelectedAssociationFragment extends Fragment {

    private static final String TAG = SelectedAssociationFragment.class.getSimpleName();
    View v;

    LinearLayout linearAbout, linearBen, linearContact, linearRegister;
    ImageView imgAss;
    TextView txtNameAss;
    TextView txt_register;
    ProgressBar _progressBar;
    LinearLayout _relative;
    boolean isAdded = false;
    Association association;
    BaseApplication app;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_selected_association, container, false);
        app = (BaseApplication) getActivity().getApplicationContext();
        association = (Association) getArguments().getSerializable("ASSOCIATION");
        app.setAssociation(association);
        isAdded = getArguments().getBoolean("IS_ADDED");

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView)   toolbar.getRootView().findViewById(R.id.txtTitle);
        mTitle.setText(association.getName());
        ImageView img_back = (ImageView)   toolbar.getRootView().findViewById(R.id.img_back);
        img_back.setOnClickListener(v -> {
            Log.e("TAG"," img back clicked");

            getActivity().onBackPressed();
        });

        init();
        Picasso.with(getActivity()).load(association.getImage())
                .fit()
                .into(imgAss);

        txtNameAss.setText(association.getName());

        linearAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutAssociationFragment aboutAssociationFragment = new AboutAssociationFragment();
                showFragment(aboutAssociationFragment);
            }
        });

        linearBen.setOnClickListener(view -> {
            // todo check if user connected
            Log.e("selected ass"," is logged = "+BaseApplication.session.isLoggedIn());
            if (BaseApplication.session.isLoggedIn()) {
                NewsBeneficAssociationFragment schedule = new NewsBeneficAssociationFragment();
                showFragment(schedule);
            }else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        linearContact.setOnClickListener(view -> {
            CommunicateWithAssociationFragment associationBeneficiaryFragment = new CommunicateWithAssociationFragment();
            showFragment(associationBeneficiaryFragment);


        });

        linearRegister.setOnClickListener(view -> {
          //  RegisterToAssociationFragment associationBeneficiaryFragment = new RegisterToAssociationFragment();
          // showFragment(associationBeneficiaryFragment);
            if (BaseApplication.session.isLoggedIn() && BaseApplication.session.getIsFrom().equals(CONSTANT_BEN)) {

                attemptRegister();
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        if (isAdded)
            txt_register.setText("إلغاء الإشتراك");
        else
            txt_register.setText(getString(R.string.txt_association_collab));
        return v;
    }

    private void init() {
        linearAbout = v.findViewById(R.id.linearAbout);
        linearBen = v.findViewById(R.id.linearBen);
        linearContact = v.findViewById(R.id.linearContact);
        linearRegister = v.findViewById(R.id.linearRegister);
        imgAss = v.findViewById(R.id.ass_profile_photo);
        txtNameAss = v.findViewById(R.id.txtNameAss);
        txt_register = v.findViewById(R.id.txt_register);
        _progressBar = v.findViewById(R.id.progressBar);
        _relative =  v.findViewById(R.id.relative);
    }

    private void showFragment(Fragment fragment) {

        Bundle args = new Bundle();
        args.putSerializable("ASSOCIATION", association);
        args.putString("FROM","BEN");
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, "home Fragment");
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }



    private void attemptRegister() {
        _progressBar.setVisibility(View.VISIBLE);
        _relative.setVisibility(View.GONE);

        register(BaseApplication.session.getUserDetails().get(Key_UserID), association.getId());

    }

    private void register(final String idBen, final String idAss ) {

        String url = Constants.REGISTER_BENEFIC_TO_ASS;
        if (isAdded)
            url = Constants.DELETE_BENEFIC_TO_ASS;
        //+"phone="+phone+"&password="+password;

        Log.e(TAG, "register url "+url);
        Log.e(TAG, "idBen  "+idBen);
        Log.e(TAG, "idAss "+idAss);


        StringRequest LoginFirstRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.v("registerRequest","response == " +response);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean _status = jsonObject.getBoolean("status");

                    if (_status) {

                        SelectedAssociationFragment selectedAssociationFragment = new SelectedAssociationFragment();
                        Bundle args = new Bundle();
                        args.putSerializable("ASSOCIATION", association);
                        selectedAssociationFragment.setArguments(args);
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, selectedAssociationFragment, "home Fragment");
                        fragmentTransaction.addToBackStack("");
                        fragmentTransaction.commit();

                        if (isAdded) {
                            app.getAddedAssociations().remove(association);
                        }else{
                            app.getAddedAssociations().add(association);
                        }

                    } else {

                        String msg = jsonObject.getString("msg");

                        AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),msg,false,0);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                _progressBar.setVisibility(View.GONE);
                _relative.setVisibility(View.VISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _progressBar.setVisibility(View.GONE);
                _relative.setVisibility(View.VISIBLE);

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
                params.put("bene_id", idBen);
                params.put("ass_id", idAss);

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
