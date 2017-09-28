package com.locit.cecilhlungwana.inethi;

/**
 * Created by cecilhlungwana on 2017/09/27.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserInfoDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userDB.db";
    private static final String TABLE_USER = "user";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BIO = "bio";

    public UserInfoDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_USER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_BIO + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void addUser(USER USER) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, USER.getUserName());
        values.put(COLUMN_PASSWORD, USER.getPassword());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USER, null, values);
        db.close();
    }

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
            cursor.close();
        } else {
            USER = null;
        }
        db.close();
        return USER;
    }

    public boolean deleteUser(String productname) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_USER + " WHERE " + COLUMN_USERNAME + " =  \"" + productname + "\"";

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

/*
    public void newUser (View view) {
        UserInfoDBHandler dbHandler = new UserInfoDBHandler(this, null, null, 1);

        int quantity =
                Integer.parseInt(quantityBox.getText().toString());

        USER product =
                new USER(productBox.getText().toString(), quantity);

        dbHandler.addUser(product);
        productBox.setText("");
        quantityBox.setText("");
    }

    public void lookupUser (View view) {
        UserInfoDBHandler dbHandler = new UserInfoDBHandler(this, null, null, 1);

        USER product =
                dbHandler.findUser(productBox.getText().toString());

        if (product != null) {
            idView.setText(String.valueOf(product.getID()));

            quantityBox.setText(String.valueOf(product.getPassword()));
        } else {
            idView.setText("No Match Found");
        }
    }

    public void removeProduct (View view) {
        UserInfoDBHandler dbHandler = new UserInfoDBHandler(this, null,
                null, 1);

        boolean result = dbHandler.deleteUser(
                productBox.getText().toString());

        if (result)
        {
            idView.setText("Record Deleted");
            productBox.setText("");
            quantityBox.setText("");
        }
        else
            idView.setText("No Match Found");
    }
 */
