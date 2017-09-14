package layout;
/*
This profile fragment class handles the profile screen view
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.locit.cecilhlungwana.inethi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private Button signoutButton;
    private Button saveButton;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        signoutButton = (Button)view.findViewById(R.id.signoutbutton);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content, new LoginFragment()); //Switch fragments
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        saveButton = (Button) view.findViewById(R.id.savebutton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getContext(), PopupActivity.class);
                //v.getContext().startActivity(intent);
            }
        });
        return view;
    }

}
