package com.example.shuvam.todolist;

import java.util.Calendar;

public class Task {
    String task;
    String taskSummary;
    String date,time;

    Long id;
    public Task(String task, String taskSummary,String date,String time){
        this.task=task;
        this.taskSummary=taskSummary;
        this.date=date;
        this.time=time;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTaskSummary() {
        return taskSummary;
    }

    public void setTaskSummary(String taskSummary) {
        this.taskSummary = taskSummary;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
