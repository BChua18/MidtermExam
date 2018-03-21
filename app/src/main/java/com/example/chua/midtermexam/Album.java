package com.example.chua.midtermexam;

/**
 * Created by Chua on 2/13/2017.
 */


public class Album {
    private String name;
    private String artist;
    private String url;
    private String image;

    public Album(String name, String artist, String url, String image) {
        this.name = name;
        this.artist = artist;
        this.url = url;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

}