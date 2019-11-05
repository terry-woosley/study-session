package com.example.study_session.ui.login;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.study_session.R;

/**
 * Performs tha main logic for the Login activity
 */
public class LoginActivity extends AppCompatActivity {

    public static final int REGISTER_USER = 101;
    public static final int SUCCESSFUL_REGISTRATION = 102;
    public static final int LOGIN_SUCCESS = 103;
    public static final int LOGIN_ACTIVITY = 104;
    public static final int EDIT_USER_INFO = 105;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.emailView);
        final EditText passwordEditText = findViewById(R.id.passwordView);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final TextView signUp = findViewById(R.id.signUp);


    }

    /**
     * Starts the RegisterNewUser activity
     *
     * @param view the screen View
     */
    public void registerNewUser(View view){
        Intent registerIntent = new Intent(this, RegisterNewUser.class);
        startActivityForResult(registerIntent,REGISTER_USER);
    }

    /**
     * Destroys login and starts **** on successful user registration,
     * and displays error message on failed registration
     *
     * @param requestCode the code the activity was started with
     * @param resultCode the result of the activity
     * @param data the intent from the activity
     */
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode == SUCCESSFUL_REGISTRATION){
            String welcome = getString(R.string.welcome) + data.getStringExtra("userName");
            Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
            setResult(SUCCESSFUL_REGISTRATION, data);
            finish();
        }
        else {
            showRegistrationFail("Failed to create account");
        }
    }

    public void loginUser(View view){
        Intent userLogin = new Intent();
        setResult(LOGIN_SUCCESS, userLogin);
        finish();
    }

    /**
     * Displays a welcome message to the user upon successful login
     *
     * @param model with the user's login information
     */
    private void updateUiWithUser(Intent model) {
        String welcome = getString(R.string.welcome) + model.getStringExtra("userName");
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    /**
     * Prints a pop-up error message to the screen when Login fails
     *
     * @param errorString the error message to display to user
     */
    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    /**
     * Prints a pop-up error message to the screen when registration failes
     *
     * @param message the error message to display to user
     */
    private void showRegistrationFail(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
