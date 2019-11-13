package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class GroupCreateActivity extends AppCompatActivity {
    private String school,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        Intent profile = getIntent();
        school = profile.getStringExtra("school");
        uid = profile.getStringExtra("uid");

        final ArrayList<Date> timesAvailable = new ArrayList<>();
        Button createGroupBTN = findViewById(R.id.createGroupBTN);
        Button addTimeBTN = findViewById(R.id.addTimeBTN);
        final EditText groupNameET = findViewById(R.id.groupNameET);
        final EditText groupSubjectET = findViewById(R.id.groupSubjectET);

        addTimeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //reconvert values everytime addTimeBTN is clicked
                EditText weekdayET = findViewById(R.id.weekdayTimeET);
                EditText hourET = findViewById(R.id.hourTimeET);
                String hourString = hourET.getText().toString();
                Integer hour = Integer.parseInt(hourString);
                EditText minuteET = findViewById(R.id.minuteTimeET);
                String minuteString = minuteET.getText().toString();
                Integer minute = Integer.parseInt(minuteString);
                //parse date and add it to timesAvailable
                Date date = new Date(weekdayET.getText().toString(), hour, minute);
                timesAvailable.add(date);
                Log.d("LOG", "Date " + date.dateToText() + " added to times available");
            }
        });

        createGroupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Group group = new Group(groupNameET.getText().toString(), school, uid, timesAvailable, new ArrayList<String>(),groupSubjectET.getText().toString());
                group.addNewGroup();
            }
        });
    }

    public void back(View view){
        finish();
    }
}
