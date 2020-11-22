package Doable.model;

public class Event {

    private String eid;
    private String title;
    private String dueDate;
    private String dueTime;
    private int timeNeed;
    private String uuid;

    public Event(String eid, String title, String dueDate, String dueTime, int timeNeed, String uuid){
        this.eid = eid;
        this.title = title;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.timeNeed = timeNeed;
        this.uuid = uuid;

    }

}
