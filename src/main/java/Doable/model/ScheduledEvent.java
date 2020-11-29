package Doable.model;

import java.util.Calendar;
import java.util.Date;

public class ScheduledEvent {

    private String sid;
    private String userid;
    private String starttime;
    private String endtime;
    private String color;
    private String title;

    public ScheduledEvent(String title, String sid, String userid, String starttime, String endtime, String color) {
        this.sid = sid;
        this.userid = userid;
        this.starttime = starttime;
        this.endtime = endtime;
        this.color = color;
        this.title = title;
    }

    public ScheduledEvent(String sid, String userid, String title, Date startDate, Date endDate, String color) {
        this.sid = sid;
        this.userid = userid;
        this.title = title;
        this.starttime = buildDateTimeString(startDate);
        this.endtime = buildDateTimeString(endDate);
        this.color = color;
    }

    public String toString(){
        return title + " " + starttime + " - " + endtime;
    }

    public String buildDateTimeString(Date date){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String ret = "";

        ret += calendar.get(Calendar.YEAR) + "-";

        if(calendar.get(Calendar.MONTH) + 1 < 10){
            ret += "0";
        }
        ret += calendar.get(Calendar.MONTH) + 1 + "-";
        if(calendar.get(Calendar.DAY_OF_MONTH) < 10){
            ret += "0";
        }
        ret += calendar.get(Calendar.DAY_OF_MONTH) + "T";


        if(date.getHours() < 10) {
            ret += "0";
        }
        ret += date.getHours() + ":";
        if(date.getMinutes() < 10){
            ret += "0";
        }
        ret += date.getMinutes() + ":";
        if(date.getSeconds() < 10){
            ret += "0";
        }
        ret += date.getSeconds();
        return ret;
    }

    public String getSid() {
        return sid;
    }

    public String getUserid() {
        return userid;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

}
