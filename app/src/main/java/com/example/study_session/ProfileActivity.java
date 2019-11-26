package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.study_session.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileActivity extends AppCompatActivity {
    private FirebaseUser user;
    public static final int VIEW_PROFILE = 90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            setContentView(R.layout.activity_profile);
            TextView schoolSet = (TextView)findViewById(R.id.profileSchoolDisplayTV);
            TextView nameSet = (TextView)findViewById(R.id.profileNameDisplayTV);
            TextView emailSet = (TextView)findViewById(R.id.profileEmailDisplayTV);

            Intent intent = getIntent();

            String school = intent.getStringExtra("school");
            String email = intent.getStringExtra("email");
            String username = intent.getStringExtra("username");



            nameSet.setText(username);
            schoolSet.setText(school);
        } else {
            // No user is signed in
        }

    }

    public void populateProfie(){
        //get name from database
        //get email from database
        //get profile pic from database
        //get times from database
        //get groups from database
    }

    public void showMain(View view) {
        finish();
    }

    public void logOut(View view){
        FirebaseAuth.getInstance().signOut();
        setResult(LoginActivity.LOGOUT);
        finish();
    }



}