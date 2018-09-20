package com.art4muslim.zedalmouhajer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;

/**
 * Created by macbook on 21/12/2017.
 */

public class SavePhotoParams {


    Context cnx;
    String name;
    String youtubelink;
    String details;
    String ass_id;
    Bitmap image;
    LinearLayout _linearLayout;
    ProgressBar _progressBar;

    public SavePhotoParams(Context cnx, String name, String youtubelink, String details, String ass_id, Bitmap image, LinearLayout _linearLayout, ProgressBar _progressBar) {
        this.cnx = cnx;
        this.name = name;
        this.youtubelink = youtubelink;
        this.details = details;
        this.ass_id = ass_id;
        this.image = image;
        this._linearLayout = _linearLayout;
        this._progressBar = _progressBar;
    }

    public LinearLayout get_linearLayout() {
        return _linearLayout;
    }

    public void set_linearLayout(LinearLayout _linearLayout) {
        this._linearLayout = _linearLayout;
    }

    public ProgressBar get_progressBar() {
        return _progressBar;
    }

    public void set_progressBar(ProgressBar _progressBar) {
        this._progressBar = _progressBar;
    }

    public Context getCnx() {
        return cnx;
    }

    public void setCnx(Context cnx) {
        this.cnx = cnx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYoutubelink() {
        return youtubelink;
    }

    public void setYoutubelink(String youtubelink) {
        this.youtubelink = youtubelink;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAss_id() {
        return ass_id;
    }

    public void setAss_id(String ass_id) {
        this.ass_id = ass_id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}

