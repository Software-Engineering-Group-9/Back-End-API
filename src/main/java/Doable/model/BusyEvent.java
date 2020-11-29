package Doable.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BusyEvent {

    private String aid;
    private String userid;
    private String color;
    private String title;
    private String start;
    private String end;

    public  BusyEvent(String sid, String title, String userid, String starttime, String endtime, String color) {
        this.aid = sid;
        this.userid = userid;
        this.start = starttime;
        this.end = endtime;
        this.color = color;
        this.title = title;
    }


    public BusyEvent(String sid, String userid, String title, Date start, Date end, String color) {
        this.aid = sid;
        this.userid = userid;
        this.title = title;
        this.start = buildDateTimeString(start);
        this.end = buildDateTimeString(end);
        this.color = color;
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

    public Date getStartDate(){
        return getDateFromString(this.start);
    }

    public Date getEndDate(){
        return getDateFromString(this.end);
    }

    /**
     * Returns true iff both the event start and end dates are in the past
     * now is the current time
     */
    public boolean IsPastBusyEvent(){
        Date now = new Date(); //initialize to current time
        return (getStartDate().before(now) && getEndDate().before(now));
    }

    public String getAid(){
        return aid;
    }

    public String getUserid(){
        return userid;
    }

    public String getStarttime(){
        return  start;
    }

    public String getEndtime(){
        return end;
    }

    public String getColor(){
        return color;
    }

    public String getTitle(){
        return title;
    }

}
