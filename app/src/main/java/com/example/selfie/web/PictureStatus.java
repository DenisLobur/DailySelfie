package com.example.selfie.web;

/**
 * Created by denis on 10/24/15.
 */
public class PictureStatus {

    public enum PictureState {
        READY, PROCESSING
    }

    private PictureStatus pictureStatus;

    public PictureStatus getStatus() {
        return pictureStatus;
    }
}
