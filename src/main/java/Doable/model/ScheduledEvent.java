package Doable.model;

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
