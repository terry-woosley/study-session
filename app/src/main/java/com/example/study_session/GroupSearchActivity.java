package com.example.study_session;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GroupSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search);
    }

    public void showGroup(View view){
        Intent showGroup = new Intent(this,GroupActivity.class);
        startActivity(showGroup);
    }

    public void showMain(View view){
        finish();
    }
}
