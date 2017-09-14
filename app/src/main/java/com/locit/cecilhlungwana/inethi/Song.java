package com.locit.cecilhlungwana.inethi;
/*
Song Abstract Class
This class is used to create an object of a song
 */

/**
 * Created by cecilhlungwana on 2017/09/03.
 */

public class Song {
    private int iconId; //Cover image id
    private String songName; //Song name
    private String artistName; //Artist name
    private int songId;

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

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

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }
}
