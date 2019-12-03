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
import com.example.study_session.ProfileAdapters.TimeAdapter;
import com.example.study_session.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class ProfileActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseFirestore db;
    public static final int VIEW_PROFILE = 90;
    private String school, username, email, uid;
    private ArrayList<String> availableTimes = new ArrayList<>();
    private ArrayList<String> groups = new ArrayList<>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (user != null) {
            setContentView(R.layout.activity_profile);
            TextView schoolSet = findViewById(R.id.profileSchoolDisplayTV);
            TextView nameSet = findViewById(R.id.profileNameDisplayTV);
            TextView emailSet = findViewById(R.id.profileEmailDisplayTV);
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

    public void editProfile(View view){
        Intent intent = new Intent(this, EditProfile.class);
        intent.putExtra("uid", uid);
        intent.putExtra("userName", username);
        intent.putExtra("email", email);
        startActivity(intent);
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
                        if (groups != null){
                            GroupListAdapter groupListAdapter = new GroupListAdapter(groups);
                            RecyclerView groupsRecycler = findViewById(R.id.groupsRecycle);
                            groupsRecycler.setAdapter(groupListAdapter);
                            LinearLayoutManager myManager = new LinearLayoutManager(context);
                            groupsRecycler.setLayoutManager(myManager);
                            Log.d("Retrieving Groups", "Group data: " + groups.toString());
                        }
                        else {
                            ArrayList<String> nullArray = new ArrayList<>();
                            nullArray.add("No groups, join a few!");
                        }
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
                        ArrayList<Map<String, Object>> availableDates = (ArrayList<Map<String, Object>>) document.get("timesAvailable");
                        for (Map<String, Object> date : availableDates){
                            Map<String, Object> innerMap = (Map<String, Object>) date.get("timeOfDay");
                            Long hour = (Long)innerMap.get("hour");
                            Long minute = (Long)innerMap.get("minute");
                            String meridien = innerMap.get("meridiem").toString();
                            String dayOfTheWeek = date.get("dayOfTheWeek").toString();
                            Date newDate = new Date (dayOfTheWeek,hour.intValue(),minute.intValue(), meridien);
                            availableTimes.add(newDate.dateToText());
                        }
                        if (availableTimes != null){
                            TimeAdapter timeAdapter = new TimeAdapter(availableTimes);
                            RecyclerView timeRecycler = findViewById(R.id.timesRecycle);
                            timeRecycler.setAdapter(timeAdapter);
                            LinearLayoutManager myManager = new LinearLayoutManager(context);
                            timeRecycler.setLayoutManager(myManager);
                            Log.d("Retrieving Times", "Time data: " + availableTimes.toString());
                        }
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