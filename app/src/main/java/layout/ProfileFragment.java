package layout;
/*
This profile fragment class handles the profile screen view
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.locit.cecilhlungwana.inethi.DbBitmapUtility;
import com.locit.cecilhlungwana.inethi.R;
import com.locit.cecilhlungwana.inethi.USER;
import com.locit.cecilhlungwana.inethi.UserInfoDBHandler;

import java.io.FileNotFoundException;
import java.io.IOException;

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

    private ImageView userProfile;

    public static final int GET_FROM_GALLERY = 3;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        loginPreferences = getActivity().getSharedPreferences("loginPrefs", getActivity().MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        //Populate user information
        userProfile = (ImageView)view.findViewById(R.id.userimageView);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        if(getArguments()!=null) {username = getArguments().getString(mUsername);}
        name = (EditText)view.findViewById(R.id.artistNameeditText);
        bio = (EditText)view.findViewById(R.id.bioeditText);
        lookupUser();

        signoutClickEvent(view);
        saveClickEvent(view);
        return view;
    }

    //Handles save button
    private void saveClickEvent(View view) {
        saveButton = (Button) view.findViewById(R.id.savebutton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoUser();
            }
        });
    }

    //Get user information
    private void infoUser(){
        UserInfoDBHandler dbHandler = new UserInfoDBHandler(getContext(), null, null, 1);
        USER user = dbHandler.findUser(username);
        if(name.getText() != null || bio.getText() != null) {
            user.setName(name.getText().toString());
            user.setBio(bio.getText().toString());
            user.setImage(((BitmapDrawable)userProfile.getDrawable()).getBitmap());
            dbHandler.addInfo(user);
            Toast.makeText(getContext(),"Saved",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getContext(), "Please fill in all fields",Toast.LENGTH_LONG).show();
        }
    }

    //Handles signout
    private void signoutClickEvent(View view) {
        signoutButton = (Button)view.findViewById(R.id.signoutbutton);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPrefsEditor.putBoolean("saveLogin", false);
                loginPrefsEditor.commit();
                fragmentManager = getActivity().getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content, new LoginFragment()); //Switch fragments
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    //Lookup user
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
            try {
                userProfile.setImageBitmap(DbBitmapUtility.getImage(USER.getImage()));
            }
            catch (Exception ignored){}
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == getActivity().RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                userProfile.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
