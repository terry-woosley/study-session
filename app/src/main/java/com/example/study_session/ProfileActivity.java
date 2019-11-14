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

    }

// initiating views
    TextView school = (TextView)findViewById(R.id.profileSchoolDisplayTV);
    TextView name = (TextView)findViewById(R.id.profileNameDisplayTV);
    TextView time = (TextView)findViewById(R.id.profileTimesDisplayTV);
    TextView groups = (TextView)findViewById(R.id.profileListOfGroupsDisplayTV);
    TextView email = (TextView)findViewById(R.id.profileEmailDisplayTV);

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