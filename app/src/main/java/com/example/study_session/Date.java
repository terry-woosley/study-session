package com.example.study_session;

public class Date {
    public class Time{
        public int hour;
        public int minute;

        public Time(int hour, int minute){
            this.hour = hour;
            this.minute = minute;
        }
    }

    public String dayOfTheWeek;
    public Time timeOfDay;

    //Constructor
    public Date(String day, int hour, int minute){
        this.dayOfTheWeek = day;
        this.timeOfDay = new Time(hour, minute);
    }

    public boolean equals(Date otherDate){
        if(otherDate == null) return false;
        if(dayOfTheWeek != otherDate.dayOfTheWeek) return false;
        if(timeOfDay.hour != otherDate.timeOfDay.hour) return false;
        if(timeOfDay.minute != otherDate.timeOfDay.minute) return false;
        return true;
    }

    public String dateToText(){
        return dayOfTheWeek + " " + timeOfDay.hour + ":" + timeOfDay.minute;
    }
}
