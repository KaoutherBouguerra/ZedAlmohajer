package com.art4muslim.zedalmouhajer.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.art4muslim.zedalmouhajer.features.LoginActivity;

import java.util.HashMap;

/**
 * Created by macbook on 20/08/2018.
 */

public class SessionManager {

    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "zedMohajerPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_IS_FROM = "Is_FROM";
    public static final String KEY_NAME = "name";
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final  String Key_UserIMAGE="USERIMAGE";
    public static final  String Key_UserID="ID";
    private static final  String Key_UserName="NAME";
    private static final  String Key_UserSESSION="SESSION";
    public static final  String Key_UserPhone="USERPHONE";
    private static final  String Key_UserEmail="USEREMAIL";
    private static final  String Key_UserMAPX="Key_UserMAPX";
    private static final  String Key_UserMAPY="Key_UserMAPY";
    private static final  String Key_UserCITY_ID="USERCITYID";
    private static final  String Key_UserCITY_NAME="USERCITYNAME";
    private static final  String Key_LANGUAGE="LANGUAGE";
    private static final  String Key_ServiceID="ID_SERVICE";
    private static final  String Key_cityId="CityId";
    private static final  String Key_Currency="Currency";
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    public static final String KEY_Acesstoken = "access_token";
    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void saveAccessToken(String accessToken){
        editor.putString(KEY_Acesstoken,accessToken);
        editor.commit();


    }

    public void saveKeyIsFrom(String isFrom){
        editor.putString(KEY_IS_FROM, isFrom);
        editor.commit();

    }
    /**
     * Get stored session data
     */
    public HashMap<String, String > getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(Key_UserID, pref.getString(Key_UserID, null));

        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(Key_UserPhone, pref.getString(Key_UserPhone, null));
        user.put(Key_UserIMAGE, pref.getString(Key_UserIMAGE, null));

        // return user
        return user;
    }
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getIsFrom(){
        return pref.getString(KEY_IS_FROM,"GENERAL");
    }

    public void createLoginSession(String id,String name, String phone, String phoneHome,String thumbnail, String status) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN,true);
        // storing access token in pref
        editor.putString(Key_UserID, id);
        Log.v("id", ""+id);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing phone in pref
        editor.putString(Key_UserPhone, phone);
        Log.v("key phone", phone);

        // Storing email in pref
        // editor.putString(Key_UserPhoneHome, phoneHome);
        // Log.v("key phoneHome", phoneHome);

        // Storing email in pref
        editor.putString(Key_UserIMAGE, thumbnail);
        Log.v("key thumbnail", thumbnail);

        // Storing status in pref
        //      editor.putString(Key_UserSTATUS, status);
        Log.v("key status", status);

        // commit changes
        editor.commit();
    }
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();


        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);

    }
}
