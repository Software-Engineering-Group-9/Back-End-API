package com.company;

import java.util.Date;
public class BusyEvent {
    public String title;
    public Date start;
    public Date end;
    public BusyEvent(String title, Date start, Date end) {
        this.title = title;
        this.start = start;
        this.end = end;
    }
    public String toString(){
        return title + " " + start + " - " + end;
    }
}