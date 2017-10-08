package com.locit.cecilhlungwana.inethi;
/*
- Main Class
- Handles Navigation Listener
- Handles Fragment Switching
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import layout.DownloadFragment;
import layout.LoginFragment;
import layout.MusicFragment;
import layout.UploadFragment;

public class MainActivity extends AppCompatActivity {
    //Private variable for class
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

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    //Create folders for the app
    private void createFolders(){
        File inethiFolder = new File(mainDirectory+ "/Inethi/"); //Main folder
        if(!inethiFolder.exists()){
            inethiFolder.mkdir();
        }

        File musicFolder = new File(mainDirectory+ "/Inethi/MUSIC/"); //For downloaded Music
        if(!musicFolder.exists()){
            musicFolder.mkdir();
        }

        File uploadFolder = new File(mainDirectory+ "/Inethi/UPLOAD/"); //For created Music
        if(!uploadFolder.exists()){
            uploadFolder.mkdir();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    createFolders();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Permission denied to read External storage", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
