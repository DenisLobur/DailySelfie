package com.example.selfie.model.mediator.webdata;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Objects;

/**
 * This "Plain Ol' Java Object" (POJO) class represents meta-data of
 * interest downloaded in Json from the Selfie Service via the
 * SelfieServiceProxy.
 */
public class Selfie implements Serializable {

    /**
     * Various fields corresponding to data downloaded in Json from
     * the Selfie WebService.
     */
    private long id;
    private String title;
    private String url;
    private long duration;
    private String location;
    private String subject;
    private String contentType;
    private byte[] pictureBlob;
    private transient long likes;
    private transient float rating;
    @JsonIgnore
    private String owner;

    public Selfie() {
    }

    public Selfie(String owner, String title, String url, long duration, long likes, float rating) {
        super();
        this.duration = duration;
        this.owner = owner;
        this.url = url;
        this.title = title;
        this.likes = likes;
        this.rating = rating;
    }

    public Selfie(String title, byte[] pictureBlob) {
        this.title = title;
        this.pictureBlob = pictureBlob;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public byte[] getPictureBlob() {
        return pictureBlob;
    }

    public void setPictureBlob(byte[] pictureBlob) {
        this.pictureBlob = pictureBlob;
    }

    /**
     * Get the Id of the Selfie.
     *
     * @return id of video
     */
    public long getId() {
        return id;
    }

    /**
     * Get the Selfie by Id
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the Title of Selfie.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the Title of Selfie.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the Duration of Selfie.
     *
     * @return Duration of Selfie.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Set the Duration of Selfie.
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Get ContentType of Selfie.
     *
     * @return contentType of Selfie.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Set the ContentType of Selfie.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getStarRating() {
        return rating;
    }

    public void setStarRating(float starRating) {
        this.rating = starRating;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return the textual representation of Selfie object.
     */
    @Override
    public String toString() {
        return "{" +
                "Id: " + id + ", " +
                "Title: " + title + ", " +
                "Duration: " + duration + ", " +
                "Location: " + location + ", " +
                "Subject: " + subject + ", " +
                "ContentType: " + contentType + ", " +
                "URL: " + url + ", " +
                "Rating: " + rating + ", " +
                "Total likes: " + likes +
                "}";
    }

    /**
     * @return an Integer hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, url, duration, owner);
    }

    /**
     * @return Compares this Selfie instance with specified
     * Selfie and indicates if they are equal.
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Selfie)
                && Objects.equals(getTitle(),
                ((Selfie) obj).getTitle())
                && getDuration() == ((Selfie) obj).getDuration()
                && getOwner() == ((Selfie) obj).getOwner();
    }
}
