package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

// initiating views


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


}