package com.art4muslim.zedalmouhajer.features;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.art4muslim.zedalmouhajer.fragments.AboutAppFragment;
import com.art4muslim.zedalmouhajer.fragments.AddSubjectFragment;
import com.art4muslim.zedalmouhajer.fragments.ApplicationToJoinFragment;
import com.art4muslim.zedalmouhajer.fragments.AssociationsGridFragment;
import com.art4muslim.zedalmouhajer.fragments.TermsAndConditionsFragment;
import com.art4muslim.zedalmouhajer.fragments.contactus.TabContactUsFragment;
import com.art4muslim.zedalmouhajer.fragments.specific_association.NewsBeneficAssociationFragment;
import com.art4muslim.zedalmouhajer.menu.DrawerAdapter;
import com.art4muslim.zedalmouhajer.menu.DrawerItem;
import com.art4muslim.zedalmouhajer.menu.SimpleItem;
import com.art4muslim.zedalmouhajer.models.Association;
import com.art4muslim.zedalmouhajer.session.Constants;
import com.art4muslim.zedalmouhajer.utils.AlertDialogManager;
import com.google.gson.Gson;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.art4muslim.zedalmouhajer.session.SessionManager.KEY_NAME;
import static com.art4muslim.zedalmouhajer.session.SessionManager.Key_UserID;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener{

    private static final int ASS_POS_INFORMATIONS = 0;
    private static final int ASS_POS_ADD_SUBJECT = 1;
    private static final int ASS_POS_BENEFICIARY_REQUESTS = 2;
    private static final int ASS_POS_SHARE_APP = 3;
    private static final int ASS_POS_ABOUT_APP = 4;
    private static final int ASS_POS_ITEMS_CONDITIONS = 5;
    private static final int ASS_POS_CONTACTS_US = 6;
    private static final int ASS_POS_LOGOUT = 7;


    private static final int BEN_POS_ASSOCIATIONS = 0;
    private static final int BEN_POS_YOUR_ASSOCIATIONS = 1;
    private static final int BEN_POS_SHARE_APP = 2;
    private static final int BEN_POS_ABOUT_APP = 3;
    private static final int BEN_POS_ITEMS_CONDITIONS = 4;
    private static final int BEN_POS_CONTACTS_US = 5;
    private static final int BEN_POS_LOGOUT = 6;

    private static final int GENERAL_POS_ASSOCIATIONS = 0;

    private static final int GENERAL_POS_SHARE_APP = 1;
    private static final int GENERAL_POS_ABOUT_APP = 2;
    private static final int GENERAL_POS_ITEMS_CONDITIONS = 3;
    private static final int GENERAL_POS_CONTACTS_US = 4;
    private static final int GENERAL_POS_LOGIN_ASS = 5;
    private static final int GENERAL_POS_LOGIN_BEN = 6;


    private String[] screenTitles;
    String from = null;
    boolean isLoggedIn = false;
    DrawerAdapter adapter;

    BaseApplication baseApplication;
    Association association;
    private SlidingRootNav slidingRootNav;
    ArrayList<Association> associations = new ArrayList<Association>();
    private static String TAG = MainActivity.class.getSimpleName();
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");


  //      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  //      getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed

                finish();
            }
        });

        baseApplication = (BaseApplication) getApplicationContext();

        association = (Association) getIntent().getSerializableExtra("ASSOCIATION");

        baseApplication.setAssociation(association);
        from = BaseApplication.session.getIsFrom();
        isLoggedIn = BaseApplication.session.isLoggedIn();

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withGravity(SlideGravity.RIGHT)
                .withMenuLayout(R.layout.menu_right_drawer)
                .inject();

        // screenIcons = loadScreenIcons();
        if (!isLoggedIn) {

            screenTitles = loadGeneralScreenTitles();

        }  else {
            if (from.equals("BEN"))
                screenTitles = loadBENScreenTitles();
            else screenTitles = loadScreenTitles();

        }

        if (!isLoggedIn) {

            adapter = new DrawerAdapter(Arrays.asList(
                    createItemFor(GENERAL_POS_ASSOCIATIONS).setChecked(true),

                    createItemFor(GENERAL_POS_SHARE_APP),
                    createItemFor(GENERAL_POS_ABOUT_APP),
                    createItemFor(GENERAL_POS_ITEMS_CONDITIONS),
                    createItemFor(GENERAL_POS_CONTACTS_US),
                    createItemFor(GENERAL_POS_LOGIN_ASS),
                    createItemFor(GENERAL_POS_LOGIN_BEN)));

        } else  {

        if (from.equals("BEN")){
            // todo get user associations
            getAllAssociation();
            adapter = new DrawerAdapter(Arrays.asList(
                    createItemFor(BEN_POS_ASSOCIATIONS).setChecked(true),
                    createItemFor(BEN_POS_YOUR_ASSOCIATIONS),
                    createItemFor(BEN_POS_SHARE_APP),
                    createItemFor(BEN_POS_ABOUT_APP),
                    createItemFor(BEN_POS_ITEMS_CONDITIONS),
                    createItemFor(BEN_POS_CONTACTS_US),
                    createItemFor(BEN_POS_LOGOUT)));
        }

        else {
            adapter = new DrawerAdapter(Arrays.asList(
                    createItemFor(ASS_POS_INFORMATIONS).setChecked(true),
                    createItemFor(ASS_POS_ADD_SUBJECT),
                    createItemFor(ASS_POS_BENEFICIARY_REQUESTS),
                    createItemFor(ASS_POS_SHARE_APP),
                    createItemFor(ASS_POS_ABOUT_APP),
                    createItemFor(ASS_POS_ITEMS_CONDITIONS),
                    createItemFor(ASS_POS_CONTACTS_US),
                    createItemFor(ASS_POS_LOGOUT)));

        }
    }

        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        TextView txtName = findViewById(R.id.txtName);
       // if (from.equals("BEN"))
        txtName.setText(BaseApplication.session.getUserDetails().get(KEY_NAME));
      //  else   txtName.setText(association.getName());

        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);


        if (!isLoggedIn) {
            adapter.setSelected(BEN_POS_ASSOCIATIONS);
        } else {
            if (from.equals("BEN"))
                adapter.setSelected(BEN_POS_ASSOCIATIONS);
            else
                adapter.setSelected(ASS_POS_INFORMATIONS);
        }
    }

    @Override
    public void onItemSelected(int position) {

        Log.e(TAG, "onItemSelected pos = " + position);
        if (!isLoggedIn) {

            if (position == GENERAL_POS_ASSOCIATIONS) {

                AssociationsGridFragment schedule = new AssociationsGridFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("ISADDED", false);
                schedule.setArguments(bundle);
                showFragment(schedule);

            } else if (position == GENERAL_POS_SHARE_APP) {
                shareOn("", getString(R.string.app_name), "");
            } else if (position == GENERAL_POS_ABOUT_APP) {

                AboutAppFragment schedule = new AboutAppFragment();
                showFragment(schedule);

            } else if (position == GENERAL_POS_ITEMS_CONDITIONS) {

                TermsAndConditionsFragment schedule = new TermsAndConditionsFragment();
                showFragment(schedule);


            } else if (position == GENERAL_POS_CONTACTS_US) {

                TabContactUsFragment schedule = new TabContactUsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("FROM", "GENERAL");
                schedule.setArguments(bundle);
                showFragment(schedule);

            }else if (position == GENERAL_POS_LOGIN_ASS) {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("FROM","ASSOCIATION");
                startActivity(intent);

            }else if (position == GENERAL_POS_LOGIN_BEN) {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("FROM","BEN");
                startActivity(intent);

            }

        } else {
        if (from.equals("BEN")) {
            if (position == BEN_POS_LOGOUT) {
                finish();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                BaseApplication.session.logoutUser();
                startActivity(intent);

            } else if (position == BEN_POS_ASSOCIATIONS) {

                AssociationsGridFragment schedule = new AssociationsGridFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("ISADDED", false);
                schedule.setArguments(bundle);
                showFragment(schedule);

            } else if (position == BEN_POS_YOUR_ASSOCIATIONS) {

                AssociationsGridFragment schedule = new AssociationsGridFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("ISADDED", true);
                schedule.setArguments(bundle);
                showFragment(schedule);

            } else if (position == BEN_POS_SHARE_APP) {
                shareOn("", getString(R.string.app_name), "");
            } else if (position == BEN_POS_ABOUT_APP) {

                AboutAppFragment schedule = new AboutAppFragment();
                showFragment(schedule);

            } else if (position == BEN_POS_ITEMS_CONDITIONS) {

                TermsAndConditionsFragment schedule = new TermsAndConditionsFragment();
                showFragment(schedule);


            } else if (position == BEN_POS_CONTACTS_US) {

                TabContactUsFragment schedule = new TabContactUsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("FROM", "APP");
                schedule.setArguments(bundle);
                showFragment(schedule);

            }
        } else {
            if (position == ASS_POS_LOGOUT) {
                finish();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                BaseApplication.session.logoutUser();
                startActivity(intent);
            } else if (position == ASS_POS_INFORMATIONS) {
               // setTitle(R.string.item_MyInfo);
                NewsBeneficAssociationFragment schedule = new NewsBeneficAssociationFragment();
                showFragment(schedule);

            } else if (position == ASS_POS_ADD_SUBJECT) {
               // setTitle(R.string.txt_add_subject);
                AddSubjectFragment schedule = new AddSubjectFragment();
                showFragment(schedule);
            } else if (position == ASS_POS_BENEFICIARY_REQUESTS) {
              //  setTitle(R.string.item_add_users);
                ApplicationToJoinFragment schedule = new ApplicationToJoinFragment();
                showFragment(schedule);
            } else if (position == ASS_POS_SHARE_APP) {
                shareOn("", getString(R.string.app_name), "");
            } else if (position == ASS_POS_ABOUT_APP) {

                AboutAppFragment schedule = new AboutAppFragment();
                showFragment(schedule);

            } else if (position == ASS_POS_ITEMS_CONDITIONS) {

                TermsAndConditionsFragment schedule = new TermsAndConditionsFragment();
                showFragment(schedule);


            } else if (position == ASS_POS_CONTACTS_US) {
              //  setTitle(R.string.item_contact);
                TabContactUsFragment schedule = new TabContactUsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("FROM", "APP");
                schedule.setArguments(bundle);
                showFragment(schedule);

            }
        }
    }
        slidingRootNav.closeMenu();
       // Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);

    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace( R.id.container,fragment,"home Fragment");
        fragmentTransaction.commit();
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem( screenTitles[position])
                .withTextTint(color(android.R.color.white))
                .withSelectedIconTint(color(android.R.color.white))
                .withSelectedTextTint(color(android.R.color.white));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ass_activityScreenTitles);
    }

    private String[] loadBENScreenTitles() {
        return getResources().getStringArray(R.array.ben_activityScreenTitles);
    }

    private String[] loadGeneralScreenTitles() {
        return getResources().getStringArray(R.array.general_activityScreenTitles);
    }


    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }


    private void shareOn(String application, String title, String description){


        try{

          //  Intent intent =  getPackageManager().getLaunchIntentForPackage(application);
           // if (intent != null) {
                // The application exists
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                //  shareIntent.setPackage(application);
                shareIntent.setType("message/rfc822");
                shareIntent.putExtra(android.content.Intent.EXTRA_TITLE, title);
                shareIntent.putExtra(Intent.EXTRA_TEXT, description);
                // Start the specific social application
                 startActivity(Intent.createChooser(shareIntent,
                        " حمل هذا التطبيق مجاناً  "));
          //  } else {
                 // The application does not exist
                // Open GooglePlay or use the default system picker
         //   }

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
// show message to user
        }
    }


    private void getAllAssociation(){
        String url  = Constants.GET_ADDED_ASSOS+BaseApplication.session.getUserDetails().get(Key_UserID);
        Log.e(TAG, "getAllAssociation url "+url);

        StringRequest hisRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Log.e(TAG, "getAll added Association response === "+response.toString());


                JSONArray resJsonObj= null;
                try {
                    resJsonObj = new JSONArray(response);

                    for (int i = 0; i<resJsonObj.length();i++) {

                        JSONObject adrJsonObj = resJsonObj.getJSONObject(i);
                        Association association = gson.fromJson(String.valueOf(adrJsonObj), Association.class);
                        Log.e(TAG, "getAllAssociation ass name === "+association.getName());
                        associations.add(association);
                        baseApplication.setAssociations(associations);
                    }

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("/////// VOLLEY  ///// ", error.toString());


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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
