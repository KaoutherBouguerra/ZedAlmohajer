package com.art4muslim.zedalmouhajer.features;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.art4muslim.zedalmouhajer.features.register.RegisterActivity;
import com.art4muslim.zedalmouhajer.models.Association;
import com.art4muslim.zedalmouhajer.session.Constants;
import com.art4muslim.zedalmouhajer.utils.AlertDialogManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    Button btnSignIn, btnSignUp;
    ProgressBar _progressBar;
    LinearLayout _linearLayout;
    TextView btForgetPassword;
    @NotEmpty(messageId =  R.string.validation_mobile, order = 1)
    protected EditText inputPhone;
    @NotEmpty(messageId = R.string.nonEmpty, order = 2)
    @MinLength(value = 4, messageId =  R.string.validation_number_length, order = 3)
    protected EditText inputPassword;
    TextView _txt_ben, _txt_ass;

    ImageView _img_logo;
    String accountType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initFields();
        accountType = _txt_ben.getText().toString();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptLogin();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });


        _txt_ben.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountType = _txt_ben.getText().toString();
                _txt_ben.setBackgroundResource(R.drawable.round_left_rect_shape_b);
                _txt_ben.setTextColor(ContextCompat.getColor(LoginActivity.this, android.R.color.white));
                _txt_ass.setBackgroundResource(R.drawable.round_right_rect_shape_w);
                _txt_ass.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorAccent));



            }
        });
        _txt_ass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 accountType = _txt_ass.getText().toString();
                _txt_ass.setBackgroundResource(R.drawable.round_right_rect_shape_b);
                _txt_ass.setTextColor(ContextCompat.getColor(LoginActivity.this, android.R.color.white));
                _txt_ben.setBackgroundResource(R.drawable.round_left_rect_shape_w);
                _txt_ben.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorAccent));


            }
        });

    }

    private void initFields(){

        btnSignIn = findViewById(R.id.btn_signin);
        btnSignUp = findViewById(R.id.btn_signup);
        inputPhone = (EditText) findViewById(R.id.etphone);
        _img_logo = (ImageView) findViewById(R.id.img_logo);
        inputPassword = (EditText) findViewById( R.id.etPassword);

        _progressBar=(ProgressBar) findViewById(R.id.progressBar);
        _linearLayout = (LinearLayout) findViewById(R.id.linearLayout4);

        btForgetPassword = (TextView) findViewById(R.id.btnForgetPassword);
        _txt_ben = (TextView) findViewById(R.id.txt_ben);
        _txt_ass = (TextView) findViewById(R.id.txt_ass);
    }

    private void attemptLogin() {


        // Reset errors.
        inputPhone.setError(null);
        inputPassword.setError(null);


        // Store values at the time of the login attempt.
        String phone = inputPhone.getText().toString();
        String password = inputPassword.getText().toString();


        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            inputPhone.setError(getString(R.string.error_field_required));
            focusView = inputPhone;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError(getString(R.string.error_field_required));
            focusView = inputPassword;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            _linearLayout.setVisibility(View.GONE);

            _progressBar.setVisibility(View.VISIBLE);
            // mAuthTask = new UserLoginTask(email, password);
            // mAuthTask.execute((Void) null);
            LoginFirst(phone, password);
        }
    }

    private void LoginFirst(final String phone, final String password) {
        String url;
        if (accountType.equals(_txt_ben.getText().toString()))
            url = Constants.LOGIN_URL_BEN;
        else  url = Constants.LOGIN_URL_ASS;
        //+"phone="+phone+"&password="+password;

        Log.e(TAG, "LoginFirst url "+url);


        StringRequest LoginFirstRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                Log.v("LoginFirstRequest","response == " +response);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");

                    if (status) {


                    //    BaseApplication.session.saveAccessToken(api_token);
                        String id = jsonObject.getString("id_user");
                        String name = jsonObject.getString("name_user");
                        String phone = jsonObject.getString("mobile_user");

                        Association association = gson.fromJson(String.valueOf(jsonObject), Association.class);

                        BaseApplication.session.createLoginSession(id,name,phone,"","","");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        if (accountType.equals("مستفيد"))
                            intent.putExtra("FROM","BEN");
                        else  {
                            intent.putExtra("FROM","ASSOCIATION");
                            intent.putExtra("ASSOCIATION",association);
                        }


                        startActivity(intent);

                        finish();

                    } else {

                        String msg = jsonObject.getString("message");
                        inputPhone.setText("");
                        inputPassword.setText("");
                        _linearLayout.setVisibility(View.VISIBLE);
                        AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),msg,false,0);
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

                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.authontiation),false,3);

                } else if (error instanceof ServerError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.servererror),false,3);
                } else if (error instanceof NetworkError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.networkerror),getResources().getString(R.string.networkerror),false,3);

                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    AlertDialogManager.showAlertDialog(LoginActivity.this,getResources().getString(R.string.app_name),getResources().getString(R.string.timeouterror),false,3);
                }
            }
        }) {
            /*     @Override
                public String getBodyContentType() {
                    return "application/json";
                }

               @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
                */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", phone);
                params.put("password", ""+ password);

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
