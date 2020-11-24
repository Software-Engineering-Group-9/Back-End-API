package com.company;

import java.util.Date;

public class CalendarEvent {

    public String title;
    public Date start;
    public Date end;

    public CalendarEvent(String title, Date start, Date end) {
        this.title = title;
        this.start = start;
        this.end = end;
    }
}
