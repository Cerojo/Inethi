package com.locit.cecilhlungwana.inethi;
/*
Song Abstract Class
This class is used to create an object of a song
 */

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.widget.ImageView;

/**
 * Created by cecilhlungwana on 2017/09/03.
 */

public class Song {
    private String songName; //Song name
    private String artistName; //Artist name
    private String size;
    private String duration;
    private Bitmap cover;
    private MediaPlayer song;

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

    public MediaPlayer getSong() {
        return song;
    }

    public void setSong(MediaPlayer song) {
        this.song = song;
    }
}
