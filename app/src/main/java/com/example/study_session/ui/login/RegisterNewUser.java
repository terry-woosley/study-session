package com.example.study_session.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.study_session.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegisterNewUser extends AppCompatActivity {

    private static final String PASSWORD_REGEX =
            "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{10,22}$";
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(PASSWORD_REGEX);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);
    }

    public void registerUser(View view){
        //TODO add logic for creating and validating user

        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        //.setUrl("https://www.example.com/finishSignUp?cartId=1234")
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setAndroidPackageName(
                                "com.example.android",
                                true, /* installIfNotAvailable */
                                "12"    /* minimumVersion */)
                        .build();

        EditText emailView = findViewById(R.id.emailView);
        String email = emailView.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("EMAIL", "Email sent.");
                        }
                    }
                });

        Intent user = new Intent();
        setResult(LoginActivity.SUCCESSFUL_REGISTRATION, user);
        finish();
    }


    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        if (!(PASSWORD_PATTERN.matcher(password).matches())){
            return false;
        }

        return true;
    }

    // A placeholder username validation check
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (!email.contains("@")) {
            return false;
        }
        else {
            return !email.trim().isEmpty();
        }
    }
}
