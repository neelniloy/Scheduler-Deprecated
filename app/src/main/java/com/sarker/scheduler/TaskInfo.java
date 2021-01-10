package com.sarker.scheduler;

import java.io.Serializable;

public class TaskInfo implements Serializable {

    public String title,details,time,date,status,key;

    public TaskInfo(){

    }

    public TaskInfo(String title, String details, String time, String date, String status, String key) {
        this.title = title;
        this.details = details;
        this.time = time;
        this.date = date;
        this.status = status;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
