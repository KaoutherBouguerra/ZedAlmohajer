package com.art4muslim.zedalmouhajer.models;

/**
 * Created by macbook on 09/09/2018.
 */

public class Video {

    private String id;
    private String name;
    private String image;
    private String date;
    private String link;
    private String embed;
    private String hits;
    private String idvideo;
    private String slug;

    public Video(String id, String name, String image, String date, String link, String embed, String hits, String idvideo, String slug) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.date = date;
        this.link = link;
        this.embed = embed;
        this.hits = hits;
        this.idvideo = idvideo;
        this.slug = slug;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getEmbed() {
        return embed;
    }

    public void setEmbed(String embed) {
        this.embed = embed;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getIdvideo() {
        return idvideo;
    }

    public void setIdvideo(String idvideo) {
        this.idvideo = idvideo;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
