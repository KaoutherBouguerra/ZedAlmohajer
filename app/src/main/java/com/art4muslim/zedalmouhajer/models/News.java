package com.art4muslim.zedalmouhajer.models;

/**
 * Created by macbook on 23/08/2018.
 */

public class News {
    String id;
    String name;
    String details;
    String image;
    String date;
    String slug;
    String youtubelink;
    String ass_id;
    String active;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public News(String id, String name, String details, String image, String date, String slug, String youtubelink, String ass_id, String active) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.image = image;
        this.date = date;
        this.slug = slug;
        this.youtubelink = youtubelink;
        this.ass_id = ass_id;
        this.active = active;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getYoutubelink() {
        return youtubelink;
    }

    public void setYoutubelink(String youtubelink) {
        this.youtubelink = youtubelink;
    }

    public String getAss_id() {
        return ass_id;
    }

    public void setAss_id(String ass_id) {
        this.ass_id = ass_id;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
