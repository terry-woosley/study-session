package com.example.study_session.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.study_session.R;

public class RegisterNewUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);
    }

    public void registerUser(View view){
        //TODO add logic for creating and validating user
        setResult(LoginActivity.SUCCESSFUL_REGISTRATION);
    }
}
