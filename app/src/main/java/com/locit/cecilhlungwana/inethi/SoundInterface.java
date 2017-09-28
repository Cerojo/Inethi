package com.locit.cecilhlungwana.inethi;

import android.graphics.Bitmap;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cecilhlungwana on 2017/09/22.
 */

public interface SoundInterface {

    void setup();

    void playSound();

    void pauseSound();

    void stopSound();

    boolean loadSound(int index);

    MediaPlayer getSound();

    void setEndWith(String endWith);

    String getEndWith();

    int getSize();

    String getFileName(int index);

    Bitmap getCoverArt(int index);

    ArrayList<HashMap<String,String>> getPlayList(String index);

    Boolean getPaused();

    void setPaused(Boolean paused);

    int getPosition();

    void setPosition(int index);

    void playButtonEventListener(final SongViewHolder holder);

    void mButtonEventListener(final SongViewHolder holder);

    String getDuration(int duration);

    String  getFileSize(int index);

    void setPath(String path);

    String getPath();
}
