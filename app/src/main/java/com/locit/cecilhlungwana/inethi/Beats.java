package com.locit.cecilhlungwana.inethi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cecilhlungwana on 2017/09/16.
 */

public class Beats {
    private Context context;
    private MediaPlayer beat;
    private ArrayList<HashMap<String,String>> beatsList;
    private final String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Inethi/Beats/Load/";
    private Boolean isPaused = false;
    private static int position = -1;
    private final String fileName = "file_name";
    private final String filePath = "file_path";
    private final String fileFormat = ".mp3";
    private final String sendType = "audio/mpeg";
    private boolean playButtonClicked = true;
    private float fileSize;

    private int[] hiHats = {R.raw.hh1, R.raw.hh2, R.raw.hh3, R.raw.hh4, R.raw.hh5, R.raw.hh6, R.raw.hh7, R.raw.hh8, R.raw.hh9, R.raw.hh10};
    private int[] kicks = {R.raw.k1, R.raw.k2, R.raw.k3, R.raw.k4, R.raw.k5, R.raw.k6, R.raw.k7, R.raw.k8, R.raw.k9, R.raw.k10};
    private int[] percussions = {R.raw.p1, R.raw.p2, R.raw.p3, R.raw.p4, R.raw.p5, R.raw.p6, R.raw.p7, R.raw.p8, R.raw.p9, R.raw.p10};
    private int[] snares = {R.raw.s1, R.raw.s2, R.raw.s3, R.raw.s4, R.raw.s5,R.raw.s6, R.raw.s7, R.raw.s8, R.raw.s9, R.raw.s10};
    private int[] toms = {R.raw.t1, R.raw.t2, R.raw.t3, R.raw.t4, R.raw.t5, R.raw.t6, R.raw.t7, R.raw.t8, R.raw.t9, R.raw.t10};

    Beats(Context context){
        this.context = context;
        beatsList = getPlayList(path);
    }

    private void playBeat(){
        if(!beat.isPlaying()){
            beat.start();
        }
    }

    private void pauseBeat(){
        if(beat.isPlaying()){
            beat.pause();
        }
    }

    private void stopBeat(){
        if(beat.isPlaying()){
            beat.stop();
        }
    }

    private boolean loadBeat(int pathIndex){
        if((beat!=null) &&(beat.isPlaying())){
            stopBeat();
        }
        if(pathIndex >= 0 && pathIndex < 10){
            beat = MediaPlayer.create(context,hiHats[pathIndex]);
        }
        if(pathIndex >= 10 && pathIndex < 20){
            pathIndex -= 10;
            beat = MediaPlayer.create(context,kicks[pathIndex]);
        }
        if(pathIndex >= 20 && pathIndex < 30){
            pathIndex -= 20;
            beat = MediaPlayer.create(context,percussions[pathIndex]);
        }
        if(pathIndex >= 30 && pathIndex < 40){
            pathIndex -= 30;
            beat = MediaPlayer.create(context,snares[pathIndex]);
        }
        if(pathIndex >= 40 && pathIndex < 50){
            pathIndex -= 40;
            beat = MediaPlayer.create(context,toms[pathIndex]);
        }
        setPosition(pathIndex);
        return true;
    }

    int getBeat(int beatAt){
        if(beatAt >= 0 && beatAt < 10){
            return hiHats[beatAt];
        }
        if(beatAt >= 10 && beatAt < 20){
            beatAt -= 10;
            return kicks[beatAt];
        }
        if(beatAt >= 20 && beatAt < 30){
            beatAt -= 20;
            return percussions[beatAt];
        }
        if(beatAt >= 30 && beatAt < 40){
            beatAt -= 30;
            return snares[beatAt];
        }
        if(beatAt >= 40 && beatAt < 50){
            beatAt -= 40;
            return toms[beatAt];
        }
        return -1;
    }

    int getSize(){
        return hiHats.length + kicks.length + percussions.length + snares.length + toms.length;
    }

    String getFileName(int nameIndex){
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

    Bitmap getCoverArt(int pathIndex){
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String path = beatsList.get(pathIndex).get(filePath);
        Bitmap bitmap = null;
        if (path!=null) {
            mmr.setDataSource(path);
            byte[] data = mmr.getEmbeddedPicture();

            if (data != null) {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            }
        }
        return bitmap;
    }

    private ArrayList<HashMap<String,String>> getPlayList(String rootPath) {
        ArrayList<HashMap<String,String>> fileList = new ArrayList<>();

        try {
            File rootFolder = new File(rootPath);
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

    Boolean getPaused() {
        return isPaused;
    }

    private void setPaused(Boolean paused) {
        isPaused = paused;
    }

    int getPosition() {
        return position;
    }

    private void setPosition(int position) {
        Beats.position = position;
    }

    void playButtonEventListener(final SongViewHolder holder){
        final ImageButton button = holder.getPlay_pause_ImageButton();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playButtonClicked) {
                    loadBeat(holder.getAdapterPosition());
                    playBeat();
                    setPosition(holder.getAdapterPosition());
                    button.setImageResource(R.drawable.pause);
                }
                else{
                    button.setImageResource(R.drawable.play);
                    pauseBeat();
                }
                setPaused(playButtonClicked);
                playButtonClicked = !playButtonClicked;
            }
        });
    }

    void shareButtonEventListener(final SongViewHolder holder){
        final ImageButton button = holder.getDusImageButton();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(beatsList.get(holder.getAdapterPosition()).get(filePath)));
                sendIntent.setType(sendType);
                holder.itemView.getContext().startActivity(Intent.createChooser(sendIntent, holder.itemView.getContext().getResources().getText(R.string.share)));
            }
        });
    }

    String getDuration(int duration){
        MediaPlayer mediaPlayer = null;
        if(duration >= 0 && duration < 10){
            mediaPlayer = MediaPlayer.create(context,hiHats[duration]);
        }
        if(duration >= 10 && duration < 20){
            duration -= 10;
            mediaPlayer = MediaPlayer.create(context,kicks[duration]);
        }
        if(duration >= 20 && duration < 30){
            duration -= 20;
            mediaPlayer = MediaPlayer.create(context,percussions[duration]);
        }
        if(duration >= 30 && duration < 40){
            duration -= 30;
            mediaPlayer = MediaPlayer.create(context,snares[duration]);
        }
        if(duration >= 40 && duration < 50){
            duration -= 40;
            mediaPlayer = MediaPlayer.create(context,toms[duration]);
        }

        int min_to_sec = mediaPlayer.getDuration()/1000;
        int m = min_to_sec/60;
        int s = min_to_sec-m*60;
        if(s<10){
            return m+":0"+s;
        }
        return m+":"+s;
    }

    String  getFileSize(int index) {
        File file = new File(beatsList.get(index).get(filePath));
        this.fileSize = (float) (file.length()/1000000.0);
        return Math.round(fileSize * 100.0) / 100.0+"MB";
    }
}
