package com.art4muslim.zedalmouhajer.menu;

/**
 * Created by macbook on 30/08/2018.
 */

public class Beneficiar {
    private String active;
    private String id;
    private String name;

    public Beneficiar(String active, String id, String name) {
        this.active = active;
        this.id = id;
        this.name = name;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
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
}
