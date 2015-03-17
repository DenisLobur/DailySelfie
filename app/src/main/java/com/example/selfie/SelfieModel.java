package com.example.selfie;

/**
 * Created by denis on 3/17/15.
 */
public class SelfieModel {

    private String name;
    private String path;

    public SelfieModel(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
