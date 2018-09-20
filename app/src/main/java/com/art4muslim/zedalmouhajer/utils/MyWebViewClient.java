package com.art4muslim.zedalmouhajer.utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by macbook on 09/09/2018.
 */

public class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
