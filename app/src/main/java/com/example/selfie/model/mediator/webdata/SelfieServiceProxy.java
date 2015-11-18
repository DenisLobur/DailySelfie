package com.example.selfie.model.mediator.webdata;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

import java.util.Collection;

public interface SelfieServiceProxy {

    public static final String PICTURE_SVC_PATH = "/picture";
    public static final String CONFIRM_PARAMETER = "confirm";
    public static final String PICTURE_CONFIRM_PATH = PICTURE_SVC_PATH + "/" + CONFIRM_PARAMETER;
    public static final String TOKEN_PATH = "/oauth/token";
    public static final String VIDEO_SVC_PATH = "/video";

    @GET(PICTURE_CONFIRM_PATH)
    public boolean isPicLoggedIn();

    @GET(PICTURE_SVC_PATH)
    public Collection<Selfie> getPictureList();

    @POST(PICTURE_SVC_PATH)
    public Selfie addPicture(@Body Selfie s);
}
