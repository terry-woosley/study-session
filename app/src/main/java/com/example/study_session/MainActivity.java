package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.study_session.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    public static final int LOGIN_SUCCESS = 103;
    public static final int LOGIN_ACTIVITY = 104;
    public static final int EDIT_USER_INFO = 105;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent login = new Intent(this, LoginActivity.class);
        startActivityForResult(login,LOGIN_ACTIVITY);
    }

    /**
     * Handles the result of either user login or registration
     *
     * @param requestCode the activity that requested the response
     * @param resultCode the result of the activity
     * @param profile the user profile information
     */
    public void onActivityResult(int requestCode,int resultCode,Intent profile){
        if(resultCode == LOGIN_SUCCESS){
            setContentView(R.layout.activity_main);
        }
        else if(resultCode == LoginActivity.SUCCESSFUL_REGISTRATION){
            Intent userInfo = new Intent();
            startActivityForResult(userInfo, EDIT_USER_INFO);
        }
    }
}
