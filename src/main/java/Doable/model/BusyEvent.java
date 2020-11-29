package Doable.model;

public class BusyEvent {

    private String aid;
    private String userid;
    private String starttime;
    private String endtime;
    private String color;
    private String title;
    public  BusyEvent(String aid, String title, String userid, String starttime, String endtime, String color) {
        this.aid = aid;
        this.userid = userid;
        this.starttime = starttime;
        this.endtime = endtime;
        this.color = color;
        this.title = title;
    }

    public String getAid(){
        return aid;
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

    public String getColor(){
        return color;
    }

    public String getTitle(){
        return title;
    }

}
