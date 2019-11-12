package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class GroupCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        Button createGroupBTN = findViewById(R.id.createGroupBTN);
        final EditText groupNameET = findViewById(R.id.groupNameET);
        final EditText groupSubjectET = findViewById(R.id.groupSubjectET);

        createGroupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Group group = new Group(groupNameET.getText().toString(),"", "", new ArrayList<Date>(), new ArrayList<String>(),groupSubjectET.getText().toString());
                group.addNewGroup();
            }
        });
    }

    public void back(View view){
        finish();
    }
}
