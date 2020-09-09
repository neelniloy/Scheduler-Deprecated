package com.sarker.scheduler;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class RoutineInfo implements Serializable {
    public String courseName,courseCode,courseTeacher,roomNo,classTime,routineKey,alarm,day,randomKey;

    public RoutineInfo(){

    }

    public RoutineInfo(String courseName, String courseCode, String courseTeacher, String roomNo, String classTime, String routineKey, String alarm, String day, String randomKey) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseTeacher = courseTeacher;
        this.roomNo = roomNo;
        this.classTime = classTime;
        this.routineKey = routineKey;
        this.alarm = alarm;
        this.day = day;
        this.randomKey = randomKey;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getClassTime() {
        return classTime;
    }

    public String getRoutineKey() {
        return routineKey;
    }

    public String getAlarm() {
        return alarm;
    }

    public String getDay() {
        return day;
    }

    public String getRandomKey() {
        return randomKey;
    }

    @Exclude
    public void setRoutineKey(String Key) {
        this.routineKey = Key;
    }
}
