package com.locit.cecilhlungwana.inethi;
//Class used to create a USER object for a database

import android.graphics.Bitmap;

/**
 * Created by cecilhlungwana on 2017/09/27.
 */

public class USER {

    //User variables
    private int _id;
    private String _username;
    private String _password;
    private String name;
    private String bio;
    private Bitmap image;

    USER() {}

    public USER(int id, String username, String password) {
        this._id = id;
        this._username = username;
        this._password = password;
    }

    public USER(String username, String password) {
        this._username = username;
        this._password = password;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setUserName(String productname) {
        this._username = productname;
    }

    public String getUserName() {
        return this._username;
    }

    public void setPassword(String quantity) {
        this._password = quantity;
    }

    public String getPassword() {
        return this._password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public byte[] getImage() {
        return DbBitmapUtility.getBytes(image);
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
