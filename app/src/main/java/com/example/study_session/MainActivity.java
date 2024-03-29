package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.study_session.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Vector;

public class MainActivity extends AppCompatActivity{

    private String userName,school,uid;
    private ArrayList<String> userGroups;
    private Vector<Group> groups;
    private boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        groups = new Vector<Group>();

        Intent login = new Intent(this, LoginActivity.class);
        startActivityForResult(login,LoginActivity.LOGIN_ACTIVITY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLoggedIn) {
            Profile.getUser(uid, new Profile.UserCallBackFunction() {
                @Override
                public void done(Profile user) {
                    userGroups = user.groups;
                    groups = new Vector<Group>();
                    bindGroupsRecyclerView();
                }

                @Override
                public void error(Exception e) {

                }
            });
        }
    }


    /**
     * Handles the result of either user login or registration
     *
     * @param requestCode the activity that requested the response
     * @param resultCode the result of the activity
     * @param profile the user profile information
     */
    public void onActivityResult(int requestCode,int resultCode,Intent profile){
        if(resultCode == LoginActivity.LOGIN_SUCCESS){
            setContentView(R.layout.activity_main);
            isLoggedIn = true;
            userName = profile.getStringExtra("userName");
            school = profile.getStringExtra("school");
            uid = profile.getStringExtra("uid");
            userGroups = profile.getExtras().getStringArrayList("groups");
            bindGroupsRecyclerView();
        }
        else if(resultCode == LoginActivity.SUCCESSFUL_REGISTRATION){
            setContentView(R.layout.activity_main);
            isLoggedIn = true;
            userName = profile.getStringExtra("userName");
            school = profile.getStringExtra("school");
            uid = profile.getStringExtra("uid");
        }
        else if (resultCode == LoginActivity.LOGOUT){
            Intent login = new Intent(this, LoginActivity.class);
            login.putExtra("requestCode",LoginActivity.LOGOUT );
            isLoggedIn = false;
            startActivityForResult(login,LoginActivity.LOGOUT);
        }

    }

    /**
     * Handles the binding of the adapter and layout for the groups recycler view
     */
    public void bindGroupsRecyclerView(){

        if(userGroups != null) {
            //bind GroupViewAdapter for group list
            final GroupViewAdapter groupServer = new GroupViewAdapter(groups);
            RecyclerView groupsRV = findViewById(R.id.groupsRV);
            groupsRV.setAdapter(groupServer);
            //bind layoutManager for group list
            LinearLayoutManager groupManager = new LinearLayoutManager(this);
            groupsRV.setLayoutManager(groupManager);

            Group.getGroupsFromReference(userGroups, groups, new Group.MultipleGroupsCallBackFunction() {
                @Override
                public void done(int index) {
                    groupServer.notifyItemInserted(index);
                    Log.d("MAIN", "groups retrieved from login " + groups);
                }

                @Override
                public void error(Exception e) {
                    Log.d("MAIN", "groups not retrieved from login " + e);
                }
            });

        }
        Log.d("MAIN", "Current groups lists. userGroups: " + userGroups + " groups: " + groups);
    }

    public void createGroup(View view){
        Intent createGroup = new Intent(this,GroupCreateActivity.class);
        createGroup.putExtra("school",school);
        createGroup.putExtra("uid",uid);
        startActivity(createGroup);
    }

    public void showProfile(View view){
        Intent showProfile = new Intent(this,ProfileActivity.class);
        showProfile.putExtra("school",school);
        showProfile.putExtra("uid",uid);
        showProfile.putExtra("username",userName);
        startActivityForResult(showProfile, ProfileActivity.VIEW_PROFILE);
    }

    public void showGroupSearch(View view){
        Intent showGroupSearch = new Intent(this,GroupSearchActivity.class);
        showGroupSearch.putExtra("school", school);
        showGroupSearch.putExtra("uid",uid);
        showGroupSearch.putExtra("username",userName);
        startActivity(showGroupSearch);
    }

    public void showGroup(View view){
        Intent showGroup = new Intent(this,GroupActivity.class);
        startActivity(showGroup);
    }

}
