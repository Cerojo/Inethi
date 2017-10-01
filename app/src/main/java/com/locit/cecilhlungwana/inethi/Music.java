package com.locit.cecilhlungwana.inethi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by cecilhlungwana on 2017/09/11.
 */

class Music extends Sound {

    Music(Context context) {
        this.context = context;
        setup();
    }

    @Override
    public void setup() {
        runnable = new CountDownRunner();
        myThread = new Thread(runnable);
        setPath("Download/");
        setEndWith(".mp3");
        soundList = getPlayList(getPath());
    }

    @Override
    public void mButtonEventListener(final SongViewHolder holder) {
        final ImageButton button = holder.getDusImageButton();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(soundList.get(holder.getAdapterPosition()).get(filePath)));
                sendIntent.setType("audio/mpeg");
                holder.itemView.getContext().startActivity(Intent.createChooser(sendIntent, holder.itemView.getContext().getResources().getText(R.string.share)));
            }
        });
    }
}
    /*{
    private Context context;
    private MediaPlayer music;
    private ArrayList<HashMap<String,String>> songList;
    private final String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Inethi/Download/";
    private Boolean isPaused = false;
    private static int position = -1;
    private final String fileName = "file_name";
    private final String filePath = "file_path";
    private final String fileFormat = ".mp3";
    private final String sendType = "audio/mpeg";
    private boolean playButtonClicked = true;
    private float fileSize;

    Music(Context context){
        this.context = context;
        songList = getPlayList(path);
    }

    private void playMusic(){
        if(!music.isPlaying()){
            music.start();
        }
    }

    private void pauseMusic(){
        if(music.isPlaying()){
            music.pause();
        }
    }

    private void stopMusic(){
        if(music.isPlaying()){
            music.stop();
        }
    }

    private boolean loadMusic(int pathIndex){
        String musicPath = songList.get(pathIndex).get(filePath);
        if((musicPath!=null) && (getPosition() != pathIndex)) {
            if((music!=null) &&(music.isPlaying())){
                stopMusic();
            }
            music = MediaPlayer.create(context, Uri.parse(musicPath));
            setPosition(pathIndex);
            return true;
        }
        return false;
    }

    MediaPlayer getMusic(){
        return music;
    }

    int getSize(){
        try {
            return songList.size();
        }
        catch (NullPointerException e){
            return -1;
        }
    }

    String getArtistName(int nameIndex){
        return songList.get(nameIndex).get(fileName);
    }

    Bitmap getCoverArt(int pathIndex){
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String path = songList.get(pathIndex).get(filePath);
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
        Music.position = position;
    }

    void playButtonEventListener(final SongViewHolder holder){
        final ImageButton button = holder.getPlay_pause_ImageButton();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playButtonClicked) {
                    loadMusic(holder.getAdapterPosition());
                    playMusic();
                    setPosition(holder.getAdapterPosition());
                    button.setImageResource(R.drawable.pause);
                }
                else{
                    button.setImageResource(R.drawable.play);
                    pauseMusic();
                }
                setPaused(playButtonClicked);
                playButtonClicked = !playButtonClicked;
            }
        });
    }

    void mButtonEventListener(final SongViewHolder holder){
        final ImageButton button = holder.getDusImageButton();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(songList.get(holder.getAdapterPosition()).get(filePath)));
                sendIntent.setType(sendType);
                holder.itemView.getContext().startActivity(Intent.createChooser(sendIntent, holder.itemView.getContext().getResources().getText(R.string.share)));
            }
        });
    }

    String getDuration(int duration){
        String musicPath = songList.get(duration).get(filePath);
        MediaPlayer mediaPlayer = MediaPlayer.create(context,Uri.parse(musicPath));
        int min_to_sec = mediaPlayer.getDuration()/1000;
        int m = min_to_sec/60;
        int s = min_to_sec-m*60;
        if(s<10){
            return m+":0"+s;
        }
        return m+":"+s;
    }

    String  getFileSize(int index) {
        File file = new File(songList.get(index).get(filePath));
        this.fileSize = (float) (file.length()/1000000.0);
        return Math.round(fileSize * 100.0) / 100.0+"MB";
    }
}*/