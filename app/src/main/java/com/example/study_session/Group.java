package com.example.study_session;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Group {
    String groupName;
    String groupSchool;
    String groupCreator;
    ArrayList<Date> groupTimesAvailable;
    ArrayList<String> groupMembers;
    String groupSubject;

    public Group(String name, String school, String creator, ArrayList<Date> timesAvailable, ArrayList<String> members, String subject){
        this.groupName = name;
        this.groupSchool = school;
        this.groupCreator = creator;
        this.groupTimesAvailable = timesAvailable;
        this.groupMembers = members;
        this.groupSubject = subject;
    }

    public void addNewGroup(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> group = new HashMap<>();
        group.put("groupName", groupName);
        group.put("groupCreator", groupCreator);
        group.put("groupSubject", groupSubject);
        group.put("groupTimesAvailable", groupTimesAvailable);
        group.put("groupMembers", groupMembers);
        group.put("groupSchool", groupSchool);

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
    }

    public void joinGroup(String uid, String groupID){
        //TODO: Retrive user id, get reference to group, update member array of group with user id
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference groupDoc = db.collection("groups").document(groupID);
        final ArrayList<String> groupMembersList = new ArrayList<>();
        //retrieve members list from referenced group
        db.collection("groups").whereEqualTo("id", groupID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ArrayList<String> groupMembers = (ArrayList<String>) document.get("groupMembers");
                        for(int i = 0; i < groupMembers.size(); i++) {
                            groupMembersList.add(groupMembers.get(i));
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting document: ", task.getException());
                }
            }
        });
        //add new member to list
        groupMembersList.add(uid);
        //update field in groupMembers with new group members list
        groupDoc.update("groupMembers", groupMembersList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public static ArrayList<Group> getGroupsFromReference(List<String> groupReferences) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Group> groupArrayList = new ArrayList<>();
        for (int i = 0; i < groupReferences.size(); i++) {
            db.collection("groups").whereEqualTo("id", groupReferences.get(i))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String groupName = (String) document.get("groupName");
                                    String groupSchool = (String) document.get("groupSchool");
                                    String groupCreator = (String) document.get("groupCreator");
                                    ArrayList<Date> groupTimesAvailable = (ArrayList<Date>) document.get("groupTimesAvailable");
                                    ArrayList<String> groupMembers = (ArrayList<String>) document.get("groupMembers");
                                    String groupSubject = (String) document.get("groupSubject");
                                    groupArrayList.add(new Group(groupName, groupSchool, groupCreator, groupTimesAvailable, groupMembers, groupSubject));
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            } else {
                                Log.d(TAG, "Error getting document: ", task.getException());
                            }
                        }
                    });
        }
        return groupArrayList;
    }
    
    public ArrayList<Group> getGroupsFromUniversity(final String school){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<Group> groupArrayList= new ArrayList<Group>();
        db.collection("groups").whereEqualTo("groupSchool", school)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String groupName = (String) document.get("groupName");
                                String groupCreator = (String) document.get("groupCreator");
                                String groupSubject = (String) document.get("groupSubject");
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
