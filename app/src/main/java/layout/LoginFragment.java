package layout;
/*
This login fragment class handles the login in view
 */

import android.content.SharedPreferences;
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

import com.locit.cecilhlungwana.inethi.USER;
import com.locit.cecilhlungwana.inethi.UserInfoDBHandler;
import com.locit.cecilhlungwana.inethi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private String username = "Admin";
    private EditText usernameInput;
    private EditText passwordInput;
    private String password = "Admin@music";
    private Button skipButton;
    private Button loginButton;
    private Button registerButton;
    private CheckBox rememberMeCheckBox;
    private final String mUsername = "username";
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginButton = (Button)view.findViewById(R.id.logingbutton);
        registerButton = (Button)view.findViewById(R.id.registerbutton);
        rememberMeCheckBox = (CheckBox) view.findViewById(R.id.rememberMecheckBox);
        usernameInput = (EditText)view.findViewById(R.id.usernameeditText);
        passwordInput = (EditText)view.findViewById(R.id.passwordeditText);

        //Login button click event
        loginClickEvent();

        //Handle register button click event
        registerClickEvent();

        loginPreferences = getActivity().getSharedPreferences("loginPrefs", getActivity().MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin) {
            usernameInput.setText(loginPreferences.getString("username", ""));
            passwordInput.setText(loginPreferences.getString("password", ""));
            rememberMeCheckBox.setChecked(true);
        }
        return view;
    }

    private void newUser(){//View view) {
        UserInfoDBHandler dbHandler = new UserInfoDBHandler(getContext(), null, null, 1);
        USER user = new USER(usernameInput.getText().toString(), passwordInput.getText().toString());
        dbHandler.addUser(user);
    }

    private boolean lookupUser(){//View view) {
        UserInfoDBHandler dbHandler = new UserInfoDBHandler(getContext(), null, null, 1);
        USER USER = dbHandler.findUser(usernameInput.getText().toString());
        if(USER != null){
            if(USER.getUserName().equals(usernameInput.getText().toString()) && USER.getPassword().equals(passwordInput.getText().toString())){
                return true;
            }
        }
        return false;
    }

    private void registerClickEvent() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //User not in database
                if((username.equals(usernameInput.getText().toString()) && password.equals(passwordInput.getText().toString())) || lookupUser()){
                    Toast.makeText(getContext(), "User already exists", Toast.LENGTH_LONG).show();
                }

                //Empty fields
                else if(usernameInput.getText().toString().equals("") || passwordInput.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Please fill in the entire fields", Toast.LENGTH_LONG).show();
                }

                //Authorised
                else {
                    Toast.makeText(getContext(), "Registered", Toast.LENGTH_LONG).show();
                    newUser();
                    Bundle bundle = new Bundle();
                    bundle.putString(mUsername, usernameInput.getText().toString());
                    ProfileFragment fragment = new ProfileFragment();
                    fragment.setArguments(bundle);
                    changeFragment(fragment);

                    rememberMeMethod();
                }
            }
        });
    }

    private void loginClickEvent() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Authorised
                if(username.equals(usernameInput.getText().toString()) && password.equals(passwordInput.getText().toString()) || lookupUser()){
                    Bundle bundle = new Bundle();
                    bundle.putString(mUsername, usernameInput.getText().toString());
                    ProfileFragment fragment = new ProfileFragment();
                    fragment.setArguments(bundle);
                    changeFragment(fragment);

                    rememberMeMethod();
                }

                //Empty fields
                else if(usernameInput.getText().toString().equals("") || passwordInput.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Please fill in the entire fields", Toast.LENGTH_LONG).show();
                }

                //User does not exist
                else {
                    Toast.makeText(getContext(), "User does not exist", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void rememberMeMethod() {
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();

        if (rememberMeCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", username);
            loginPrefsEditor.putString("password", password);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }
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
