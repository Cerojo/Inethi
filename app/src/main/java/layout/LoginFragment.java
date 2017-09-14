package layout;
/*
This login fragment class handles the login in view
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.locit.cecilhlungwana.inethi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private final String username = "Admin";
    private EditText usernameInput;
    private EditText passwordInput;
    private final String password = "Admin@music";
    private Button skipButton;
    private Button loginButton;
    private Button registerButton;
    private CheckBox rememberMeCheckBox;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        skipButton = (Button)view.findViewById(R.id.skipbutton);
        loginButton = (Button)view.findViewById(R.id.logingbutton);
        registerButton = (Button)view.findViewById(R.id.registerbutton);
        rememberMeCheckBox = (CheckBox) view.findViewById(R.id.rememberMecheckBox);

        usernameInput = (EditText)view.findViewById(R.id.usernameeditText);
        passwordInput = (EditText)view.findViewById(R.id.passwordeditText);

        //Skip button click event
        skipClickEvent();

        //Login button click event
        loginClickEvent();

        //Handle register button click event
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //User not in database
                if(username.equals(usernameInput.getText().toString()) && password.equals(passwordInput.getText().toString())){
                    Toast.makeText(getContext(), "User already exists", Toast.LENGTH_LONG).show();
                }

                //Empty fields
                else if(usernameInput.getText().toString().equals("") || passwordInput.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Please fill in the entire fields", Toast.LENGTH_LONG).show();
                }

                //Authorised
                else {
                    Toast.makeText(getContext(), "Registered", Toast.LENGTH_LONG).show();
                    changeFragment(new ProfileFragment());
                }
            }
        });

        return view;
    }

    private void skipClickEvent() {
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProfileFragment();
                changeFragment(fragment);
            }
        });
    }

    private void loginClickEvent() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Authorised
                if(username.equals(usernameInput.getText().toString()) && password.equals(passwordInput.getText().toString())){
                    changeFragment(new ProfileFragment());
                }

                //User does not exist
                else {
                    Toast.makeText(getContext(), "User does not exist", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Switch to profile class
    private void changeFragment(Fragment newFragment) {
        FragmentManager fragmentManager;
        FragmentTransaction transaction;

        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
