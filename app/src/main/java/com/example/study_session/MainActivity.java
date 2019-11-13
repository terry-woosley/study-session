package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.study_session.ui.login.LoginActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String userName,school,uid;
    private List<String> groups;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent login = new Intent(this, LoginActivity.class);
        startActivityForResult(login,LoginActivity.LOGIN_ACTIVITY);

        //TODO: bind GroupViewAdapter
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
            groups = getUserGroups(uid);
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
        userName = createGroup.getStringExtra("userName");
        school = createGroup.getStringExtra("school");
        uid = createGroup.getStringExtra("uid");
        startActivity(createGroup);
    }

    public void showProfile(View view){
        Intent showProfile = new Intent(this,ProfileActivity.class);
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
