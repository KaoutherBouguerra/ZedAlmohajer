package com.art4muslim.zedalmouhajer.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

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
import com.art4muslim.zedalmouhajer.adapters.SpinnerCityAdapter;
import com.art4muslim.zedalmouhajer.features.MainActivity;
import com.art4muslim.zedalmouhajer.models.Association;
import com.art4muslim.zedalmouhajer.models.City;
import com.art4muslim.zedalmouhajer.session.Constants;
import com.art4muslim.zedalmouhajer.utils.AlertDialogManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.art4muslim.zedalmouhajer.session.SessionManager.Key_UserID;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterToAssociationFragment extends Fragment {
    private static final String TAG = RegisterToAssociationFragment.class.getSimpleName();

   View v;
   Button btnSend;
    EditText edtNameBenfic, edtPhone, edtPwd, edtIdNumber;

    Spinner spinnerStatus;

    ProgressBar _progressBar;
    LinearLayout _linearLayout;

    String status;
    Association association;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_register_to_association, container, false);
        association = (Association) getArguments().getSerializable("ASSOCIATION");
        Log.e(TAG, "Association ID = "+association.getId());
        initFields();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        String[] socialStatus = getResources().getStringArray(R.array.social_status);

        final ArrayAdapter<String> adapterStatus=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,android.R.id.text1, socialStatus);

        spinnerStatus.setAdapter(adapterStatus);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                status = adapterStatus.getItem(position);
                // Here you can do the action you want to...


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        return v;
    }

    private void initFields(){
        btnSend = v.findViewById(R.id.btn_send);
        edtNameBenfic= v.findViewById(R.id.edt_name_benfis);
        edtPhone= v.findViewById(R.id.edt_phone);

        edtPwd= v.findViewById(R.id.edt_password);
        edtIdNumber= v.findViewById(R.id.edt_id_number);

        _progressBar=(ProgressBar) v.findViewById(R.id.progressBar);
        _linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout4);
         spinnerStatus = v.findViewById(R.id.spinnerStatus);

     }



    private void attemptRegister() {
        // Reset errors.
  /*      edtNameBenfic.setError(null);
        edtPhone.setError(null);

        edtPwd.setError(null);

        edtIdNumber.setError(null);
*/

        // Store values at the time of the login attempt.
        String phone = edtPhone.getText().toString();
        String password = edtPwd.getText().toString();
        String name = edtNameBenfic.getText().toString();


        String idNumber = edtIdNumber.getText().toString();


   /*     boolean cancel = false;
        View focusView = null;




        if (TextUtils.isEmpty(name)) {
            edtNameBenfic.setError(getString(R.string.error_field_required));
            focusView = edtNameBenfic;
            cancel = true;
        }

        if (TextUtils.isEmpty(idNumber)) {
            edtIdNumber.setError(getString(R.string.error_field_required));
            focusView = edtIdNumber;
            cancel = true;
        }
        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError(getString(R.string.error_field_required));
            focusView = edtPhone;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            edtPwd.setError(getString(R.string.error_field_required));
            focusView = edtPwd;
            cancel = true;
        }


        if (!Patterns.PHONE.matcher(phone).matches()){
            edtPhone.setError(getString(R.string.invalidePhoneNumber));
            focusView = edtPhone;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {

        */
            _linearLayout.setVisibility(View.GONE);

            _progressBar.setVisibility(View.VISIBLE);

            register(BaseApplication.session.getUserDetails().get(Key_UserID), association.getId());
       // }
    }

    private void register(final String idBen, final String idAss ) {

        String url = Constants.REGISTER_BENEFIC_TO_ASS;
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

                        fragmentTransaction.commit();


                    } else {

                        String msg = jsonObject.getString("msg");

                        edtPhone.setText("");
                        edtIdNumber.setText("");
                        edtPwd.setText("");

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
