package com.art4muslim.zedalmouhajer.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.art4muslim.zedalmouhajer.fragments.contactus.TabFragment2;
import com.art4muslim.zedalmouhajer.session.Constants;
import com.art4muslim.zedalmouhajer.utils.AlertDialogManager;
import com.art4muslim.zedalmouhajer.utils.SavePhotoParams;
import com.art4muslim.zedalmouhajer.utils.SavePhotoTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddSubjectFragment extends Fragment {


    private static int RESULT_LOAD_IMG = 100;
    View v;
    EditText _edt_name, _edt_video_link, edt_text;
    ProgressBar _progressBar;
    LinearLayout _linearLayout;
    Button _btn_send, _btnAddPhoto;
    private static final String TAG = AddSubjectFragment.class.getSimpleName();
    BaseApplication app;
    String idAss;
    Bitmap selectedImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_add_subject, container, false);
        app = (BaseApplication) getActivity().getApplicationContext();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView)   toolbar.getRootView().findViewById(R.id.txtTitle);
        mTitle.setText( R.string.txt_add_subject);
        initFields();

        idAss = app.getAssociation().getId_user();

        _btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSendMessage();
            }
        });

        _btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        return v;
    }


    private void attemptSendMessage() {

        _edt_name.setError(null);
        _edt_video_link.setError(null);
        edt_text.setError(null);
        String name = _edt_name.getText().toString();
        String link = _edt_video_link.getText().toString();
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




        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            _linearLayout.setVisibility(View.GONE);
            _progressBar.setVisibility(View.VISIBLE);
            addSubject(name,link,text,idAss);
        }

    }


    private void initFields() {

        _edt_name = v.findViewById(R.id.edt_name);
        _edt_video_link = v.findViewById(R.id.edt_video_link);
        edt_text = v.findViewById(R.id.edt_text);
        _btn_send = v.findViewById(R.id.btn_send);
        _btnAddPhoto = v.findViewById(R.id.btnAddPhoto);
        _progressBar=(ProgressBar) v.findViewById(R.id.progressBar);
        _linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);

    }


    private void addSubject(final String name, final String youtubelink, final String details, final String ass_id ) {


        SavePhotoParams paramsImage = new SavePhotoParams(getActivity(), name, youtubelink, details, ass_id, selectedImage, _linearLayout, _progressBar);
        SavePhotoTask savePhotoTask = new SavePhotoTask();
        savePhotoTask.execute(paramsImage);



    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getActivity(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }


}
