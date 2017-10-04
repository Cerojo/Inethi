package layout;
/*
The beats fragment class handles the view
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.locit.cecilhlungwana.inethi.R;
import com.locit.cecilhlungwana.inethi.Song;
import com.locit.cecilhlungwana.inethi.SongAdapter;
import com.locit.cecilhlungwana.inethi.VerticalSeekBar;
import com.locit.cecilhlungwana.inethi.VisualizerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeatFragment extends Fragment {
    private MediaPlayer song;
    private MediaPlayer music;
    private MediaPlayer voice;
    private int seekbarInt = 0;
    private int[] sound1Inputs = {0,0,0,0};
    private int[] sound2Inputs = {0,0,0,0};
    private int[] sound3Inputs = {0,0,0,0};
    private int[] sound4Inputs = {0,0,0,0};

    private MediaPlayer sound1;
    private MediaPlayer sound2;
    private MediaPlayer sound3;
    private MediaPlayer sound4;


    private Boolean sound1Boolean = true;
    private Boolean sound2Boolean = true;
    private Boolean sound3Boolean = true;
    private Boolean sound4Boolean = true;

    private Button sound1Button;
    private Button sound2Button;
    private Button sound3Button;
    private Button sound4Button;
    private Button saveButton;
    private Button mMusic;
    private Button mVoice;

    private ImageButton playButton;
    private ImageButton stopButton;
    private ImageButton recordButton;
    private ImageButton micButton;

    private ImageButton volumeButton;
    private ImageButton musicButton;

    private Boolean playBoolean = true;
    private Boolean stopBoolean = true;
    private Boolean recordBoolean = true;
    private Boolean micBoolean = true;
    private Boolean musicBoolean = true;
    private Boolean mBoolean = true;
    private Boolean vBoolean = true;

    private Boolean volumeState = true;

    private ToggleButton k;
    private ToggleButton h;
    private ToggleButton s;
    private ToggleButton c;

    private SeekBar beatsBar;
    private VerticalSeekBar volume;
    private VerticalSeekBar tempo;

    //Toggle button IDs
    private int sound1Toggle[] = {R.id.k1toggleButton, R.id.k2toggleButton, R.id.k3toggleButton, R.id.k4toggleButton};
    private int sound2Toggle[] = {R.id.c1toggleButton, R.id.c2toggleButton, R.id.c3toggleButton, R.id.c4toggleButton};
    private int sound3Toggle[] = {R.id.h1toggleButton, R.id.h2toggleButton, R.id.h3toggleButton, R.id.h4toggleButton};
    private int sound4Toggle[] = {R.id.s1toggleButton, R.id.s2toggleButton, R.id.s3toggleButton, R.id.s4toggleButton};

    public static int beats[] = {R.raw.kick, R.raw.hihat, R.raw.clap, R.raw.snare};

    private int seekForwardTime = 5 * 1000; // default 5 second
    private int seekBackwardTime = 5 * 1000; // default 5 second
    private RecyclerView defaultRecyclerView;
    private SongAdapter songAdapter;
    private boolean threadRunning = false;
    private Runnable runnable;
    private Thread myThread;

    private int color = Color.rgb(105,26,153);

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Inethi/Beats/Voice";
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    private PopupWindow pw;
    private View view = null;

    private final static int MAX_VOLUME = 100;

    private int bpm = 522;

    //******************************************
    private final String fileName = "file_name";
    private final String filePath = "file_path";
    private ArrayList<HashMap<String, String>> soundList;
    private String pathDefault = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Inethi/";// + "Download/";
    private String pathMusic = pathDefault+"Download/";
    private String pathVoice = pathDefault +"Beats/Voice";
    //private String
    private String path = "";
    private String fileFormat;
    private final String mp3Format = ".mp3";
    private final String audioFormat = ".MPEG_4";
    private List<Song> songs;
    private List<Song> filteredSongs;
    private int counter = 0;
    private static boolean state = false;
    private EditText searchBox;
    private SimpleItemRecyclerViewAdapter mAdapter;
    private boolean threadRunningUP = false;
    private Runnable runnableUP;
    private Thread myThreadUP;
    private Boolean instrument;
    private Button sButton;
    private int buttonColumn;
    private Boolean musicState = false;
    private int maxVolume;
    private AudioManager audioManager;

    private int[] hiHats = {R.raw.hh1, R.raw.hh2, R.raw.hh3, R.raw.hh4, R.raw.hh5, R.raw.hh6, R.raw.hh7, R.raw.hh8, R.raw.hh9, R.raw.hh10};
    private int[] kicks = {R.raw.k1, R.raw.k2, R.raw.k3, R.raw.k4, R.raw.k5, R.raw.k6, R.raw.k7, R.raw.k8, R.raw.k9, R.raw.k10};
    private int[] percussions = {R.raw.p1, R.raw.p2, R.raw.p3, R.raw.p4, R.raw.p5, R.raw.p6, R.raw.p7, R.raw.p8, R.raw.p9, R.raw.p10};
    private int[] snares = {R.raw.s1, R.raw.s2, R.raw.s3, R.raw.s4, R.raw.s5,R.raw.s6, R.raw.s7, R.raw.s8, R.raw.s9, R.raw.s10};
    private int[] toms = {R.raw.t1, R.raw.t2, R.raw.t3, R.raw.t4, R.raw.t5, R.raw.t6, R.raw.t7, R.raw.t8, R.raw.t9, R.raw.t10};
    //******************************************

    public BeatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_beat, container, false);
        beatsBar = (SeekBar)view.findViewById(R.id.loopseekBar);

        /*String outputFile = pathMusic+"09. Brave.mp3";//Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        byte[] soundBytes = null;
        byte[] soundBytes1 = null;

        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(Uri.fromFile(new File(outputFile)));
            InputStream inputStream1 = getActivity().getContentResolver().openInputStream(Uri.fromFile(new File(pathMusic+"07. Wading.mp3")));

            soundBytes = new byte[inputStream.available()];
            soundBytes1 = new byte[inputStream1.available()];
            soundBytes = toByteArray(inputStream);
            soundBytes1 = toByteArray(inputStream1);

            //Toast.makeText(getContext(), "Recordin Finished"+ " " + soundBytes, Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            e.printStackTrace();
        }

        byte[] newSound = null;
        assert soundBytes != null;
        assert soundBytes1 != null;
        if(soundBytes.length < soundBytes1.length){
            newSound = new byte[soundBytes.length];
            for(int i = 0; i < soundBytes.length; i++){
                final byte b = (byte)(soundBytes[i]+soundBytes1[i]);
                newSound[i] = b;
            }
        }
        else{
            newSound = new byte[soundBytes1.length];
            for(int i = 0; i < soundBytes1.length; i++){
                final byte b = (byte)(soundBytes[i]+soundBytes1[i]);
                newSound[i] = b;
            }
        }

        //convertBytesToFile(newSound);
        //playMp3(soundBytes);*/

        // Record to the external cache directory for visibility
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        maxVolume  = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,maxVolume, 0);


        createMediaPlayer(0,0); //Loads the sound to the media player
        TempoSeekbarMethod(); //Controls the Thread speed TEMPO
        setupThreads(); //Creates a new thread

        mMusic = (Button)view.findViewById(R.id.music_b);
        mMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBoolean){
                    mMusic.setTextColor(Color.RED);
                    if(music!=null){
                        music.start();
                    }
                }
                else{
                    mMusic.setTextColor(color);
                    if(music!=null){
                        music.pause();
                    }
                }
                mBoolean = !mBoolean;
            }
        });

        mVoice = (Button)view.findViewById(R.id.mic_b);
        mVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vBoolean){
                    mVoice.setTextColor(Color.RED);
                    if(voice!=null){
                        voice.start();
                    }
                }
                else{
                    mVoice.setTextColor(color);
                    if(voice!=null){
                        voice.pause();
                    }
                }
                vBoolean = !vBoolean;
            }
        });

        setupThread();
        VolumeButtonMethod();
        volumeSeekBarMethod();
        playMethod(view);
        micMethod(view);
        musicButtonEventListener(view);
        saveButtonCLickEvent();

        sound1Button = (Button) view.findViewById(R.id.button1);
        selectBeatMethod1(sound1Button);
        sound3Button = (Button) view.findViewById(R.id.button3);
        selectBeatMethod2(sound3Button);
        sound2Button = (Button) view.findViewById(R.id.button2);
        selectBeatMethod3(sound2Button);
        sound4Button = (Button) view.findViewById(R.id.button4);
        selectBeatMethod4(sound4Button);

        //Load beats to the toggle buttons
        loadBeats(view);
        return view;
    }

    public byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = 0;
        byte[] buffer = new byte[1024];
        while (read != -1) {
            read = in.read(buffer);
            if (read != -1)
                out.write(buffer,0,read);
        }
        out.close();
        return out.toByteArray();
    }

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private void playMp3(byte[] mp3SoundByteArray) {
        try {
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile("kurchina", "mp3", getActivity().getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();

            // resetting mediaplayer instance to evade problems
            mediaPlayer.reset();

            // In case you run into issues with threading consider new instance like:
            // MediaPlayer mediaPlayer = new MediaPlayer();

            // Tried passing path directly, but kept getting
            // "Prepare failed.: status=0x1"
            // so using file descriptor instead
            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
    }

    private void convertBytesToFile(byte[] bytearray) {
        try {

            File outputFile = File.createTempFile("file", ".mp3", getActivity().getCacheDir());
            outputFile.deleteOnExit();
            File file = new File(pathMusic+"song.mp3");
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            fileoutputstream.write(bytearray);
            fileoutputstream.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveButtonCLickEvent() {
        Button saveButton = (Button) view.findViewById(R.id.savebutton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay(true);
            }
        });
    }

    private void setupThread() {
        volumeButton = (ImageButton) view.findViewById(R.id.speakerimageButton);
        volume = (VerticalSeekBar) view.findViewById(R.id.volumeseekBar);
    }

    private void setupThreads() {
        runnable = new CountDownRunner();
        myThread= new Thread(runnable);
    }

    public void createMediaPlayer(int i , int id) {
        sound1 = MediaPlayer.create(view.getContext(), beats[0]);
        sound3 = MediaPlayer.create(view.getContext(), beats[1]);
        sound2 = MediaPlayer.create(view.getContext(), beats[2]);
        sound4 = MediaPlayer.create(view.getContext(), beats[3]);
        loadBeats(view);
    }

    private void TempoSeekbarMethod() {
        tempo = (VerticalSeekBar)view.findViewById(R.id.temposeekBar);
        tempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bpm = 522 + (progress)*23;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void musicButtonEventListener(final View view) {
        musicButton = (ImageButton)view.findViewById(R.id.musicimageButton);
        musicButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                fileFormat = mp3Format;
                path = pathMusic;
                musicState = true;
                showPopup(false);
                setButton(mMusic);
                return false;
            }
        });
        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicBoolean){
                    musicButton.setColorFilter(Color.RED);
                }
                else{
                    musicButton.setColorFilter(color);
                }
                musicBoolean = !musicBoolean;
            }
        });
    }

    private void selectBeatMethod1(final Button button) {
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                buttonColumn = 0;
                setButton(button);
                showPopup(true);
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound1Boolean){
                    button.setTextColor(Color.RED);
                }
                else {
                    button.setTextColor(color);
                }
                //onPlay(sound1Boolean);
                sound1Boolean = !sound1Boolean;
            }
        });
    }

    private void selectBeatMethod2(final Button button) {
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                buttonColumn = 1;
                setButton(button);
                showPopup(true);
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound3Boolean){
                    button.setTextColor(Color.RED);
                }
                else {
                    button.setTextColor(color);
                }
                sound3Boolean = !sound3Boolean;
            }
        });
    }

    private void selectBeatMethod3(final Button button) {
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                buttonColumn = 2;
                setButton(button);
                showPopup(true);
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound2Boolean){
                    button.setTextColor(Color.RED);
                }
                else {
                    button.setTextColor(color);
                }
                //onPlay(sound2Boolean);
                sound2Boolean = !sound2Boolean;
            }
        });
    }

    private void selectBeatMethod4(final Button button) {
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                buttonColumn = 3;
                setButton(button);
                showPopup(true);
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound4Boolean){
                    button.setTextColor(Color.RED);
                }
                else {
                    button.setTextColor(color);
                }
                //onPlay(sound4Boolean);
                sound4Boolean = !sound4Boolean;
            }
        });
    }

    private void setButton(Button button){
        sButton = button;
    }

    private Button getButton(){
        return sButton;
    }

    private void VolumeButtonMethod() {
        volumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(volume.getProgress() == 0){
                    volume.setProgress(volume.getMax());
                }
                else {
                    volume.setProgress(0);
                }
            }
        });
    }

    private void volumeSeekBarMethod() {
        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0){
                    volumeButton.setImageResource(R.drawable.mute);
                }
                else{
                    volumeButton.setImageResource(R.drawable.speaker);
                }

                float vol = (float) (1 - (Math.log(MAX_VOLUME - progress*10) / Math.log(MAX_VOLUME)));
                if(!sound1Boolean){
                    sound1.setVolume(vol, vol);
                }
                if(!sound3Boolean){
                    sound3.setVolume(vol, vol);
                }
                if(!sound2Boolean){
                    sound2.setVolume(vol, vol);
                }
                if(!sound4Boolean){
                    sound4.setVolume(vol, vol);
                }
                if(!musicBoolean){
                    music.setVolume(vol, vol);
                }
                if(!vBoolean){
                    voice.setVolume(vol, vol);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void loadBeats(View view) {
        for (int i = 0; i < sound1Toggle.length; i++) {
            k = (ToggleButton) view.findViewById(sound1Toggle[i]); //Find toggle button ID
            setBeat(k, sound1, sound1Inputs, i); //Load beat
        }
        for (int i = 0; i < sound2Toggle.length; i++) {
            c = (ToggleButton) view.findViewById(sound2Toggle[i]);
            setBeat(c, sound2, sound2Inputs, i);
        }
        for (int i = 0; i < sound3Toggle.length; i++) {
            h = (ToggleButton) view.findViewById(sound3Toggle[i]);
            setBeat(h, sound3, sound3Inputs, i);
        }
        for (int i = 0; i < sound4Toggle.length; i++) {
            s = (ToggleButton) view.findViewById(sound4Toggle[i]);
            setBeat(s, sound4, sound4Inputs, i);
        }
    }

    private void l1Beats(MediaPlayer mPlayer) {
        for (int i = 0; i < sound1Toggle.length; i++) {
            k = (ToggleButton) view.findViewById(sound1Toggle[i]); //Find toggle button ID
            sound1 = mPlayer;
            setBeat(k, sound1, sound1Inputs, i); //Load beat
        }
    }

    private void l2Beats(MediaPlayer mPlayer) {
        for (int i = 0; i < sound2Toggle.length; i++) {
            c = (ToggleButton) view.findViewById(sound2Toggle[i]); //Find toggle button ID
            sound2 = mPlayer;
            setBeat(c, sound2, sound2Inputs, i); //Load beat
        }
    }

    private void l3Beats(MediaPlayer mPlayer) {
        for (int i = 0; i < sound3Toggle.length; i++) {
            h = (ToggleButton) view.findViewById(sound3Toggle[i]); //Find toggle button ID
            sound3 = mPlayer;
            setBeat(h, sound3, sound3Inputs, i); //Load beat
        }
    }

    private void l4Beats(MediaPlayer mPlayer) {
        for (int i = 0; i < sound4Toggle.length; i++) {
            s = (ToggleButton) view.findViewById(sound4Toggle[i]); //Find toggle button ID
            sound4 = mPlayer;
            setBeat(s, sound4, sound4Inputs, i); //Load beat
        }
    }

    private void showPopup(boolean instrumentB) {
        try {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.fragment_music_display, (ViewGroup) getActivity().findViewById(R.id.listbeats));
            pw = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            pw.showAtLocation(layout, Gravity.TOP, 0, 0);

            if(instrumentB) {
                songs = getSongsBeat();
                filteredSongs = new ArrayList<Song>();
                filteredSongs.addAll(songs);
            }
            else{
                soundList = getPlayList(path);
                songs = getSongs();
                filteredSongs = new ArrayList<Song>();
                filteredSongs.addAll(songs);
            }
            instrument = instrumentB;

            searchBox = (EditText) layout.findViewById(R.id.search_box);
            RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.item_list);
            mAdapter = new SimpleItemRecyclerViewAdapter(filteredSongs);
            recyclerView.setAdapter(mAdapter);

            searchBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mAdapter.getFilter().filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void micMethod(final View view) {
        micButton = (ImageButton)view.findViewById(R.id.micimageButton);
        micButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                fileFormat = audioFormat;
                path = pathVoice;
                musicState = false;
                showPopup(false);
                setButton(mVoice);
                return false;
            }
        });
        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(micBoolean){
                    micButton.setColorFilter(Color.RED);
                }
                else{
                    micButton.setColorFilter(color);
                }
                onRecord(micBoolean);
                micBoolean = !micBoolean;
            }
        });
    }

    private void playMethod(View view) {
        playButton = (ImageButton) view.findViewById(R.id.playimageButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playBoolean){
                    playButton.setImageResource(R.drawable.pause);
                    if(!myThread.isAlive()) {
                        threadRunning = true;
                        try {
                            myThread.start();
                        }
                        catch (IllegalThreadStateException e){
                            myThread= new Thread(runnable);
                            myThread.start();
                        }
                    }
                }
                else {
                    playButton.setImageResource(R.drawable.play);
                    if(myThread!=null){
                        threadRunning = false;
                    }
                }
                playBoolean = !playBoolean;
            }
        });
    }

    //Toggle button click Event
    private void setBeat(final ToggleButton beatButton, final MediaPlayer beat, final int[] beats, final int i) {
        beatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(beatButton.isChecked()){
                    beats[i] = 1;
                }
                else {
                    beats[i] = 0;
                }
                beat.start();
            }
        });
    }

    public void forwardSong(MediaPlayer mPlayer) {
        if (mPlayer != null) {
            int currentPosition = mPlayer.getCurrentPosition();
            if (currentPosition + seekForwardTime <= mPlayer.getDuration()) {
                mPlayer.seekTo(currentPosition + seekForwardTime);
            } else {
                mPlayer.seekTo(mPlayer.getDuration());
            }
        }
    }

    public void rewindSong(MediaPlayer mPlayer) {
        if (mPlayer != null) {
            int currentPosition = mPlayer.getCurrentPosition();
            if (currentPosition - seekBackwardTime >= 0) {
                mPlayer.seekTo(currentPosition - seekBackwardTime);
            } else {
                mPlayer.seekTo(0);
            }
        }
    }

    public void doWork() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if(sound1Inputs[seekbarInt] == 1){
                        sound1.start();
                    }
                    if(sound3Inputs[seekbarInt] == 1){
                        sound3.start();
                    }
                    if(sound2Inputs[seekbarInt] == 1){
                        sound2.start();
                    }
                    if(sound4Inputs[seekbarInt] == 1){
                        sound4.start();
                    }

                    beatsBar.setProgress(seekbarInt);
                    if(seekbarInt < beatsBar.getMax()){
                        seekbarInt++;
                    }
                    else{
                        seekbarInt = 0;
                    }
                } catch (Exception ignored) {}
            }
        });
    }

    private class CountDownRunner implements Runnable{
        public void run() {
            while(threadRunning){//!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(bpm); //522 - 750 BPM, Increment by 23
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception ignored){}
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) getActivity().finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        Date currentTime = Calendar.getInstance().getTime();
        String audioName = currentTime.getDate()+""+currentTime.getMonth()+""+currentTime.getYear()+""+currentTime.getHours()+""+currentTime.getMinutes()+""+currentTime.getSeconds();
        audioName = mFileName+"/Inethi"+audioName+".MPEG_4";
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(audioName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        Toast.makeText(getContext(),"Recording",Toast.LENGTH_LONG).show();
        mRecorder.start();
    }

    private void stopRecording() {
        Toast.makeText(getContext(),"Saving",Toast.LENGTH_LONG).show();
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    //********************************************
    class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> implements Filterable {
        private List<Song> mValues;
        private CustomFilter mFilter;
        private Boolean playing = true;
        private int previousSong = -1;
        private ImageButton previousB;
        private ViewHolder vHolder;

        SimpleItemRecyclerViewAdapter(List<Song> items) {
            mValues = items;
            mFilter = new CustomFilter(SimpleItemRecyclerViewAdapter.this);
            runnableUP = new CountDownRunnerUP();
            myThreadUP = new Thread(runnableUP);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sound_view_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            holder.mName.setText(String.valueOf(mValues.get(position).getArtistName()));
            holder.mDuration.setText(String.valueOf(mValues.get(position).getDuration()));
            holder.mSize.setText(String.valueOf(mValues.get(position).getSize()));
            holder.mShare.setImageResource(R.drawable.select);
            if(mValues.get(position).getCover()!=null){holder.mImage.setImageBitmap(mValues.get(position).getCover());}
            else{holder.mImage.setImageResource(R.drawable.musicicon);}

            holder.mShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),mValues.get(position).getArtistName()+" selected",Toast.LENGTH_LONG).show();
                    if(instrument) {
                        switch (buttonColumn) {
                            case 0:
                                l1Beats(holder.mItem.getSong());
                                break;
                            case 1:
                                l2Beats(holder.mItem.getSong());
                                break;
                            case 2:
                                l3Beats(holder.mItem.getSong());
                                break;
                            case 3:
                                l4Beats(holder.mItem.getSong());
                                break;
                        }
                    }
                    else{
                        if(musicState) {
                            music = holder.mItem.getSong();
                        }
                        else{
                            voice = holder.mItem.getSong();
                        }
                    }
                    getButton().setText(mValues.get(position).getArtistName());
                    pw.dismiss();
                }
            });

            holder.mPlay.setImageResource(R.drawable.play);
            final boolean check = song!=null && song.isPlaying() && !song.equals(holder.mItem.getSong());
            if(check){
                holder.mPlay.setImageResource(R.drawable.play);
            }
            if(previousSong == position){
                holder.mPlay.setImageResource(R.drawable.pause);
            }

            holder.mPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if((song!=null) && (song.isPlaying()) && (holder.getAdapterPosition() != previousSong)){
                        song.pause();
                        getPreviousB().setImageResource(R.drawable.play);
                        setPreviousB(null);
                        vHolder.mDuration.setText(getDuration(previousSong));
                        playing = !playing;
                        if(myThreadUP!=null){
                            threadRunningUP = false;
                        }
                    }

                    if(playing){
                        holder.mPlay.setImageResource(R.drawable.pause);
                        song = holder.mItem.getSong();
                        setPreviousB(holder.mPlay);
                        previousSong = position;
                        song.start();
                        vHolder = holder;
                        counter = 0;
                        if(!state) {
                            myThreadUP.start();
                            state = true;
                        }
                        threadRunningUP = true;
                    }
                    else{
                        holder.mPlay.setImageResource(R.drawable.play);
                        song.pause();
                        if(myThreadUP!=null){
                            threadRunningUP = false;
                        }
                    }
                    playing = !playing;
                }
            });
        }

        private void setPreviousB(ImageButton button){
            previousB = button;
        }
        private ImageButton getPreviousB(){
            return previousB;
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        @Override
        public Filter getFilter() {
            return mFilter;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            final TextView mName;
            final TextView mDuration;
            final TextView mSize;
            final ImageView mImage;
            final ImageButton mPlay;
            final ImageButton mShare;
            Song mItem;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mName = (TextView) view.findViewById(R.id.artistNametextView);
                mDuration = (TextView) view.findViewById(R.id.durationtextView);
                mSize = (TextView) view.findViewById(R.id.sizetextView);
                mImage = (ImageView) view.findViewById(R.id.songCoverimageView);
                mPlay = (ImageButton) view.findViewById(R.id.playimageButton);
                mShare = (ImageButton) view.findViewById(R.id.downloadimageButton);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mName.getText() + "'";
            }
        }

        class CustomFilter extends Filter {
            private SimpleItemRecyclerViewAdapter mAdapter;

            private CustomFilter(SimpleItemRecyclerViewAdapter mAdapter) {
                super();
                this.mAdapter = mAdapter;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                filteredSongs.clear();
                final FilterResults results = new FilterResults();
                if (constraint.length() == 0) {
                    filteredSongs.addAll(songs);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    for (final Song mSong : songs) {
                        if (mSong.getSongName().toLowerCase().contains(filterPattern) || mSong.getArtistName().toLowerCase().contains(filterPattern)) {
                            filteredSongs.add(mSong);
                        }
                    }
                }
                System.out.println("Count Number " + filteredSongs.size());
                results.values = filteredSongs;
                results.count = filteredSongs.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                System.out.println("Count Number 2 " + ((List<Song>) results.values).size());
                this.mAdapter.notifyDataSetChanged();
            }
        }

        private void doWorkUP() {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    String d;
                    int min_to_sec = song.getDuration()/1000 - counter;
                    if(min_to_sec >= 0) {
                        int m = min_to_sec / 60;
                        int s = min_to_sec - m * 60;
                        if (s < 10) {
                            d = m + ":0" + s;
                        } else {
                            d = m + ":" + s;
                        }

                        vHolder.mDuration.setText(d);
                        counter++;
                    }
                    else{
                        threadRunningUP = false;
                        counter = 0;
                        vHolder.mPlay.setImageResource(R.drawable.play);
                        if(instrument) {
                            vHolder.mDuration.setText(getDurationBeat(previousSong));
                        }else{
                            vHolder.mDuration.setText(getDuration(previousSong));
                        }
                    }
                }
            });
        }

        class CountDownRunnerUP implements Runnable{
            public void run() {
                while(threadRunningUP){
                    try {
                        doWorkUP();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }catch(Exception ignored){}
                }
            }
        }
    }

    public List<Song> getSongsBeat() {
        List<Song> songs = new ArrayList<Song>();
        for (int i = 0; i < 50; i++) {
            Song song = new Song();
            song.setSongName(getFileNameBeat(i));
            song.setSong(getSongBeat(i));
            song.setArtistName(getFileNameBeat(i));
            song.setCover(null);
            song.setDuration(getDurationBeat(i));
            song.setSize("");
            songs.add(song);
        }
        return songs;
    }

    public List<Song> getSongs() {
        List<Song> songs = new ArrayList<Song>();
        for (int i = 0; i < soundList.size(); i++) {
            Song song = new Song();
            song.setSongName(getFileName(i));
            song.setSong(getSong(i));
            song.setArtistName(getFileName(i));
            song.setCover(getCoverArt(i));
            song.setDuration(getDuration(i));
            song.setSize(getFileSize(i));
            songs.add(song);
        }
        return songs;
    }

    public ArrayList<HashMap<String, String>> getPlayList(String index) {
        ArrayList<HashMap<String, String>> fileList = new ArrayList<>();
        try {
            File rootFolder = new File(index);
            File[] files = rootFolder.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    if (getPlayList(file.getAbsolutePath()) != null) {
                        fileList.addAll(getPlayList(file.getAbsolutePath()));
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(fileFormat)) {
                    HashMap<String, String> song = new HashMap<>();
                    song.put(filePath, file.getAbsolutePath());
                    song.put(fileName, file.getName());
                    fileList.add(song);
                }
            }
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }

    private String getFileNameBeat(int nameIndex){
        String fileName = "";
        switch (nameIndex){
            case 0:
                fileName = "Hi-hat 1";
                break;
            case 1:
                fileName = "Hi-hat 2";
                break;
            case 2:
                fileName = "Hi-hat 3";
                break;
            case 3:
                fileName = "Hi-hat 4";
                break;
            case 4:
                fileName = "Hi-hat 5";
                break;
            case 5:
                fileName = "Hi-hat 6";
                break;
            case 6:
                fileName = "Hi-hat 7";
                break;
            case 7:
                fileName = "Hi-hat 8";
                break;
            case 8:
                fileName = "Hi-hat 9";
                break;
            case 9:
                fileName = "Hi-hat 10";
                break;
            case 10:
                fileName = "Kick 1";
                break;
            case 11:
                fileName = "Kick 2";
                break;
            case 12:
                fileName = "Kick 3";
                break;
            case 13:
                fileName = "Kick 4";
                break;
            case 14:
                fileName = "Kick 5";
                break;
            case 15:
                fileName = "Kick 6";
                break;
            case 16:
                fileName = "Kick 7";
                break;
            case 17:
                fileName = "Kick 8";
                break;
            case 18:
                fileName = "Kick 9";
                break;
            case 19:
                fileName = "Kick 10";
                break;
            case 20:
                fileName = "Percussion 1";
                break;
            case 21:
                fileName = "Percussion 2";
                break;
            case 22:
                fileName = "Percussion 3";
                break;
            case 23:
                fileName = "Percussion 4";
                break;
            case 24:
                fileName = "Percussion 5";
                break;
            case 25:
                fileName = "Percussion 6";
                break;
            case 26:
                fileName = "Percussion 7";
                break;
            case 27:
                fileName = "Percussion 8";
                break;
            case 28:
                fileName = "Percussion 9";
                break;
            case 29:
                fileName = "Percussion 10";
                break;
            case 30:
                fileName = "Snare 1";
                break;
            case 31:
                fileName = "Snare 2";
                break;
            case 32:
                fileName = "Snare 3";
                break;
            case 33:
                fileName = "Snare 4";
                break;
            case 34:
                fileName = "Snare 5";
                break;
            case 35:
                fileName = "Snare 6";
                break;
            case 36:
                fileName = "Snare 7";
                break;
            case 37:
                fileName = "Snare 8";
                break;
            case 38:
                fileName = "Snare 9";
                break;
            case 39:
                fileName = "Snare 10";
                break;
            case 40:
                fileName = "Toms 1";
                break;
            case 41:
                fileName = "Toms 2";
                break;
            case 42:
                fileName = "Toms 3";
                break;
            case 43:
                fileName = "Toms 4";
                break;
            case 44:
                fileName = "Toms 5";
                break;
            case 45:
                fileName = "Toms 6";
                break;
            case 46:
                fileName = "Toms 7";
                break;
            case 47:
                fileName = "Toms 8";
                break;
            case 48:
                fileName = "Toms 9";
                break;
            case 49:
                fileName = "Toms 10";
                break;
        }
        return fileName;
    }

    public String getFileName(int index) {
        return soundList.get(index).get(fileName).split(fileFormat)[0];
    }

    private MediaPlayer getSongBeat(int pathIndex){
        if((song!=null) &&(song.isPlaying())){
            song.stop();
        }
        if(pathIndex >= 0 && pathIndex < 10){
            song = MediaPlayer.create(getContext(),hiHats[pathIndex]);
        }
        if(pathIndex >= 10 && pathIndex < 20){
            pathIndex -= 10;
            song = MediaPlayer.create(getContext(),kicks[pathIndex]);
        }
        if(pathIndex >= 20 && pathIndex < 30){
            pathIndex -= 20;
            song = MediaPlayer.create(getContext(),percussions[pathIndex]);
        }
        if(pathIndex >= 30 && pathIndex < 40){
            pathIndex -= 30;
            song = MediaPlayer.create(getContext(),snares[pathIndex]);
        }
        if(pathIndex >= 40 && pathIndex < 50){
            pathIndex -= 40;
            song = MediaPlayer.create(getContext(),toms[pathIndex]);
        }
        return song;
    }
    public MediaPlayer getSong(int index) {
        String musicPath = soundList.get(index).get(filePath);
        return MediaPlayer.create(getContext(), Uri.parse(musicPath));
    }

    public Bitmap getCoverArt(int index) {
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String path = soundList.get(index).get(filePath);
        Bitmap bitmap = null;
        if (path != null) {
            mmr.setDataSource(path);
            byte[] data = mmr.getEmbeddedPicture();

            if (data != null) {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            }
        }
        return bitmap;
    }

    public String getDurationBeat(int duration) {
        MediaPlayer mediaPlayer = null;
        if(duration >= 0 && duration < 10){
            mediaPlayer = MediaPlayer.create(getContext(),hiHats[duration]);
        }
        if(duration >= 10 && duration < 20){
            duration -= 10;
            mediaPlayer = MediaPlayer.create(getContext(),kicks[duration]);
        }
        if(duration >= 20 && duration < 30){
            duration -= 20;
            mediaPlayer = MediaPlayer.create(getContext(),percussions[duration]);
        }
        if(duration >= 30 && duration < 40){
            duration -= 30;
            mediaPlayer = MediaPlayer.create(getContext(),snares[duration]);
        }
        if(duration >= 40 && duration < 50){
            duration -= 40;
            mediaPlayer = MediaPlayer.create(getContext(),toms[duration]);
        }

        int min_to_sec = mediaPlayer.getDuration()/1000;
        int m = min_to_sec/60;
        int s = min_to_sec-m*60;
        if(s<10){
            return m+":0"+s;
        }
        return m+":"+s;
    }

    public String getDuration(int duration) {
        String soundPath = soundList.get(duration).get(filePath);
        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(soundPath));
        int min_to_sec = mediaPlayer.getDuration()/1000;
        int m = min_to_sec/60;
        int s = min_to_sec-m*60;
        if(s<10){
            return m+":0"+s;
        }
        return m+":"+s;
    }

    public String getFileSize(int index) {
        File file = new File(soundList.get(index).get(filePath));
        float fileSize = (float) (file.length() / 1000000.0);
        return Math.round(fileSize * 100.0) / 100.0 + "MB";
    }

    public String getFileSizeBeat(int index) {
        File file = new File(soundList.get(index).get(filePath));
        float fileSize = (float) (file.length() / 1000000.0);
        return Math.round(fileSize * 100.0) / 100.0 + "MB";
    }
    //********************************************
}
