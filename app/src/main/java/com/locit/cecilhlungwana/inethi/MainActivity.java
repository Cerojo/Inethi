package com.locit.cecilhlungwana.inethi;
/*
- Main Class
- Handles Navigation Listener
- Handles Fragment Switching
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import layout.LoginFragment;
import layout.MusicFragment;

public class MainActivity extends AppCompatActivity {
    //Private variable for class
    private int FRAGMENT_ID;
    private TextView mTextMessage;
    private File mainDirectory = Environment.getExternalStorageDirectory();

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
                    mTextMessage.setText(R.string.profile);
                    changeFragment(new LoginFragment());
                    return true;
                case R.id.navigation_download:
                    mTextMessage.setText(R.string.download);
                    changeFragment(new DownloadFragment());
                    return true;
                case R.id.navigation_upload:
                    mTextMessage.setText(R.string.upload);
                    changeFragment(new UploadFragment());
                    return true;
                case R.id.navigation_music:
                    mTextMessage.setText(R.string.music);
                    changeFragment(new MusicFragment());
                    return true;
            }
            return false;
        }

    };

    /*
    Method for handling to process of switching fragments
    Parameters: Fragment ID, SearchBar hint text, List of songs to be displayed.
     */
    private void changeFragment(Fragment fragment){
        FragmentManager fragmentManager;
        FragmentTransaction transaction;
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, fragment); //Switch fragments
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
        mTextMessage.setText(R.string.profile);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        createFolders();
    }

    private void createFolders(){
        File inethiFolder = new File(mainDirectory+ "/Inethi/");
        if(!inethiFolder.exists()){
            inethiFolder.mkdir();
        }

        File beatsFolder = new File(mainDirectory+ "/Inethi/Beats/");
        if(!beatsFolder.exists()){
            beatsFolder.mkdir();
        }

        File createdFolder = new File(mainDirectory+ "/Inethi/Beats/Create/");
        if(!createdFolder.exists()){
            createdFolder.mkdir();
        }

        File downloadedFolder = new File(mainDirectory+ "/Inethi/Beats/Download/");
        if(!downloadedFolder.exists()){
            downloadedFolder.mkdir();
        }

        File loadedFolder = new File(mainDirectory+ "/Inethi/Beats/Load/");
        if(!loadedFolder.exists()){
            loadedFolder.mkdir();
        }

        File voiceFolder = new File(mainDirectory+ "/Inethi/Beats/Voice/");
        if(!voiceFolder.exists()){
            voiceFolder.mkdir();
        }

        File downloadFolder = new File(mainDirectory+ "/Inethi/Download");
        if(!downloadFolder.exists()){
            downloadFolder.mkdir();
        }

        File userFolder = new File(mainDirectory+ "/Inethi/User");
        if(!userFolder.exists()){
            userFolder.mkdir();
        }
    }
}
