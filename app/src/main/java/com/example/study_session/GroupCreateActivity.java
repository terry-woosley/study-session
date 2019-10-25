package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.study_session.data.model.LoggedInUser;

public class GroupCreateActivity extends AppCompatActivity {

    Button createGroupBTN;
    EditText groupNameET;
    EditText groupSubject;
    EditText meetingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        createGroupBTN = (Button) findViewById(R.id.createGroupBTN);
        groupNameET = (EditText) findViewById(R.id.groupNameET);
        groupSubject = (EditText) findViewById(R.id.groupSubjectET);
        meetingTime = (EditText) findViewById(R.id.meetingTimeET);
        createGroupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Group group = new Group(groupNameET.getText().toString(),"", "",null,null,groupSubject.getText().toString());
                group.addNewGroup();
            }
        });
    }

    public void back(View view){
        finish();
    }
}
