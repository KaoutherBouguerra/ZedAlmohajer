package com.art4muslim.zedalmouhajer.models;

/**
 * Created by macbook on 28/08/2018.
 */

public class City {

    int id;
    String name;

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
