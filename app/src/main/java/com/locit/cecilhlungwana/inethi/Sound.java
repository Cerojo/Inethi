package com.locit.cecilhlungwana.inethi;

import android.content.Context;
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
 * Created by cecilhlungwana on 2017/09/22.
 */

abstract class Sound implements SoundInterface{
    Context context;
    ArrayList<HashMap<String,String>> soundList;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Inethi/";
    private boolean playButtonClicked = true;
    private Boolean isPaused = false;
    private static int position = -1;
    private float fileSize;
    private final String fileName = "file_name";
    private final String filePath = "file_path";
    private String fileFormat;
    private MediaPlayer sound;

    @Override
    public void setup(){
        soundList = getPlayList(getPath());
    }

    public void setEndWith(String endWith){
        fileFormat = endWith;
    }

    public String getEndWith(){
        return fileFormat;
    }

    @Override
    public void playSound() {
        if(!sound.isPlaying()){
            sound.start();
        }
    }

    @Override
    public void pauseSound() {
        if(sound.isPlaying()){
            sound.pause();
        }
    }

    @Override
    public void stopSound() {
        if(sound.isPlaying()){
            sound.stop();
        }
    }

    @Override
    public boolean loadSound(int index) {
        String musicPath = soundList.get(index).get(filePath);
        if((musicPath!=null) && (getPosition() != index)) {
            if((sound!=null) &&(sound.isPlaying())){
                stopSound();
            }
            sound = MediaPlayer.create(context, Uri.parse(musicPath));
            setPosition(index);
            return true;
        }
        return false;
    }

    @Override
    public MediaPlayer getSound() {
        return sound;
    }

    @Override
    public void playButtonEventListener(final SongViewHolder holder) {
        final ImageButton button = holder.getPlay_pause_ImageButton();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playButtonClicked) {
                    loadSound(holder.getAdapterPosition());
                    playSound();
                    setPosition(holder.getAdapterPosition());
                    button.setImageResource(R.drawable.pause);
                }
                else{
                    button.setImageResource(R.drawable.play);
                    pauseSound();
                }
                setPaused(playButtonClicked);
                playButtonClicked = !playButtonClicked;
            }
        });
    }

    @Override
    public int getSize() {
        try {
            return soundList.size();
        }
        catch (NullPointerException e){
            return -1;
        }
    }

    @Override
    public String getFileName(int index) {
        return soundList.get(index).get(fileName);
    }

    @Override
    public Bitmap getCoverArt(int index) {
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String path = soundList.get(index).get(filePath);
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

    @Override
    public ArrayList<HashMap<String, String>> getPlayList(String index) {
        ArrayList<HashMap<String,String>> fileList = new ArrayList<>();
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
                } else if (file.getName().endsWith(getEndWith())) {
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

    @Override
    public Boolean getPaused() {
        return isPaused;
    }

    @Override
    public void setPaused(Boolean paused) {
        isPaused = paused;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(int index) {
        position = index;
    }

    @Override
    public String getDuration(int duration) {
        String soundPath = soundList.get(duration).get(filePath);
        MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.parse(soundPath));
        int min_to_sec = mediaPlayer.getDuration()/1000;
        int m = min_to_sec/60;
        int s = min_to_sec-m*60;
        if(s<10){
            return m+":0"+s;
        }
        return m+":"+s;
    }

    @Override
    public String getFileSize(int index) {
        File file = new File(soundList.get(index).get(filePath));
        this.fileSize = (float) (file.length()/1000000.0);
        return Math.round(fileSize * 100.0) / 100.0+"MB";
    }

    @Override
    public void setPath(String path){
        this.path += path;
    }

    @Override
    public String getPath(){
        return path;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
