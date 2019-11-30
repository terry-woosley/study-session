package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;


public class GroupActivity extends AppCompatActivity {

    Group group;
    Vector<Profile> memberArrayList = new Vector<Profile>();
    GroupMemberViewAdapter groupMemberViewAdapter;
    TextView creatorTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        //get all View Components
        TextView groupNameTV = findViewById(R.id.memberNameTV);
        TextView schoolTV = findViewById(R.id.schoolTV);
        TextView subjectTV = findViewById(R.id.subjectTV);
        creatorTV = findViewById(R.id.creatorTV);
        TextView timeTV = findViewById(R.id.timeTV);
        final Button joinBTN = findViewById(R.id.joinBTN);

        //get all importent Context Data from Intent
        Intent intent = getIntent();
        boolean join = intent.getBooleanExtra("join",false);
        group = (Group) intent.getSerializableExtra("group");
        final String uid = intent.getStringExtra("uid");
        final String username = intent.getStringExtra("username");

        //show all group information
        groupNameTV.setText(group.groupName);
        schoolTV.setText(group.groupSchool);
        subjectTV.setText(group.groupSubject);

        String timeString = "";
        for(Date d:group.groupTimesAvailable){
            timeString += d.dateToText() + "\n";
        }
        timeTV.setText(timeString);

        //if the user isn't the creator the login/leave button is visible
        //and the username will be fetched from the backend
        if(uid != "" && group != null && !group.groupCreator.equals(uid)){
            Profile.getUser(group.groupCreator, new Profile.UserCallBackFunction() {
                @Override
                public void done(Profile user) {
                    creatorTV.setText(user.name);
                }

                @Override
                public void error(Exception e) {

                }
            });
            joinBTN.setVisibility(View.VISIBLE);
            //if the user is already member the text of the button will be changed to join
            //if not it will be changed to leave
            if(!group.groupMembers.contains(uid) && join){
                joinBTN.setText("Join");
            }else {
                joinBTN.setText("Leave");
            }
        }else {
            creatorTV.setText(username);
            joinBTN.setVisibility(View.INVISIBLE);
        }

        //if the user is not a group member the onclicklistener will ad the user id to the group document in the backend
        //or it will removed from the document
        joinBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!group.groupMembers.contains(uid)){
                    Group.joinGroup(uid, group, new Group.CallBackFunction() {
                        @Override
                        public void done() {
                            group.groupMembers.add(uid);
                            memberArrayList.add(new Profile(username, "", new ArrayList<Date>()));
                            groupMemberViewAdapter.notifyDataSetChanged();
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
                                if(id.equals(uid))
                                    break;
                                i++;
                            }
                            group.groupMembers.remove(i);
                            i = 0;
                            for (Profile member:memberArrayList) {
                                if(member.name.equals(username))
                                    break;
                                i++;
                            }
                            memberArrayList.remove(i);
                            groupMemberViewAdapter.notifyItemRemoved(i);
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

        //a list of all groupmember names will be fetched from the backend
        //and everytime a new member is fetched the recyclerviewadapter will be notifyed
        Profile.getUserFromReference(group.groupMembers, memberArrayList, new Profile.MultipleUserCallBackFunction() {
            @Override
            public void done(int index) {
                groupMemberViewAdapter.notifyItemInserted(index);
            }

            @Override
            public void error(Exception e) {

            }
        });
        //the group member list will be shown in a recyclerview with an adapter and manager
        //only the name will be shown
        RecyclerView memberRV = findViewById(R.id.memberRV);
        groupMemberViewAdapter = new GroupMemberViewAdapter(memberArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        memberRV.setAdapter(groupMemberViewAdapter);
        memberRV.setLayoutManager(linearLayoutManager);

    }

    public void showMain(View view){
        finish();
    }
}