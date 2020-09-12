package com.sarker.scheduler;


import java.io.Serializable;

public class RoutineInfo implements Serializable {
    public String courseName,courseCode,courseTeacher,roomNo,classTime,routineKey,day;
    public int randomKey;

    public RoutineInfo(){

    }

    public RoutineInfo(String courseName, String courseCode, String courseTeacher, String roomNo, String classTime, String routineKey, String day, int randomKey) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseTeacher = courseTeacher;
        this.roomNo = roomNo;
        this.classTime = classTime;
        this.routineKey = routineKey;
        this.day = day;
        this.randomKey = randomKey;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(String courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public String getRoutineKey() {
        return routineKey;
    }

    public void setRoutineKey(String routineKey) {
        this.routineKey = routineKey;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getRandomKey() {
        return randomKey;
    }

    public void setRandomKey(int randomKey) {
        this.randomKey = randomKey;
    }
}
