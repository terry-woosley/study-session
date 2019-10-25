package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GroupCreateActivity extends AppCompatActivity {

    Button createGroupBTN;
    EditText groupNameET;
    EditText groupSubject;
    EditText meetingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        createGroupBTN = findViewById(R.id.createBTN);
        groupNameET = findViewById(R.id.groupNameET);
        groupSubject = findViewById(R.id.groupSubjectET);
        meetingTime = findViewById(R.id.meetingTimeET);
        createGroupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
    }

    public void back(View view){
        finish();
    }
}
