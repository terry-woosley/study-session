package com.example.study_session;

public class Date {
    public class Time{
        public int hour;
        public int minute;
        public String meridiem;

        public Time(int hour, int minute, String meridiem){
            this.hour = hour;
            this.minute = minute;
            this.meridiem  = meridiem;
        }
    }

    public String dayOfTheWeek;
    public Time timeOfDay;

    //Constructor
    public Date(String day, int hour, int minute, String meridiem){
        this.dayOfTheWeek = day;
        this.timeOfDay = new Time(hour, minute, meridiem);
    }

    public boolean equals(Date otherDate){
        if(otherDate == null) return false;
        if(!dayOfTheWeek.equals(otherDate.dayOfTheWeek)) return false;
        if(timeOfDay.hour != otherDate.timeOfDay.hour) return false;
        if(timeOfDay.minute != otherDate.timeOfDay.minute) return false;
        if(!timeOfDay.meridiem.equals(otherDate.timeOfDay.meridiem)) return false;
        return true;
    }

    public String dateToText(){
        return dayOfTheWeek + " " + timeOfDay.hour + ":" + timeOfDay.minute + " " + timeOfDay.meridiem;
    }
}
