package com.locit.cecilhlungwana.inethi;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by cecilhlungwana on 2017/09/22.
 */

class Voice extends Sound {

    Voice(Context context){
        this.context = context;
        setup();
    }

    @Override
    public void setup(){
        setPath("Beats/Voice/");
        setEndWith(".MPEG_4");
        soundList = getPlayList(getPath());
    }

    @Override
    public void shareButtonEventListener(SongViewHolder holder) {

    }
}
