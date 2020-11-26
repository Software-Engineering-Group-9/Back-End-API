package Doable.model;

public class ScheduledEvent {

    private String sid;
    private String userid;
    private String starttime;
    private String endtime;

    public ScheduledEvent(String sid, String userid, String starttime, String endtime) {
        this.sid = sid;
        this.userid = userid;
        this.starttime = starttime;
        this.endtime = endtime;
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
