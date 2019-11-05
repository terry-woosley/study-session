package com.example.study_session.ui.login;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.study_session.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

/**
 * Performs tha main logic for the Login activity
 */
public class LoginActivity extends AppCompatActivity {

    public static final String PASSWORD_REGEX =
            "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{10,22}$";
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(PASSWORD_REGEX);
    public static final int REGISTER_USER = 101;
    public static final int SUCCESSFUL_REGISTRATION = 102;
    public static final int LOGIN_SUCCESS = 103;
    public static final int LOGIN_ACTIVITY = 104;
    public static final int EDIT_USER_INFO = 105;
    private FirebaseAuth mAuth;
    private EditText userEmailText, passwordText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    private String email,password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmailText = findViewById(R.id.emailView);
        passwordText = findViewById(R.id.passwordView);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
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

        try {
            email = userEmailText.getText().toString();
        }
        catch (NullPointerException e){
            userEmailText.setError("Please enter an email address");
            return;
        }
        try {
            password = passwordText.getText().toString();
        }
        catch (NullPointerException e){
            passwordText.setError("Please enter a password");
            return;
        }

        if (!isEmailValid(email)) {
            userEmailText.setError(getString(R.string.invalid_email));
        } else if (!isPasswordValid(password)) {
            passwordText.setError(getString(R.string.invalid_password));
        }
        else {

            loadingProgressBar.setVisibility(View.VISIBLE);
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            loadingProgressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SignIn", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent userLogin = new Intent();
                                setResult(LOGIN_SUCCESS, userLogin);
                                finish();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SignIn", "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        }
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

    private static boolean isPasswordValid(String password) {
        return (PASSWORD_PATTERN.matcher(password).matches());
    }

    private static boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        return (email.contains("@") && email.contains(".com"));
    }

}
