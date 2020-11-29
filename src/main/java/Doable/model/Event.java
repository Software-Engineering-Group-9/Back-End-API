package Doable.model;

public class Event {

    private String eid;
    private String title;
    private String dueDate;
    private String dueTime;
    private int timeNeed;
    private String uuid;

    public String getEid() {
        return eid;
    }

    public String getTitle() {
        return title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getDueTime() {
        return dueTime;
    }

    public int getTimeNeed() {
        return timeNeed;
    }

    public String getUuid() {
        return uuid;
    }

    public Event(String eid, String title, String dueDate, String dueTime, int timeNeed, String uuid){
        this.eid = eid;
        this.title = title;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.timeNeed = timeNeed;
        this.uuid = uuid;

    }

    public Event(String eid,  String uuid, String title, Date dueDate, int timeNeed, String color){
        this.eid = eid;
        this.title = title;
        this.dueDate = buildDateString(dueDate);
        this.dueTime = buildTimeString(dueDate);
        this.timeNeed = timeNeed;
        this.uuid = uuid;


        System.out.println("New todo: " + dueDate);
        System.out.println(this.dueDate + this.dueTime);
    }

    public String buildDateString(Date date){
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
        ret += calendar.get(Calendar.DAY_OF_MONTH) + " ";
        return ret;
    }

    public String buildTimeString(Date time){
        String ret = "";
        if(time.getHours() < 10) {
            ret += "0";
        }
        ret += time.getHours() + ":";
        if(time.getMinutes() < 10){
            ret += "0";
        }
        ret += time.getMinutes() + ":";
        if(time.getSeconds() < 10){
            ret += "0";
        }
        ret += time.getSeconds();
        return ret;
    }

    public Date getDueAsDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date todoDueDate =  new Date();
        try{
            todoDueDate = df.parse(this.dueDate + " " + this.dueTime);
        }catch(Exception e){
            System.err.println(e + " - Invalid Date Format!");
        }
        return todoDueDate;
    }


}
