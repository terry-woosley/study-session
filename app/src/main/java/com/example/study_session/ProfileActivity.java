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
    public static final int EDIT_PROFILE = 91;
    public static final int UPDATE_PROFILE = 92;
    private String school, username, email, uid;
    private ArrayList<String> availableTimes = new ArrayList<>();
    private ArrayList<String> groups = new ArrayList<>();
    private Context context;
    private GroupListAdapter groupListAdapter;

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
            groupListAdapter = new GroupListAdapter();
            RecyclerView groupsRecycler = findViewById(R.id.groupsRecycle);
            groupsRecycler.setAdapter(groupListAdapter);
            LinearLayoutManager myManager = new LinearLayoutManager(context);
            groupsRecycler.setLayoutManager(myManager);
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
        Intent data = new Intent(this, EditProfile.class);
        data.putExtra("uid", uid);
        data.putExtra("school", school);
        data.putExtra("email", email);
        startActivityForResult(data, EDIT_PROFILE);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent profile){
        if (resultCode == UPDATE_PROFILE){
            TextView schoolSet = findViewById(R.id.profileSchoolDisplayTV);
            TextView emailSet = findViewById(R.id.profileEmailDisplayTV);

            email = profile.getStringExtra("email");
            school = profile.getStringExtra("school");

            emailSet.setText(email);
            schoolSet.setText(school);

        }
    }

    public void getGroups(){
        Profile.getUser(uid, new Profile.UserCallBackFunction() {
            @Override
            public void done(Profile user) {
                getGroupNames(user.groups);
            }

            @Override
            public void error(Exception e) {

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


    public void getGroupNames(ArrayList<String> groups){
        GroupListAdapter.preModel = new ArrayList<>();
        Group.getGroupNames(groups, GroupListAdapter.preModel, new Group.CallBackFunction() {
            @Override
            public void done() {
                GroupListAdapter.postModel = GroupListAdapter.preModel;
                groupListAdapter.notifyDataSetChanged();
            }

            @Override
            public void error(Exception e) {

            }
        });
    }



}