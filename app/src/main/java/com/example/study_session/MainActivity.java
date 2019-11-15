package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.study_session.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String userName,school,uid;
    private List<String> userGroups;
    private ArrayList<Group> groups;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent login = new Intent(this, LoginActivity.class);
        startActivityForResult(login,LoginActivity.LOGIN_ACTIVITY);

        /*
        //TODO: Call getGroupsFromReference here somehow?
        groups = Group.getGroupsFromReference(userGroups);

        //bind GroupViewAdapter for group list
        GroupViewAdapter groupServer = new GroupViewAdapter(groups);
        RecyclerView groupsRV = findViewById(R.id.groupsRV);
        groupsRV.setAdapter(groupServer);
        //bind layoutManager for group list
        LinearLayoutManager groupManager = new LinearLayoutManager(this);
        groupsRV.setLayoutManager(groupManager);
        */
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
            userName = profile.getStringExtra("userName");
            school = profile.getStringExtra("school");
            uid = profile.getStringExtra("uid");
            userGroups = getUserGroups(uid);
        }
        else if(resultCode == LoginActivity.SUCCESSFUL_REGISTRATION){
            setContentView(R.layout.activity_main);
            userName = profile.getStringExtra("userName");
            school = profile.getStringExtra("school");
            uid = profile.getStringExtra("uid");
            //Start add times/groups
        }
    }

    public void createGroup(View view){
        Intent createGroup = new Intent(this,GroupCreateActivity.class);
        school = createGroup.getStringExtra("school");
        uid = createGroup.getStringExtra("uid");
        startActivity(createGroup);
    }

    public void showProfile(View view){
        Intent showProfile = new Intent(this,ProfileActivity.class);
        school = showProfile.getStringExtra("school");
        uid = showProfile.getStringExtra("uid");

        startActivity(showProfile);
    }

    public void showGroupSearch(View view){
        Intent showGroupSearch = new Intent(this,GroupSearchActivity.class);
        startActivity(showGroupSearch);
    }

    public void showGroup(View view){
        Intent showGroup = new Intent(this,GroupActivity.class);
        startActivity(showGroup);
    }

    //Stubbed
    public List<String> getUserGroups(String uid){
        return null;
    }
}
