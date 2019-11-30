package com.example.study_session;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Profile {
    String name;
    String school;
    ArrayList<Date> timesAvailable;
    ArrayList<String> groups;

    interface MultipleUserCallBackFunction {
        public void done(int index);
        public void error(Exception e);
    }
    interface UserCallBackFunction {
        public void done(Profile user);
        public void error(Exception e);
    }

    public Profile() {
        name = new String();
        school = new String();
        timesAvailable = new ArrayList<Date>();
        groups = new ArrayList<String>();
    }

    public Profile(String userName, String userSchool, ArrayList<Date> timesAvailable) {
        this.name = userName;
        this.school = userSchool;
        this.timesAvailable = timesAvailable;
    }


    public ArrayList<Date> getTimesAvailable() {
        return timesAvailable;
    }

    /**
     * Default getter for userName
     *
     * @return the userName
     */
    public String getName() {
        return name;
    }

    /**
     * Default getter for userSchool
     *
     * @return the usersSchool
     */
    public String getSchool() {
        return school;
    }

    public ArrayList<String> getGroups(){return groups; }

    public static void getUserFromReference(List<String> userReferences, final Vector<Profile> profileVector, final MultipleUserCallBackFunction multipleUserCallBackFunction) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (int i = 0; i < userReferences.size(); i++) {
            DocumentReference docRef = db.collection("users").document(userReferences.get(i));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Profile newUser = new Profile();
                        newUser.groups = (ArrayList<String>) document.get("groups");
                        newUser.name = (String) document.get("name");
                        newUser.school = (String) document.get("school");
                        ArrayList<Map<String, Object>> availableDates = (ArrayList<Map<String, Object>>) document.get("timesAvailable");
                        for (Map<String, Object> date : availableDates) {
                            Map<String, Object> innerMap = (Map<String, Object>) date.get("timeOfDay");
                            Long hour = (Long) innerMap.get("hour");
                            Long minute = (Long) innerMap.get("minute");
                            String meridien = innerMap.get("meridiem").toString();
                            String dayOfTheWeek = date.get("dayOfTheWeek").toString();
                            Date newDate = new Date(dayOfTheWeek, hour.intValue(), minute.intValue(), meridien);
                            newUser.timesAvailable.add(newDate);
                        }
                        profileVector.add(newUser);
                        multipleUserCallBackFunction.done(profileVector.size()-1);
                    } else {
                        multipleUserCallBackFunction.error(task.getException());
                    }
                }
            });
        }
    }
    public static void getUser(String userReferences, final UserCallBackFunction UserCallBackFunction) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(userReferences);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Profile user = new Profile();
                        user.groups = (ArrayList<String>) document.get("groups");
                        user.name = (String) document.get("name");
                        user.school = (String) document.get("school");
                        ArrayList<Map<String, Object>> availableDates = (ArrayList<Map<String, Object>>) document.get("timesAvailable");
                        for (Map<String, Object> date : availableDates) {
                            Map<String, Object> innerMap = (Map<String, Object>) date.get("timeOfDay");
                            Long hour = (Long) innerMap.get("hour");
                            Long minute = (Long) innerMap.get("minute");
                            String meridien = innerMap.get("meridiem").toString();
                            String dayOfTheWeek = date.get("dayOfTheWeek").toString();
                            Date newDate = new Date(dayOfTheWeek, hour.intValue(), minute.intValue(), meridien);
                            user.timesAvailable.add(newDate);
                        }
                        UserCallBackFunction.done(user);
                    } else {
                        UserCallBackFunction.error(task.getException());
                    }
                }
            });
    }
}
