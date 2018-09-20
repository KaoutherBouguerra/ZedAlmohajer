package com.art4muslim.zedalmouhajer;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.art4muslim.zedalmouhajer.models.Association;
import com.art4muslim.zedalmouhajer.models.InfoApp;
import com.art4muslim.zedalmouhajer.session.SessionManager;

import java.util.ArrayList;
import java.util.Locale;

import static com.art4muslim.zedalmouhajer.features.BaseActivity.getLocale;

/**
 * Created by macbook on 19/08/2018.
 */

public class BaseApplication extends Application {
    public static SessionManager session;
    private static BaseApplication mInstance;
    private Association association;
    public static final String TAG = BaseApplication.class.getSimpleName();

    InfoApp infoApp;
    String abouAppText;
    String termsText;

    public InfoApp getInfoApp() {
        return infoApp;
    }

    public void setInfoApp(InfoApp infoApp) {
        this.infoApp = infoApp;
    }

    public String getTermsText() {
        return termsText;
    }

    public void setTermsText(String termsText) {
        this.termsText = termsText;
    }

    public String getAbouAppText() {
        return abouAppText;
    }

    public void setAbouAppText(String abouAppText) {
        this.abouAppText = abouAppText;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        session = new SessionManager(getApplicationContext());
        setLocale();
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }


    public static ArrayList<Association> associations = new ArrayList<Association>();
    private RequestQueue mRequestQueue;

    public static ArrayList<Association> getAssociations() {
        return associations;
    }

    public static void setAssociations(ArrayList<Association> associations) {
        BaseApplication.associations = associations;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setLocale() {

        final Resources resources = getResources();
        final Configuration configuration = resources.getConfiguration();
        final Locale locale = getLocale(this);
        if (!configuration.locale.equals(locale)) {
            configuration.setLocale(locale);
            resources.updateConfiguration(configuration, null);
        }
    }
}
