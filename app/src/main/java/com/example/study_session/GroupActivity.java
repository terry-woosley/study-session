package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class GroupActivity extends AppCompatActivity {

    Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Intent intent = getIntent();
        boolean join = intent.getBooleanExtra("join",false);
        group = (Group) intent.getSerializableExtra("group");
        final String uid = intent.getStringExtra("uid");
        final Button joinBTN = findViewById(R.id.joinBTN);
        if(uid != "" && group != null && !group.groupCreator.equals(uid)){
            joinBTN.setVisibility(View.VISIBLE);
            if(!group.groupMembers.contains(uid) && join){
                joinBTN.setText("Join");
            }else {
                joinBTN.setText("Leave");
            }
        }else {
            joinBTN.setVisibility(View.INVISIBLE);
        }

        joinBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!group.groupMembers.contains(uid)){

                    Group.joinGroup(uid, group, new Group.CallBackFunction() {
                        @Override
                        public void done() {
                            group.groupMembers.add(uid);
                            Toast.makeText(getApplicationContext(),"You joined this Group!",Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void error(Exception e) {
                            Toast.makeText(getApplicationContext(),"Not possible to join this Group!",Toast.LENGTH_SHORT).show();
                        }
                    });
                    joinBTN.setText("Leave");
                }else {
                    Group.leaveGroup(uid, group, new Group.CallBackFunction() {
                        @Override
                        public void done() {
                            int i = 0;
                            for (String id:group.groupMembers) {
                                if(id == uid)
                                    break;
                                i++;
                            }
                            group.groupMembers.remove(i);
                            Toast.makeText(getApplicationContext(),"You leaved!",Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void error(Exception e) {
                            Toast.makeText(getApplicationContext(),"Not possible to leave this Group!",Toast.LENGTH_SHORT).show();
                        }
                    });
                    joinBTN.setText("Join");
                }

            }
        });
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
