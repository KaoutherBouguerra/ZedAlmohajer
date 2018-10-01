package com.art4muslim.zedalmouhajer.models;

import java.io.Serializable;

/**
 * Created by macbook on 19/08/2018.
 */

public class Association implements Serializable {

    private String id;
    private String id_user;
    private String name;
    private String mobile;
    private String city_id;
    private String email;
    private String password;
    private String copypassword;
    private String image;
    private String date;
    private String about;
    private String facebook;
    private String twitter;
    private String youtube;
    private String instagram;
    private String googleplus;
    private String slug;
    private String emailencrption;


    private String status;
    private String name_user;
    private String email_user;
    private String mobile_user;

    public Association(String id, String name, String mobile, String image) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.image = image;
    }


    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public Association(String id, String id_user, String name, String mobile, String city_id, String email, String password, String copypassword, String image, String date, String about, String facebook, String twitter, String youtube, String instagram, String googleplus, String slug, String emailencrption, String status, String name_user, String email_user, String mobile_user) {
        this.id = id;
        this.id_user = id_user;
        this.name = name;
        this.mobile = mobile;
        this.city_id = city_id;
        this.email = email;
        this.password = password;
        this.copypassword = copypassword;
        this.image = image;
        this.date = date;
        this.about = about;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
        this.instagram = instagram;
        this.googleplus = googleplus;
        this.slug = slug;
        this.emailencrption = emailencrption;
        this.status = status;
        this.name_user = name_user;
        this.email_user = email_user;
        this.mobile_user = mobile_user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCopypassword() {
        return copypassword;
    }

    public void setCopypassword(String copypassword) {
        this.copypassword = copypassword;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getEmailencrption() {
        return emailencrption;
    }

    public void setEmailencrption(String emailencrption) {
        this.emailencrption = emailencrption;
    }
}
