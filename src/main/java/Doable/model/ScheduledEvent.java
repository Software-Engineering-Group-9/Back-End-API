package Doable.model;

import java.util.Calendar;
import java.util.Date;

public class ScheduledEvent {

    private String sid;
    private String userid;
    private String title;
    private String starttime;
    private String endtime;

    public ScheduledEvent(String sid, String userid, String title, String starttime, String endtime) {
        this.sid = sid;
        this.userid = userid;
        this.title = title;
        this.starttime = starttime;
        this.endtime = endtime;
    }

    public ScheduledEvent(String sid, String userid, String title, Date startDate, Date endDate) {
        this.sid = sid;
        this.userid = userid;
        this.title = title;
        this.starttime = buildDateTimeString(startDate);
        this.endtime = buildDateTimeString(endDate);
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


    public String getSid(){
        return sid;
    }

    public String getUserid(){
        return userid;
    }

    public String getStarttime(){
        return  starttime;
    }

    public String getEndtime(){
        return endtime;
    }

}
