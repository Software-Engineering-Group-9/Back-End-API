package com.company;

import java.util.Date;

public class TodoEvent {

    public String title;
    public Date due;
    public float timeNeeded;

    public TodoEvent(String title, Date due, float timeNeeded) {
        this.title = title;
        this.due = due;
        this.timeNeeded = timeNeeded;
    }
}
