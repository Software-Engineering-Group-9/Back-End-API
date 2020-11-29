package Doable.model;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class BusyEvent {

    private String sid;
    private String userid;
    private String title;
    private Date start;
    private Date end;
    private String color;

    public BusyEvent(String sid, String userid, String title, Date start, Date end, String color) {
        this.sid = sid;
        this.userid = userid;
        this.title = title;
        this.start = start;
        this.end = end;
        this.color = color;
    }

    public BusyEvent(String sid, String userid, String title, String start, String end, String color) {
        this.sid = sid;
        this.userid = userid;
        this.title = title;
        this.start = getDateFromString(start);
        this.end = getDateFromString(end);
        this.color = color;
    }

    public Date getDateFromString(String date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date todoDueDate =  new Date();

        String[] dateArr = date.split("T");
        try{
            todoDueDate = df.parse(dateArr[0] + " " + dateArr[1]);
        }catch(Exception e){
            System.err.println(e + " - Invalid Date Format!");
        }
        return todoDueDate;
    }

    /**
     * Returns true iff both the event start and end dates are in the past
     * now is the current time
     */
    public boolean IsPastBusyEvent(){
        Date now = new Date(); //initialize to current time
        return (this.start.before(now) && this.end.before(now));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return title + " " + start + " - " + end;
    }
}