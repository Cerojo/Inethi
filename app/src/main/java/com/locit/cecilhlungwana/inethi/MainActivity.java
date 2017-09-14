package com.locit.cecilhlungwana.inethi;
/*
- Main Class
- Handles Navigation Listener
- Handles Fragment Switching
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import layout.LoginFragment;
import layout.MusicFragment;

public class MainActivity extends AppCompatActivity {
    //Private variable for class
    private int FRAGMENT_ID;
    private final String uploadString = "Search song to Upload";
    private final String downloadString = "Search song to Download";
    private TextView mTextMessage;

    /*
    Method that handlers Navigation Listener and Fragment Change
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            /*
            Uses item id to switch between fragments.
             */
            switch (item.getItemId()) {
                /*
                Exception for the Beat item id, where an activity gets called.
                 */
                case R.id.navigation_beat:
                    Intent intent = new Intent(getApplicationContext(), BeatActivity.class); //Load The Activity
                    startActivity(intent);  //Start the Activity
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.profile);           //Load The Header Title
                    FRAGMENT_ID = 0;                          //Fragment ID for knowing which fragment to change to.
                    changeFragment(FRAGMENT_ID);
                    return true;
                case R.id.navigation_download:
                    mTextMessage.setText(R.string.download);
                    FRAGMENT_ID = 1;
                    changeFragment(FRAGMENT_ID);
                    return true;
                case R.id.navigation_upload:
                    mTextMessage.setText(R.string.upload);
                    FRAGMENT_ID = 2;
                    changeFragment(FRAGMENT_ID);
                    return true;
                case R.id.navigation_music:
                    mTextMessage.setText(R.string.music);
                    FRAGMENT_ID = 3;
                    changeFragment(FRAGMENT_ID);
                    return true;
            }
            return false;
        }

    };

    /*
    Method for handling to process of switching fragments
    Parameters: Fragment ID, SearchBar hint text, List of songs to be displayed.
     */
    private void changeFragment(final int id){
        FragmentManager fragmentManager;
        FragmentTransaction transaction;

        //This only get changed when the new fragment is the Login fragment.
        if(id == 0){
            Fragment fragment = new LoginFragment(); //Load new fragment
            fragmentManager = getSupportFragmentManager();
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content, fragment); //Switch fragments
            transaction.commit();
            return; //Exit
        }

        //All the other fragments have the same features
        MusicFragment musicFragment = new MusicFragment(); //Load new fragment
        musicFragment.setMusicFragment(id); //Set ID

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, musicFragment); //Switch fragments
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //First fragment to be load on start.
        Fragment fragment = new LoginFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();

        //Set up Navigation controls
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
