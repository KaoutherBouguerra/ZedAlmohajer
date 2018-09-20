package com.art4muslim.zedalmouhajer.models;

/**
 * Created by macbook on 09/09/2018.
 */

public class Sound {

    private String id;
    private String name;
    private String soundcloud;
    private String date;

    public Sound(String id, String name, String soundcloud, String date) {
        this.id = id;
        this.name = name;
        this.soundcloud = soundcloud;
        this.date = date;
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

    public String getSoundcloud() {
        return soundcloud;
    }

    public void setSoundcloud(String soundcloud) {
        this.soundcloud = soundcloud;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
