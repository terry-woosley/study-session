package com.example.study_session;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Group {
    String groupName;
    String groupSchool;
    String creator;
    Date[] timesAvailable;
    String[] members;
    String subject;

    public Group(String groupName, String groupSchool, String creator, Date[] timesAvailable, String[] members, String subject){
        this.groupName = groupName;
        this.groupSchool = groupSchool;
        this.creator = creator;
        this.timesAvailable = timesAvailable;
        this.members = members;
        this.subject = subject;
    }

    public void addNewGroup(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> group = new HashMap<>();
        group.put("groupName", groupName);
        group.put("creator", creator);
        group.put("subject", subject);
        group.put("timesAvailable", timesAvailable);

        db.collection("groups")
                .add(group)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        // Create a new user with a first, middle, and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Alan");
        user.put("middle", "Mathison");
        user.put("last", "Turing");
        user.put("born", 1912);
    }
}
