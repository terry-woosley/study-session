package com.example.study_session.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.study_session.MainActivity;
import com.example.study_session.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class RegisterNewUser extends AppCompatActivity {

    private static final String PASSWORD_REGEX =
            "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{10,22}$";
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(PASSWORD_REGEX);
    private EditText emailView;
    private EditText passwordView;
    private String password;
    private String email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);
        emailView = findViewById(R.id.emailView);
        passwordView = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        final Button createAccount = findViewById(R.id.createBTN);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailView.getText().toString();
                password = passwordView.getText().toString();
                if (!isEmailValid(email)){
                    emailView.setError(getString(R.string.invalid_email));
                }
                else if (!isPasswordValid(password)){
                    passwordView.setError(getString(R.string.invalid_password));
                }
                else {
                    createAccount(email,password);
                }
                //TODO fix me
            }
        });


    }

    public void createAccount(String email, String password){
        //TODO add logic for creating and validating user

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("CreateTag", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("CreateTag", "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                            //        Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });



    }


    // A placeholder password validation check
    private static boolean isPasswordValid(String password) {
        return (PASSWORD_PATTERN.matcher(password).matches());
    }

    // A placeholder username validation check
    private static boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        return (email.contains("@") && email.contains(".com"));
    }
}
