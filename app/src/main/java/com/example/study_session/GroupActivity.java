package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class GroupActivity extends AppCompatActivity {

    Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Intent intent = getIntent();
        group = (Group) intent.getSerializableExtra("group");
        TextView groupNameTV = findViewById(R.id.groupNameTV);
        TextView schoolTV = findViewById(R.id.schoolTV);
        TextView subjectTV = findViewById(R.id.subjectTV);
        TextView creatorTV = findViewById(R.id.creatorTV);
        TextView timeTV = findViewById(R.id.timeTV);
        RecyclerView memberRV = findViewById(R.id.memberRV);
        groupNameTV.setText(group.groupName);
        schoolTV.setText(group.groupSchool);
        subjectTV.setText(group.groupSubject);
        creatorTV.setText(group.groupCreator);
        String timeString = "";

        timeTV.setText(timeString);

    }

    public void showMain(View view){
        finish();
    }
}
