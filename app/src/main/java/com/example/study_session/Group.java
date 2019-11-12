package com.example.study_session;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
        group.put("members", members);

        db.collection("groups").add(group).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
    }

    public void joinGroup(String uid, String groupID){
        //TODO: Retrive user id, get reference to group, update member array of group with user id
    }

    public void getSingleGroup(){

    }
    
    public ArrayList<Group> getGroupsFromUniversity(final String school){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Group> groupArrayList= new ArrayList<Group>();
        db.collection("groups").whereEqualTo("groupSchool", school).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String groupName = (String) document.get("groupName");
                                String groupCreator = (String) document.get("creator");
                                String groupSubject = (String) document.get("subject");
                                groupArrayList.add(new Group(groupName, school, groupCreator,null,null,groupSubject));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return groupArrayList;
    }
}
