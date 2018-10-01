package com.art4muslim.zedalmouhajer.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.art4muslim.zedalmouhajer.utils.MultipartUtility;
import com.art4muslim.zedalmouhajer.utils.SavePhotoParams;
import com.art4muslim.zedalmouhajer.utils.SavePhotoTask;
import com.art4muslim.zedalmouhajer.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.art4muslim.zedalmouhajer.session.SessionManager.Key_UserID;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddSubjectFragment extends Fragment {
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100;
    Uri imageUri;
    private static int RESULT_LOAD_IMG = 100;
    View v;
    EditText _edt_name, _edt_video_link, edt_text, _edt_photo_name;
    ProgressBar _progressBar;
    LinearLayout _linearLayout;
    Button _btn_send, _btnAddPhoto;
    private static final String TAG = AddSubjectFragment.class.getSimpleName();
    BaseApplication app;
    String idAss;
    Bitmap selectedImage;
    File selectedImageFile;
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
        String url;
        if (app.getAssociation() != null)
            idAss = app.getAssociation().getId();
      //  else idAss = BaseApplication.session.getUserDetails().get(Key_UserID);


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
        _edt_photo_name = v.findViewById(R.id.edt_photo_name);
        _btn_send = v.findViewById(R.id.btn_send);
        _btnAddPhoto = v.findViewById(R.id.btnAddPhoto);
        _progressBar=(ProgressBar) v.findViewById(R.id.progressBar);
        _linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);

    }


    private void addSubject(final String name, final String youtubelink, final String details, final String ass_id ) {
        Thread thread = new Thread(() -> {
            try  {
                uploadMedia(name, youtubelink, details, ass_id, selectedImageFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();


      //  SavePhotoParams paramsImage = new SavePhotoParams(getActivity(), name, youtubelink, details, ass_id, selectedImage, _linearLayout, _progressBar);
       // SavePhotoTask savePhotoTask = new SavePhotoTask();
       // savePhotoTask.execute(paramsImage);



    }
    private void uploadMedia(final String name, final String youtubelink, final String details, final String ass_id, File selectedImageFile) {
        try {

            String charset = "UTF-8";

          //  String requestURL = Data.BASE_URL+Data.URL_UPLOAD_REACTION_TEST;
            String requestURL = Constants.INSERT_NEW_SUBJECT;
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);

//            multipart.addHeaderField("User-Agent", "CodeJava");
//            multipart.addHeaderField("Test-Header", "Header-Value");

            multipart.addFormField("name", name);
            multipart.addFormField("details", details);
            multipart.addFormField("youtubelink", youtubelink);
            multipart.addFormField("ass_id", ass_id);


            if (selectedImageFile != null) {
                Log.e(TAG, " selected image file name = " + selectedImageFile.getName());

                multipart.addFilePart("image", selectedImageFile);
            }
            List<String> response = multipart.finish();

            Log.v("rht", "SERVER REPLIED:");
            StringBuilder stringBuilder = new StringBuilder();
            for (String line : response) {
                stringBuilder.append(line);


            }
            String finalString = stringBuilder.toString();
            Log.v("rht", "finalString json : "+finalString);


            getActivity().runOnUiThread(() -> {
                _linearLayout.setVisibility(View.VISIBLE);
                _progressBar.setVisibility(View.GONE);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(finalString);

                    boolean _status = jsonObject.getBoolean("status");
                    String msg = jsonObject.getString("message");
                    _edt_name.setText("");
                    edt_text.setText("");
                    _edt_photo_name.setText("");
                    _edt_video_link.setText("");
                    if (_status) {
                        AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),msg,true,0);


                    } else {



                        _linearLayout.setVisibility(View.VISIBLE);
                        AlertDialogManager.showAlertDialog(getActivity(),getResources().getString(R.string.app_name),msg,false,0);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                imageUri  = data.getData();
                Log.e(TAG," uri selected image = "+imageUri);
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {


                         requestPermissions(
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);


                } else {
                    // Permission has already been granted
                    String imagepath = getPath(imageUri);
                    selectedImageFile = new File(imagepath);
                    _edt_photo_name.setText(selectedImageFile.getName());
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getActivity(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    public String getPath(Uri uri) {
        String path;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);
        path = cursor.getString(column_index);
        cursor.close();
        selectedImage = BitmapFactory.decodeFile(filePath);
        return path;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                Log.e(TAG," permission grantResults.length "+grantResults.length);
                Log.e(TAG," permission grantResults[0] "+grantResults[0]);
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.e(TAG," permission garanted");
                    String imagepath = getPath(imageUri);
                    selectedImageFile = new File(imagepath);
                    _edt_photo_name.setText(selectedImageFile.getName());
                } else {

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
