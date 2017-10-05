package com.locit.cecilhlungwana.inethi;

import android.media.MediaPlayer;
import android.os.Environment;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import layout.MusicFragment;

/**
 * Created by cecilhlungwana on 2017/10/05.
 */

public class SongManager {
    //private SimpleItemRecyclerViewAdapter mAdapter;
    // Search edit box
    private EditText searchBox;
    //******************************************
    private final String fileName = "file_name";
    private final String filePath = "file_path";
    private ArrayList<HashMap<String, String>> soundList;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Inethi/" + "Download/";
    private final String fileFormat = ".mp3";
    private List<Song> songs;
    private List<Song> filteredSongs;
    private MediaPlayer song;
    private int counter = 0;
    private boolean threadRunning = false;
    private static boolean state = false;
    private Runnable runnable;
    private Thread myThread;

    SongManager(){

    }

    SongManager(String path){

    }
}
