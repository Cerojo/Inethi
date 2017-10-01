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
import android.widget.EditText;
import android.widget.Toast;

import com.locit.cecilhlungwana.inethi.R;
import com.locit.cecilhlungwana.inethi.USER;
import com.locit.cecilhlungwana.inethi.UserInfoDBHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private Button signoutButton;
    private Button saveButton;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    private final String mUsername = "username";
    private String username;

    private EditText name;
    private EditText bio;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        if(getArguments()!=null) {username = getArguments().getString(mUsername);}

        name = (EditText)view.findViewById(R.id.artistNameeditText);
        bio = (EditText)view.findViewById(R.id.bioeditText);
        lookupUser();

        signoutClickEvent(view);
        saveClickEvent(view);
        return view;
    }

    private void saveClickEvent(View view) {
        saveButton = (Button) view.findViewById(R.id.savebutton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoUser();
            }
        });
    }

    private void infoUser(){//View view) {
        UserInfoDBHandler dbHandler = new UserInfoDBHandler(getContext(), null, null, 1);
        USER user = dbHandler.findUser(username);
        if(name.getText() != null || bio.getText() != null) {
            user.setName(name.getText().toString());
            user.setBio(bio.getText().toString());
            dbHandler.addInfo(user);
            Toast.makeText(getContext(),"Saved",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getContext(), "Please fill in all fields",Toast.LENGTH_LONG).show();
        }
    }

    private void signoutClickEvent(View view) {
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
    }

    private void lookupUser(){
        UserInfoDBHandler dbHandler = new UserInfoDBHandler(getContext(), null, null, 1);
        USER USER = dbHandler.findUser(username);
        if(USER != null){
            if(USER.getName() != null){
                name.setText(USER.getName());
            }
            if(USER.getBio() != null){
                bio.setText(USER.getBio());
            }
        }
    }
}
