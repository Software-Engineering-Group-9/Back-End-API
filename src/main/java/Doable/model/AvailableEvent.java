package Doable.model;

import java.util.Date;

public class AvailableEvent {
    private Date start;
    private Date end;

    public AvailableEvent(Date start, Date end) {
        this.start = start;
        this.end = end;
    }



    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String toString(){
        return start + " - " + end;
    }

    public float GetSize(){
        return Math.abs(end.getTime() - start.getTime()) / (1000 * 60 * 60);
    }



}
