package com.example.study_session;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static android.content.ContentValues.TAG;


public class Group implements Serializable {
    interface CallBackFunction {
        public void done();
        public void error(Exception e);
    }

    interface MultipleGroupsCallBackFunction {
        public void done(int index);
        public void error(Exception e);
    }

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
        if (members != null){
            this.groupMembers = members;
        }else {
            this.groupMembers = new ArrayList<String>();
        }
        this.groupSubject = subject;
    }

    public void addNewGroup(final CallBackFunction callBackFunction){
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
                        callBackFunction.done();
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        callBackFunction.error(e);
                    }
                });
    }

    static public void joinGroup(final String uid, final Group group, final CallBackFunction callBackFunction){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<String> groupMembersList = new ArrayList<>();
        //retrieve members list from referenced group
        db.collection("groups").whereEqualTo("groupName",group.groupName)
                .whereEqualTo("groupSchool",group.groupSchool).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String groupID = new String();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ArrayList<String> groupMembers = (ArrayList<String>) document.get("groupMembers");
                        groupMembersList.addAll(groupMembers);
                        groupMembersList.add(uid);
                        groupID = document.getId();
                        DocumentReference groupDoc = db.collection("groups").document(document.getId());
                        //update field in groupMembers with new group members list
                        groupDoc.update("groupMembers", groupMembersList)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    callBackFunction.done();
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
                    db.collection("users").document(uid).update("groups", FieldValue.arrayUnion(group.groupName));
                } else {
                    Log.d(TAG, "Error getting document: ", task.getException());
                    callBackFunction.error(task.getException());
                }
            }
        });

    }
    static public void leaveGroup(final String uid, final Group group, final CallBackFunction callBackFunction){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ArrayList<String> groupMembersList = new ArrayList<>();
        //retrieve members list from referenced group
        db.collection("groups").whereEqualTo("groupName",group.groupName)
                .whereEqualTo("groupSchool",group.groupSchool).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String groupID = new String();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ArrayList<String> groupMembers = (ArrayList<String>) document.get("groupMembers");
                        int i = 0;
                        for (String id:groupMembers) {
                            if(id.equals(uid))
                                break;
                            i++;
                        }
                        groupMembers.remove(i);
                        groupID = document.getId();
                        groupMembersList.addAll(groupMembers);
                        DocumentReference groupDoc = db.collection("groups").document(document.getId());
                        //update field in groupMembers with new group members list
                        groupDoc.update("groupMembers", groupMembersList)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        callBackFunction.done();
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
                    db.collection("users").document(uid).update("groups", FieldValue.arrayRemove(groupID));
                } else {
                    Log.d(TAG, "Error getting document: ", task.getException());
                    callBackFunction.error(task.getException());
                }
            }
        });

    }

    public static void getGroupsFromReference(List<String> groupReferences, final Vector<Group> groupVector, final MultipleGroupsCallBackFunction multipleGroupsCallBackFunction) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (int i = 0; i < groupReferences.size(); i++) {
            DocumentReference docRef = db.collection("groups").document(groupReferences.get(i));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        String groupName = (String) document.get("groupName");
                        String groupSchool = (String) document.get("groupSchool");
                        String groupCreator = (String) document.get("groupCreator");
                        ArrayList<Date> groupTimesAvailable = new ArrayList<Date>();
                        ArrayList<Map<String, Object>> availableDates = (ArrayList<Map<String, Object>>) document.get("groupTimesAvailable");
                        if (availableDates != null){
                            for (Map<String, Object> date : availableDates){
                                Map<String, Object> innerMap = (Map<String, Object>) date.get("timeOfDay");
                                Long hour = (Long)innerMap.get("hour");
                                Long minute = (Long)innerMap.get("minute");
                                String meridien = innerMap.get("meridiem").toString();
                                String dayOfTheWeek = date.get("dayOfTheWeek").toString();
                                Date newDate = new Date (dayOfTheWeek,hour.intValue(),minute.intValue(), meridien);
                                groupTimesAvailable.add(newDate);
                            }
                            ArrayList<String> groupMembers = (ArrayList<String>) document.get("groupMembers");
                            String groupSubject = (String) document.get("groupSubject");
                            groupVector.add(new Group(groupName, groupSchool, groupCreator, groupTimesAvailable, groupMembers, groupSubject));
                            multipleGroupsCallBackFunction.done(groupVector.size()-1);
                        }
                        if (document.exists()) {
                            Log.d("GROUP", "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d("GROUP", "No such document");
                        }
                    } else {
                        Log.d("GROUP", "get failed with ", task.getException());
                        multipleGroupsCallBackFunction.error(task.getException());
                    }
                }
            });
        }
            /*
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
                                    groupVector.add(new Group(groupName, groupSchool, groupCreator, groupTimesAvailable, groupMembers, groupSubject));
                                    Log.d("GROUP", "getGroupsFromReference result: " + document.getData());
                                }
                                callBackFunction.done();
                            } else {
                                Log.d(TAG, "Error getting document: ", task.getException());
                                callBackFunction.error(task.getException());
                            }
                        }
                    });
        }
        */
    }
    
    public static void getGroupsFromUniversity(final String school, final Vector<Group> groupVector, final CallBackFunction callBackFunction){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("groups").whereEqualTo("groupSchool", school)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String groupName = (String) document.get("groupName");
                                String groupSchool = (String) document.get("groupSchool");
                                String groupCreator = (String) document.get("groupCreator");
                                ArrayList<Date> groupTimesAvailable = new ArrayList<Date>();
                                ArrayList<Map<String, Object>> availableDates = (ArrayList<Map<String, Object>>) document.get("groupTimesAvailable");
                                for (Map<String, Object> date : availableDates){
                                    Map<String, Object> innerMap = (Map<String, Object>) date.get("timeOfDay");
                                    Long hour = (Long)innerMap.get("hour");
                                    Long minute = (Long)innerMap.get("minute");
                                    String meridien = innerMap.get("meridiem").toString();
                                    String dayOfTheWeek = date.get("dayOfTheWeek").toString();
                                    Date newDate = new Date (dayOfTheWeek,hour.intValue(),minute.intValue(), meridien);
                                    groupTimesAvailable.add(newDate);
                                }
                                ArrayList<String> groupMembers = (ArrayList<String>) document.get("groupMembers");
                                String groupSubject = (String) document.get("groupSubject");
                                groupVector.add(new Group(groupName, groupSchool, groupCreator, groupTimesAvailable, groupMembers, groupSubject));
                                Log.d(TAG, document.getId() + " => " + document.get("groupName"));
                            }
                            callBackFunction.done();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            callBackFunction.error(task.getException());
                        }
                    }
                });
    }
}
