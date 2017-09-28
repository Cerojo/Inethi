package layout;
/*
The beats fragment class handles the view
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
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
import com.locit.cecilhlungwana.inethi.VisualizerView;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeatFragment extends Fragment {
    private MediaPlayer song;
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

    public BeatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_beat, container, false);
        beatsBar = (SeekBar)view.findViewById(R.id.loopseekBar);

        // Record to the external cache directory for visibility
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        createMediaPlayer(0,0); //Loads the sound to the media player
        TempoSeekbarMethod(); //Controls the Thread speed TEMPO
        setupThreads(); //Creates a new thread

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

    private void musicButtonEventListener(View view) {
        musicButton = (ImageButton)view.findViewById(R.id.musicimageButton);
        musicButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopup(4);
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

    private void selectBeatMethod1(final Button button) {
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopup(4);
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
                showPopup(5);
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
                showPopup(6);
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
                showPopup(7);
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

    private void showPopup(int select) {
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
            songAdapter = new SongAdapter(getContext(), select, layout, view);
            songAdapter.setActivity(getActivity());
            defaultRecyclerView.setAdapter(songAdapter);

            //Change recycler view orientation
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            defaultRecyclerView.setLayoutManager(layoutManager);
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
        micButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopup(8);
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
                    float vol = (float) (1 - (Math.log(MAX_VOLUME - volume.getProgress()*10) / Math.log(MAX_VOLUME)));
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
}
