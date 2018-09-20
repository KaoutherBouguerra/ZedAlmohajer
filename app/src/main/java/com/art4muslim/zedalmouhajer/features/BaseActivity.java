package com.art4muslim.zedalmouhajer.features;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    private Locale mCurrentLocale;

    @Override
    protected void onStart() {
        super.onStart();

        mCurrentLocale = getResources().getConfiguration().locale;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Locale locale = getLocale(this);

        if (!locale.equals(mCurrentLocale)) {

            mCurrentLocale = locale;
            recreate();
        }
    }

    public static Locale getLocale(Context context){

        String lang = "ar";

        return new Locale(lang);
    }
}
