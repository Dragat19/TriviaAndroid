package com.playtown.triviaplay.model;

/**
 * Created by albertsanchez on 30/10/17.
 */

public class User {

    private String name;
    private int puntuacion;
    private int index;


    public User() {
    }

    public User(String name, int puntuacion, int index) {
        this.name = name;
        this.puntuacion = puntuacion;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

}
