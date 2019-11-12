package com.example.study_session;

import java.util.List;

public class Profile {
    String name;
    String school;
    List<String> groups;
    //Maybe consider changing the type to something more fitting
    Date[] timesAvailable;

    public Profile() {
    }

    public Profile(String userName, String userSchool) {
        this.name = name;
        this.school = school;
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

    public List<String> getGroups(){return groups; }
}
