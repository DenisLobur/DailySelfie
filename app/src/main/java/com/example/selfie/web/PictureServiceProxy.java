package com.example.selfie.web;

import com.example.selfie.model.mediator.webdata.Selfie;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

import java.util.Collection;

/**
 * Created by denis on 10/24/15.
 */
public interface PictureServiceProxy {
    String TOKEN_PATH = "/oauth/token";
    String PICTURES_PATH = "/picture";
    String PICTURE_ID = "id";
    String PICTURE_ID_PATH = PICTURES_PATH + "/{id}";
    String PICTURE_DATA = "data";

    @POST(PICTURES_PATH)
    public Selfie sendPictureData(@Body Selfie selfie);

    @GET(PICTURES_PATH)
    public Collection<Selfie> getSelfies();




}
