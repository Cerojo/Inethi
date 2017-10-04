package layout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.locit.cecilhlungwana.inethi.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment {


    public UploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_display, container, false);
        return view;
    }

}
