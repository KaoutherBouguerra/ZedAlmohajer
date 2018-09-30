package com.art4muslim.zedalmouhajer.features.register;


import android.annotation.SuppressLint;
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
import com.art4muslim.zedalmouhajer.adapters.SpinnerCityAdapter;
import com.art4muslim.zedalmouhajer.R;
import com.art4muslim.zedalmouhajer.features.LoginAsAssociationActivity;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AssociationFragment extends Fragment {

    private static final String TAG = AssociationFragment.class.getSimpleName();
    View v;
    Button btnSignUp;
    EditText edtNameBenfic, edtPhone,edtAbout, edtEmail, edtPwd, edtRepassword ;
    Spinner spinnerCity;

    CheckBox checkbox;
    ProgressBar _progressBar;
    LinearLayout _linearLayout;
    int id_city;

    SpinnerCityAdapter adapter;
    ArrayList<City> cities = new ArrayList<City>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_association, container, false);

        initFields();
        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                    attemptRegister();

            }
        });


        adapter = new SpinnerCityAdapter(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1,
                cities);


        spinnerCity.setAdapter(adapter);

        getCity();
        // You can create an anonymous listener to handle the event when is selected an spinner item
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                City category = adapter.getItem(position);
                // Here you can do the action you want to...
                id_city = category.getId();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });


        String[] socialStatus = getResources().getStringArray(R.array.social_status);

        final ArrayAdapter<String> adapterStatus=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,android.R.id.text1, socialStatus);

             return v;
    }

    private void initFields(){

        btnSignUp = v.findViewById(R.id.btn_signup);
        edtNameBenfic= v.findViewById(R.id.edt_name_benfis);
        edtPhone= v.findViewById(R.id.edt_phone);
        edtAbout= v.findViewById(R.id.edt_about);
        edtEmail= v.findViewById(R.id.edt_email);
        edtPwd= v.findViewById(R.id.edt_password);

        edtRepassword= v.findViewById(R.id.edt_repassword);
        _progressBar=(ProgressBar) v.findViewById(R.id.progressBar);
        _linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout4);
        spinnerCity = v.findViewById(R.id.spinner);


        checkbox = v.findViewById(R.id.checkbox);
    }

    public void showDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.popup_success);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final Button dialogButtonoui = (Button) dialog.findViewById(R.id.btn_accept);


        dialogButtonoui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


                Intent intent = new Intent(getActivity(), LoginAsAssociationActivity.class);
                intent.putExtra("FROM","ASSOCIATION");
                startActivity(intent);


                // applyForOrder(Constants.CONFIRM_ORDER_URL,getString(R.string.deliver_success));
            }
        });

        dialog.show();

    }

    private void getCity() {

        String url = Constants.GET_ALL_CITIES;

        Log.e(TAG, "getCity url "+url);
        _progressBar.setVisibility(VISIBLE);
        StringRequest hisRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "getCity response === "+response.toString());
                _progressBar.setVisibility(GONE);
                try {
                    JSONArray resJsonObj = new JSONArray(response);


                    for (int i = 0; i<resJsonObj.length();i++){

                        JSONObject adrJsonObj = resJsonObj.getJSONObject(i);
                        int id = adrJsonObj.getInt("id");
                        String name = adrJsonObj.getString("name");


                        cities.add(new City(id,name));
                    }

                    adapter.notifyDataSetChanged();



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _progressBar.setVisibility(GONE);
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
        };

        // Adding request to request queue
        BaseApplication.getInstance().addToRequestQueue(hisRequest);
    }

    private void attemptRegister() {
        // Reset errors.
        edtNameBenfic.setError(null);
        edtPhone.setError(null);
        edtEmail.setError(null);
        edtPwd.setError(null);
        edtRepassword.setError(null);
        edtAbout.setError(null);



        // Store values at the time of the login attempt.
        String phone = edtPhone.getText().toString();
        String password = edtPwd.getText().toString();
        String about = edtAbout.getText().toString();
        String name = edtNameBenfic.getText().toString();
        String repassword = edtRepassword.getText().toString();
        String email = edtEmail.getText().toString();



        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(name)) {
            edtNameBenfic.setError(getString(R.string.error_field_required));
            focusView = edtNameBenfic;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError(getString(R.string.error_field_required));
            focusView = edtEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(repassword)) {
            edtRepassword.setError(getString(R.string.error_field_required));
            focusView = edtRepassword;
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
        if (TextUtils.isEmpty(about)) {
            edtAbout.setError(getString(R.string.error_field_required));
            focusView = edtAbout;
            cancel = true;
        }

        if (!password.equals(repassword)){
            edtPwd.setError(getString(R.string.error_password_match));
            focusView = edtPwd;
            cancel = true;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError(getString(R.string.invalideEmail));
            focusView = edtEmail;
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

            if (checkbox.isChecked()){
                _linearLayout.setVisibility(View.GONE);

                _progressBar.setVisibility(View.VISIBLE);
                register(name,phone,email, id_city, password,about);
            }

            else
                Toast.makeText(getActivity(),"الرجاء الضغط على زر قرأت الشروط و الأحكام",Toast.LENGTH_LONG).show();


        }
    }

    private void register(final String name, final String phone, final String email, final int cityId, final String password, final String about) {

        String url = Constants.REGISTER_ASSOCIATION;
        //+"phone="+phone+"&password="+password;

        Log.e(TAG, "register url "+url);


        StringRequest LoginFirstRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.v("registerRequest","response == " +response);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean _status = jsonObject.getBoolean("status");

                    if (_status) {

                      //  BaseApplication.session.createLoginSession(Integer.parseInt(idNumber),name,phone,"","",status);

                        showDialog();

                    } else {

                        String msg = jsonObject.getString("msg");
                        edtEmail.setText("");
                        edtPhone.setText("");

                        edtPwd.setText("");
                        edtRepassword.setText("");
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
                params.put("email", ""+ email);
                params.put("city_id", ""+ id_city);
                params.put("password", ""+ password);
                params.put("about", ""+ about);


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

    private void showFragment(Fragment fragment) {

        Bundle args = new Bundle();


        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, "home Fragment");
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }

}
