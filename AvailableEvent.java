package com.company;

import java.util.Date;

public class AvailableEvent {
    public Date start;
    public Date end;

    public AvailableEvent(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public AvailableEvent() {

    }

    public String toString(){
        return start + " - " + end;
    }

}
