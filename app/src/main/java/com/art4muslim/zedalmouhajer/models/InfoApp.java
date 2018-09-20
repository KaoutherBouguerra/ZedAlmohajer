package com.art4muslim.zedalmouhajer.models;

/**
 * Created by macbook on 02/09/2018.
 */

public class InfoApp {
    String siteemail;
    String mob;
    String facebook;
    String twitter;
    String youtube;
    String instagram;
    String googleplus;

    public InfoApp(String siteemail, String mob, String facebook, String twitter, String youtube, String instagram, String googleplus) {
        this.siteemail = siteemail;
        this.mob = mob;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
        this.instagram = instagram;
        this.googleplus = googleplus;
    }

    public String getSiteemail() {
        return siteemail;
    }

    public void setSiteemail(String siteemail) {
        this.siteemail = siteemail;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getGoogleplus() {
        return googleplus;
    }

    public void setGoogleplus(String googleplus) {
        this.googleplus = googleplus;
    }
}

