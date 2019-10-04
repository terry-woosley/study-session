package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.study_session.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent login = new Intent(this, LoginActivity.class);
        startActivityForResult(login,LoginActivity.LOGIN_ACTIVITY);
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
        }
        else if(resultCode == LoginActivity.SUCCESSFUL_REGISTRATION){
            setContentView(R.layout.activity_main);
        }
    }
}
