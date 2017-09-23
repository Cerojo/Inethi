package com.locit.cecilhlungwana.inethi;
/*
Song Adapter Class
This is will be used to load the song information in the recycler view.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import layout.BeatFragment;

/**
 * Created by cecilhlungwana on 2017/09/03.
 */

public class SongAdapter extends RecyclerView.Adapter<SongViewHolder>{
    private final String shareString = "Search song to Share";
    private final LayoutInflater inflater;
    private SongViewHolder songViewHolder;
    private SearchView musicSearchView;
    private int classID;
    private Music music;
    private Beats beats;
    private Voice voice;
    private View parentView;

    //Constructor
    public SongAdapter(Context context, int classID, View view, View parentView){
        inflater = LayoutInflater.from(context);
        this.classID = classID;
        music = new Music(context);
        beats = new Beats(context);
        voice = new Voice(context);

        this.parentView = parentView;

        musicSearchView = (SearchView) view.findViewById(R.id.musicsearchView); //Set up the search bar
        musicSearchView.setIconified(false);
        musicSearchView.setQueryHint(shareString);
        musicSearchView.clearFocus();
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sound_view_layout, parent,false);
        songViewHolder = new SongViewHolder(view); //Load widgets id
        return songViewHolder;
    }

    /*
    This method handlers the recycling of reviews
     */
    @Override
    public void onBindViewHolder(final SongViewHolder holder, final int position) {
        /*Song song = songs.get(position); //Get information of song at current position*/
        Button button;
        switch (classID) {
            case 1: //Download
                break;
            case 2: //Upload
                break;
            case 3: //Music
                soundSetup(holder, music, position);
                break;
            case 4:
                button = (Button)parentView.findViewById(R.id.button1);
                beatSetup(holder, position, button);
                break;
            case 5:
                button = (Button)parentView.findViewById(R.id.button2);
                beatSetup(holder, position, button);
                break;
            case 6:
                button = (Button)parentView.findViewById(R.id.button3);
                beatSetup(holder, position, button);
                break;
            case 7:
                button = (Button)parentView.findViewById(R.id.button4);
                beatSetup(holder, position, button);
                break;
            case 8:
                holder.getDusImageButton().setImageResource(R.drawable.select);
                soundSetup(holder,voice,position);
                break;
        }
    }

    private void soundSetup(SongViewHolder holder, Sound sound, int position) {
        if(sound.getCoverArt(position)!=null) {holder.getSongCover().setImageBitmap(sound.getCoverArt(position));}
        else{holder.getSongCover().setImageResource(R.drawable.musicicon);}

        holder.getArtistName().setText(sound.getFileName(position));
        holder.getSongDuration().setText(sound.getDuration(position));
        holder.getSongSize().setText(sound.getFileSize(position));
        holder.getPlay_pause_ImageButton().setImageResource(R.drawable.play);
        holder.getSongCover().setAdjustViewBounds(true);

        //holder.getDusImageButton().setImageResource(R.drawable.share); //Use share image icon
        sound.playButtonEventListener(holder);
        sound.shareButtonEventListener(holder);
        if((sound.getSound() != null) && (sound.getSound().isPlaying()) && (position == sound.getPosition())){
            holder.getPlay_pause_ImageButton().setImageResource(R.drawable.pause);}
    }

    private void beatSetup(final SongViewHolder holder, final int position, final Button button) {
        holder.getArtistName().setText(beats.getFileName(position));
        holder.getSongCover().setImageResource(R.drawable.musicicon);
        holder.getSongDuration().setText(beats.getDuration(position));
        holder.getSongSize().setText("Select");
        beats.playButtonEventListener(holder);
        holder.getDusImageButton().setImageResource(R.drawable.select);
        holder.getDusImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setText(beats.getFileName(position));
                BeatFragment.beats[0] = beats.getBeat(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        switch (classID) {
            case 1: //Download
                break;
            case 2: //Upload
                break;
            case 3: //Music
                return music.getSize();
            case 4:
                return beats.getSize();
            case 5:
                return beats.getSize();
            case 6:
                return beats.getSize();
            case 7:
                return beats.getSize();
            case 8:
                return voice.getSize();
        }
        return -1;
    }
}
