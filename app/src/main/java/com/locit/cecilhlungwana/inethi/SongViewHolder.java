package com.locit.cecilhlungwana.inethi;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by cecilhlungwana on 2017/09/12.
 */

class SongViewHolder extends RecyclerView.ViewHolder{
    private ImageView songCover;
    private TextView songName;
    private TextView artistName;
    private TextView songSize;
    private TextView songDuration;
    private LinearLayout layout;
    private ImageButton dusImageButton;
    private ImageButton play_pause_ImageButton;
    private ImageButton uploadImageButton;
    private MediaPlayer mediaPlayer;

    //Load everyting in the constructor
    public SongViewHolder(View itemView) {
        super(itemView);

        songCover = (ImageView) itemView.findViewById(R.id.songCoverimageView);
        songName = (TextView) itemView.findViewById(R.id.songNametextView);
        artistName = (TextView) itemView.findViewById(R.id.artistNametextView);
        songDuration = (TextView) itemView.findViewById(R.id.durationtextView);
        songSize = (TextView) itemView.findViewById(R.id.sizetextView);
        layout = (LinearLayout) itemView.findViewById(R.id.songLayout);
        dusImageButton = (ImageButton) itemView.findViewById(R.id.downloadimageButton);
        play_pause_ImageButton = (ImageButton) itemView.findViewById(R.id.playimageButton);
        uploadImageButton = (ImageButton) itemView.findViewById(R.id.uploadimageButton);
    }

    public ImageView getSongCover() {
        return songCover;
    }

    public void setSongCover(ImageView songCover) {
        this.songCover = songCover;
    }

    public TextView getSongName() {
        return songName;
    }

    public void setSongName(TextView songName) {
        this.songName = songName;
    }

    public TextView getArtistName() {
        return artistName;
    }

    public void setArtistName(TextView artistName) {
        this.artistName = artistName;
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public void setLayout(LinearLayout layout) {
        this.layout = layout;
    }

    public ImageButton getDusImageButton() {
        return dusImageButton;
    }

    public void setDusImageButton(ImageButton dusImageButton) {
        this.dusImageButton = dusImageButton;
    }

    public ImageButton getPlay_pause_ImageButton() {
        return play_pause_ImageButton;
    }

    public void setPlay_pause_ImageButton(ImageButton play_pause_ImageButton) {
        this.play_pause_ImageButton = play_pause_ImageButton;
    }

    public ImageButton getUploadImageButton() {
        return uploadImageButton;
    }

    public void setUploadImageButton(ImageButton uploadImageButton) {
        this.uploadImageButton = uploadImageButton;
    }

    public TextView getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(TextView songDuration) {
        this.songDuration = songDuration;
    }

    public TextView getSongSize() {
        return songSize;
    }

    public void setSongSize(TextView songSize) {
        this.songSize = songSize;
    }
}
