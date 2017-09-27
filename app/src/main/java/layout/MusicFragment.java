package layout;
/*
This fragment handles the view of the music information
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.locit.cecilhlungwana.inethi.R;
import com.locit.cecilhlungwana.inethi.Song;
import com.locit.cecilhlungwana.inethi.SongAdapter;

import java.util.List;

/**
 * Created by cecilhlungwana on 2017/09/06.
 */

public class MusicFragment extends Fragment{
    private String searchViewText;
    private int musicFragment;;
    private RecyclerView musicRecyclerView;
    private SongAdapter songAdapter;

    public MusicFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Load the view
        View view = inflater.inflate(R.layout.fragment_music_display, container, false);

        //Load the recycler view
        musicRecyclerView = (RecyclerView) view.findViewById(R.id.musicrecyclerView);
        songAdapter = new SongAdapter(getContext(), getMusicFragment(),view, null);
        songAdapter.setActivity(getActivity());
        musicRecyclerView.setAdapter(songAdapter);

        //Change recycler view orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        musicRecyclerView.setLayoutManager(layoutManager);
        return view;
    }

    public int getMusicFragment() {
        return musicFragment;
    }

    public void setMusicFragment(int musicFragment) {
        this.musicFragment = musicFragment;
    }
}
