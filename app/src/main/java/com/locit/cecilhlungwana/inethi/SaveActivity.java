package com.locit.cecilhlungwana.inethi;
//Class for handling saving of Music

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import layout.SaveFragment;

public class SaveActivity extends AppCompatActivity {

    private final String createBeat = "BEAT";
    private String audioName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        Bundle saveData = getIntent().getExtras();
        if (saveData != null)
        {
            audioName = saveData.getString(createBeat);
            Bundle bundle = new Bundle();
            bundle.putString(createBeat, audioName);
            Fragment fragment = new SaveFragment();
            fragment.setArguments(bundle);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.save, fragment);
            transaction.commit();
        }
        else {
            Fragment fragment = new SaveFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.save, fragment);
            transaction.commit();
        }
    }
}
