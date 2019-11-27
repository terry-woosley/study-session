package com.example.study_session;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.study_session.ProfileAdapters.GroupListAdapter;
import com.example.study_session.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseFirestore db;
    public static final int VIEW_PROFILE = 90;
    private String school, username, email, uid;
    private ArrayList<Date> availableTimes;
    private ArrayList<String> groups = new ArrayList<String>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (user != null) {
            setContentView(R.layout.activity_profile);
            TextView schoolSet = (TextView)findViewById(R.id.profileSchoolDisplayTV);
            TextView nameSet = (TextView)findViewById(R.id.profileNameDisplayTV);
            TextView emailSet = (TextView)findViewById(R.id.profileEmailDisplayTV);
            Intent intent = getIntent();
            populateProfile(intent);

            emailSet.setText(email);
            nameSet.setText(username);
            schoolSet.setText(school);
        } else {
            logOut(getCurrentFocus());
        }

    }

    public void populateProfile(Intent intent){
        uid = intent.getStringExtra("uid");
        username = intent.getStringExtra("username");
        school = intent.getStringExtra("school");
        email = user.getEmail();
        getGroups();
        getTimes();
    }

    public void showMain(View view) {
        finish();
    }

    public void logOut(View view){
        FirebaseAuth.getInstance().signOut();
        setResult(LoginActivity.LOGOUT);
        finish();
    }

    public void getGroups(){
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        groups = (ArrayList<String>) document.get("groups");
                        GroupListAdapter groupListAdapter = new GroupListAdapter(groups);
                        RecyclerView groupsRecycler = findViewById(R.id.groupsRecycle);
                        groupsRecycler.setAdapter(groupListAdapter);
                        LinearLayoutManager myManager = new LinearLayoutManager(context);
                        groupsRecycler.setLayoutManager(myManager);
                        Log.d("Retrieving Groups", "Group data: " + groups.toString());
                    } else {
                        Log.d("Retrieving Groups", "No such document");
                    }
                } else {
                    Log.d("Retrieving Groups", "get failed with ", task.getException());
                }
            }
        });
    }

    public void getTimes(){
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        availableTimes = (ArrayList<Date>) document.get("timesAvailable");
                        Log.d("Retrieving Times", "Time data: " + availableTimes.toString());
                    } else {
                        Log.d("Retrieving Times", "No such document");
                    }
                } else {
                    Log.d("Retrieving Times", "get failed with ", task.getException());
                }
            }
        });
    }



}