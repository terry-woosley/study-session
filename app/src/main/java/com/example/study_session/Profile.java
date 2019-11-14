package com.example.study_session;

import java.util.ArrayList;

public class Profile {
    String name;
    String school;
    ArrayList<Date> timesAvailable;
    ArrayList<String> groups;

    public Profile() {
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
}
