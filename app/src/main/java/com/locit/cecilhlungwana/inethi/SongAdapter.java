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
import android.widget.SearchView;

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
    private View parentView;

    //Constructor
    public SongAdapter(Context context, int classID, View view){
        inflater = LayoutInflater.from(context);
        this.classID = classID;
        music = new Music(context);
        beats = new Beats(context);

        this.parentView = view;

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

        switch (classID) {
            case 1: //Download
                break;
            case 2: //Upload
                break;
            case 3: //Music
                musicSetup(holder, position);
                break;
            case 4:
                beatSetup(holder, position);
                break;
        }
    }

    private void musicSetup(SongViewHolder holder, int position) {
        if(music.getCoverArt(position)!=null) {holder.getSongCover().setImageBitmap(music.getCoverArt(position));}
        else{holder.getSongCover().setImageResource(R.drawable.musicicon);}

        holder.getArtistName().setText(music.getArtistName(position));
        holder.getSongDuration().setText(music.getDuration(position));
        holder.getSongSize().setText(music.getFileSize(position));
        holder.getPlay_pause_ImageButton().setImageResource(R.drawable.play);
        holder.getSongCover().setAdjustViewBounds(true);

        holder.getDusImageButton().setImageResource(R.drawable.share); //Use share image icon
        music.playButtonEventListener(holder);
        music.shareButtonEventListener(holder);
        if((music.getMusic() != null) && (music.getMusic().isPlaying()) && (position == music.getPosition())){
            holder.getPlay_pause_ImageButton().setImageResource(R.drawable.pause);}
    }

    private void beatSetup(SongViewHolder holder, int position) {
        if(beats.getCoverArt(position)!=null) {holder.getSongCover().setImageBitmap(beats.getCoverArt(position));}
        else{holder.getSongCover().setImageResource(R.drawable.musicicon);}

        holder.getArtistName().setText(beats.getArtistName(position));
        holder.getSongDuration().setText(beats.getDuration(position));
        holder.getSongSize().setText(beats.getFileSize(position));
        holder.getPlay_pause_ImageButton().setImageResource(R.drawable.play);
        holder.getSongCover().setAdjustViewBounds(true);

        holder.getDusImageButton().setImageResource(R.drawable.share); //Use share image icon
        beats.playButtonEventListener(holder);
        beats.shareButtonEventListener(holder);
        if((beats.getBeat() != null) && (beats.getBeat().isPlaying()) && (position == beats.getPosition())){
            holder.getPlay_pause_ImageButton().setImageResource(R.drawable.pause);}
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
        }
        return -1;
    }
}
