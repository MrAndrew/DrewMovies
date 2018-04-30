package com.example.android.drewmovies.models;

import java.io.Serializable;

//serilizable to allow transfer of an object instance to an intent
public class Movie implements Serializable {

    private Integer movieId;
    private boolean hasVideo;
    private String movieTitle;
    private String imageUrlpath;
    private String about;
    private String releaseDate;
    private Double userRating;

    public Movie() {

    }

    public Movie (Integer movieId, boolean hasVideo, String movieTitle, String imageUrlpath,
                  String about, String releaseDate, Double userRating) {
        this.movieId = movieId;
        this.hasVideo = hasVideo;
        this.movieTitle = movieTitle;
        this.imageUrlpath = imageUrlpath;
        this.about = about;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
    }

    //getters
    public Integer getMovieId() {
        return movieId;
    }
    public boolean isHasVideo() {
        return hasVideo;
    }
    public String getMovieTitle() {
        return movieTitle;
    }
    public String getImageUrlpath() {
        return imageUrlpath;
    }
    public String getAbout() {
        return about;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public Double getUserRating() {
        return userRating;
    }

    //setters
    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }
    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }
    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }
    public void setImageUrlpath(String imageUrlpath) {
        this.imageUrlpath = imageUrlpath;
    }
    public void setAbout(String about) {
        this.about = about;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    public void setUserRating(Double userRating) { this.userRating = userRating; }

}
