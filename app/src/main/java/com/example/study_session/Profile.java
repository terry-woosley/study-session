package com.example.study_session;

public class Profile {
    String name;
    String school;
    String[] groups;
    //Maybe consider changing the type to something more fitting
    Date[] timesAvailable;

    public Profile() {
    }

    public Profile(String name, String school) {
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
}
