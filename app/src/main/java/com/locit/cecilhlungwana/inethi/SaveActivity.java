package com.locit.cecilhlungwana.inethi;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import layout.SaveFragment;

public class SaveActivity extends AppCompatActivity {

    private final String createBeat = "BEAT";
    private String audioName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        Fragment fragment = new SaveFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.save, fragment);
        transaction.commit();
    }
}
