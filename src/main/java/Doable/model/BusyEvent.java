package Doable.model;

public class BusyEvent {

    private String aid;
    private String userid;
    private String starttime;
    private String endtime;
    private String color;
    private String title;

    public  BusyEvent(String sid, String title, String userid, String starttime, String endtime, String color) {
        this.aid = sid;
        this.userid = userid;
        this.starttime = starttime;
        this.endtime = endtime;
        this.color = color;
        this.title = title;
    }


    public BusyEvent(String sid, String userid, String title, Date start, Date end, String color) {
        this.sid = sid;
        this.userid = userid;
        this.title = title;
        this.start = start;
        this.end = end;
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
