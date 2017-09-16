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
    private final String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Inethi/Beats/Loaded/";
    private Boolean isPaused = false;
    private static int position = -1;
    private final String fileName = "file_name";
    private final String filePath = "file_path";
    private final String fileFormat = ".mp3";
    private final String sendType = "audio/mpeg";
    private boolean playButtonClicked = true;
    private float fileSize;

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
        String beatPath = beatsList.get(pathIndex).get(filePath);
        if((beatPath!=null) && (getPosition() != pathIndex)) {
            if((beat!=null) &&(beat.isPlaying())){
                stopBeat();
            }
            beat = MediaPlayer.create(context, Uri.parse(beatPath));
            setPosition(pathIndex);
            return true;
        }
        return false;
    }

    MediaPlayer getBeat(){
        return beat;
    }

    int getSize(){
        return beatsList.size();
    }

    String getArtistName(int nameIndex){
        return beatsList.get(nameIndex).get(fileName);
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
        String beatPath = beatsList.get(duration).get(filePath);
        MediaPlayer mediaPlayer = MediaPlayer.create(context,Uri.parse(beatPath));
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
