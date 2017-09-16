package layout;
/*
The beats fragment class handles the view
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.locit.cecilhlungwana.inethi.R;
import com.locit.cecilhlungwana.inethi.SongAdapter;
import com.locit.cecilhlungwana.inethi.VerticalSeekBar;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeatFragment extends Fragment {
    private MediaPlayer song;
    private int seekbarInt = 0;
    private int[] kickInputs = {0,0,0,0};//,0};
    private int[] hihatInputs = {0,0,0,0};//,0};
    private int[] snareInputs = {0,0,0,0};//,0};
    private int[] clapInputs = {0,0,0,0,};//0};

    private MediaPlayer kickSound;
    private MediaPlayer hihatSound;
    private MediaPlayer clapSound;
    private MediaPlayer snareSound;


    private Boolean kickBoolean = true;
    private Boolean hihatBoolean = true;
    private Boolean clapBoolean = true;
    private Boolean snareBoolean = true;

    private Button kickButton;
    private Button hihatButton;
    private Button snareButton;
    private Button clapButton;

    private Button saveButton;

    Button Close;

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

    private Boolean volumeState = true;

    private ToggleButton k;
    private ToggleButton h;
    private ToggleButton s;
    private ToggleButton c;

    private SeekBar beatsBar;
    private VerticalSeekBar volume;

    //Toggle button IDs
    private int kick[] = {R.id.k1toggleButton, R.id.k2toggleButton, R.id.k3toggleButton, R.id.k4toggleButton};//, R.id.k5toggleButton};
    private int hihat[] = {R.id.h1toggleButton, R.id.h2toggleButton, R.id.h3toggleButton, R.id.h4toggleButton};//, R.id.h5toggleButton};
    private int clap[] = {R.id.c1toggleButton, R.id.c2toggleButton, R.id.c3toggleButton, R.id.c4toggleButton};//, R.id.c5toggleButton};
    private int snare[] = {R.id.s1toggleButton, R.id.s2toggleButton, R.id.s3toggleButton, R.id.s4toggleButton};//, R.id.s5toggleButton};

    private int beats[] = {R.raw.kick, R.raw.hihat, R.raw.clap, R.raw.snare};

    private int seekForwardTime = 5 * 1000; // default 5 second
    private int seekBackwardTime = 5 * 1000; // default 5 second
    private Thread myThread;
    private boolean playMusic = true;
    private RecyclerView defaultRecyclerView;
    private RecyclerView createRecyclerView;
    private RecyclerView downloadedRecyclerView;
    private SongAdapter songAdapter;
    private boolean threadRunning = false;
    private Runnable runnable;

    private int color = Color.rgb(105,26,153);

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Inethi/Beats/Voice";
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    public BeatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beat, container, false);
        beatsBar = (SeekBar)view.findViewById(R.id.loopseekBar);

        // Record to the external cache directory for visibility
        mFileName += "/audiorecordtest.MPEG_4";
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        kickSound = MediaPlayer.create(view.getContext(), beats[0]);
        hihatSound = MediaPlayer.create(view.getContext(), beats[1]);
        clapSound = MediaPlayer.create(view.getContext(), beats[2]);
        snareSound = MediaPlayer.create(view.getContext(), beats[3]);

        runnable = new CountDownRunner();
        myThread= new Thread(runnable);

        volumeButton = (ImageButton) view.findViewById(R.id.speakerimageButton);
        volume = (VerticalSeekBar) view.findViewById(R.id.volumeseekBar);

        VolumeButtonMethod();
        volumeSeekBarMethod();

        playMethod(view);
        micMethod(view);

        musicButtonEventListener(view);

        kickButton = (Button) view.findViewById(R.id.kickbutton);
        selectBeatMethod(kickButton);
        hihatButton = (Button) view.findViewById(R.id.hihatbutton);
        selectBeatMethod(hihatButton);
        clapButton = (Button) view.findViewById(R.id.clapbutton);
        selectBeatMethod(clapButton);
        snareButton = (Button) view.findViewById(R.id.snarebutton);
        selectBeatMethod(snareButton);

        //Load beats to the toggle buttons
        loadBeats(view);
        return view;
    }

    private void musicButtonEventListener(View view) {
        musicButton = (ImageButton)view.findViewById(R.id.musicimageButton);
        musicButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopup();
                return false;
            }
        });
        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Bye World", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void selectBeatMethod(final Button button) {
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Hello World!",Toast.LENGTH_LONG).show();
                if(kickBoolean){
                    button.setTextColor(Color.RED);
                }
                else {
                    button.setTextColor(color);
                }
                onPlay(kickBoolean);
                kickBoolean = !kickBoolean;
            }
        });
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
        for (int i = 0; i < kick.length; i++) {
            k = (ToggleButton) view.findViewById(kick[i]); //Find toggle button ID
            setBeat(k, kickSound, kickInputs, i); //Load beat
        }
        for (int i = 0; i < hihat.length; i++) {
            h = (ToggleButton) view.findViewById(hihat[i]);
            setBeat(h, hihatSound, hihatInputs, i);
        }
        for (int i = 0; i < clap.length; i++) {
            c = (ToggleButton) view.findViewById(clap[i]);
            setBeat(c, clapSound, clapInputs, i);
        }
        for (int i = 0; i < snare.length; i++) {
            s = (ToggleButton) view.findViewById(snare[i]);
            setBeat(s, snareSound, snareInputs, i);
        }
    }

    private PopupWindow pw;
    private void showPopup() {
        try {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.list_beats, (ViewGroup) getActivity().findViewById(R.id.listbeats));
            pw = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            Close = (Button) layout.findViewById(R.id.close_popup);
            Close.setOnClickListener(cancel_button);

            //----------------------------------------------------------
            //Load the recycler view
            defaultRecyclerView = (RecyclerView) layout.findViewById(R.id.defaultRecyclerView);
            songAdapter = new SongAdapter(getContext(), 4, layout);
            defaultRecyclerView.setAdapter(songAdapter);

            //Change recycler view orientation
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            defaultRecyclerView.setLayoutManager(layoutManager);

            //----------------------------------------------------------
            //Load the recycler view
            defaultRecyclerView = (RecyclerView) layout.findViewById(R.id.createdRecyclerView);
            songAdapter = new SongAdapter(getContext(), 4, layout);
            defaultRecyclerView.setAdapter(songAdapter);

            //Change recycler view orientation
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            defaultRecyclerView.setLayoutManager(layoutManager);

            //----------------------------------------------------------
            //Load the recycler view
            defaultRecyclerView = (RecyclerView) layout.findViewById(R.id.DownloadedRecyclerView);
            songAdapter = new SongAdapter(getContext(), 4, layout);
            defaultRecyclerView.setAdapter(songAdapter);

            //Change recycler view orientation
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            defaultRecyclerView.setLayoutManager(layoutManager);
            //----------------------------------------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener cancel_button = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();
        }
    };

    private void micMethod(View view) {
        micButton = (ImageButton)view.findViewById(R.id.micimageButton);
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
                    if(kickInputs[seekbarInt] == 1){
                        kickSound.start();
                    }
                    if(hihatInputs[seekbarInt] == 1){
                        hihatSound.start();
                    }
                    if(clapInputs[seekbarInt] == 1){
                        clapSound.start();
                    }
                    if(snareInputs[seekbarInt] == 1){
                        snareSound.start();
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
                    Thread.sleep(500);
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
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
}
