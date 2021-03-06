package com.locit.cecilhlungwana.inethi;

/**
 * Created by cecilhlungwana on 2017/09/27.
 */
//Local SQL database to hold user information

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserInfoDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userDB.db";
    private static final String TABLE_USER = "user";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_BIO = "bio";
    private static final String COLUMN_IMAGE = "image";


    public UserInfoDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " +
                TABLE_USER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_BIO + " TEXT,"
                + COLUMN_IMAGE + " BLOB"+")";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    //Method to create a User object
    public void addUser(USER USER) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, USER.getUserName());
        values.put(COLUMN_PASSWORD, USER.getPassword());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    //Method to add information of the user
    public void addInfo(USER USER) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, USER.getName());
        values.put(COLUMN_BIO, USER.getBio());
        values.put(COLUMN_IMAGE, USER.getImage());

        SQLiteDatabase db = this.getWritableDatabase();

        db.update(TABLE_USER, values, COLUMN_ID+" = '" + USER.getID() + "'", null);
        db.close();
    }

    //Method to lookup a user
    public USER findUser(String username) {
        String query = "Select * FROM " + TABLE_USER + " WHERE " + COLUMN_USERNAME + " =  \"" + username + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        USER USER = new USER();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            USER.setID(Integer.parseInt(cursor.getString(0)));
            USER.setUserName(cursor.getString(1));
            USER.setPassword(cursor.getString(2));
            USER.setName(cursor.getString(3));
            USER.setBio(cursor.getString(4));
            try {
                USER.setImage(DbBitmapUtility.getImage(cursor.getBlob(5)));
            }
            catch (Exception ignored){}
            cursor.close();
        } else {
            USER = null;
        }
        db.close();
        return USER;
    }

    //Method to delete a user
    public boolean deleteUser(String username) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_USER + " WHERE " + COLUMN_USERNAME + " =  \"" + username + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        USER USER = new USER();

        if (cursor.moveToFirst()) {
            USER.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_USER, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(USER.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
