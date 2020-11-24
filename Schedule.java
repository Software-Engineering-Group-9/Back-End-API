package com.company;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Schedule {
    public ArrayList<TodoEvent> todoEvents = new ArrayList<>();
    public ArrayList<BusyEvent> busyEvents = new ArrayList<>();
    public ArrayList<CalendarEvent> calendarEvents = new ArrayList<>();
    public Schedule() {
    }
    public ArrayList<CalendarEvent> Scheduler(){
        //SortTodos();
        ArrayList<AvailableEvent> availableEvents = GetAvailability(busyEvents);
        if(availableEvents.size() == 0) System.out.println("Empty List");
        for(AvailableEvent availableEvent : availableEvents){
            System.out.println(availableEvent.toString());
        }
        return calendarEvents;
    }

    //bubble sorts todoEvents by dueDate where the earlier dueDate appear earlier in the sorted list
    public void SortTodos() {
        for (int i = 0; i < todoEvents.size(); i++) {
            for (int j = 1; j < todoEvents.size() - i; j++) {
                if (todoEvents.get(j - 1).due.after(todoEvents.get(j).due)){
                    TodoEvent tempEvent = todoEvents.get(j - 1);
                    todoEvents.set(j - 1, todoEvents.get(j));
                    todoEvents.set(j, tempEvent);
                }
            }
        }
    }

    public boolean OnSameDay(Date d1, Date d2){
        d1 = Date.from(d1.toInstant().truncatedTo(ChronoUnit.DAYS));
        d2 =  Date.from(d2.toInstant().truncatedTo(ChronoUnit.DAYS));
        return d1 == d2;
    }

    /**
     * Converts the list of busy events from the user into a list of availability for use by the scheduler method
     * @param busyEvents is a list of all times when the user cannot complete a task
     * @return is the list of all time the user has not marked themselves busy
     */
    public ArrayList<AvailableEvent> GetAvailability(ArrayList<BusyEvent> busyEvents){
        ArrayList<AvailableEvent> availableEvents = new ArrayList<>();
        if (busyEvents.isEmpty()){
            return(availableEvents);
        }
        Date now = new Date();
        now = Date.from(now.toInstant().truncatedTo(ChronoUnit.MINUTES));
        while(now.getMinutes() % 15 != 0){
            now.setMinutes(now.getMinutes() + 1);
        }

        for(BusyEvent busyEvent : busyEvents){
            if(IsAfterBusyEvent(busyEvent, now)){
                continue;
            }else if(IsDuringBusyEvent(busyEvent, now)){
                now = busyEvent.end;
            }else if(IsBeforeBusyEvent(busyEvent, now)){
                if(ChronoUnit.MINUTES.between(now.toInstant(), busyEvent.start.toInstant()) >= 30){
                    if(busyEvent.start.getHours() == 0 && busyEvent.start.getMinutes() == 0){
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(busyEvent.start);
                        cal.add(Calendar.MINUTE, -1);
                        Date start = cal.getTime();
                        availableEvents.add(new AvailableEvent(now, start));

                    }else{
                        availableEvents.add(new AvailableEvent(now, busyEvent.start));
                    }
                    now = busyEvent.end;
                }
            }
        }
        availableEvents.add(new AvailableEvent(now, todoEvents.get(todoEvents.size() - 1).due));
        return (availableEvents);
    }

    public boolean IsAfterBusyEvent(BusyEvent busyEvent, Date now){
        return (busyEvent.start.before(now)) && (busyEvent.end.before(now));
    }

    public boolean IsDuringBusyEvent(BusyEvent busyEvent, Date now){
        return busyEvent.start.before(now) && busyEvent.end.after(now);
    }

    public boolean IsBeforeBusyEvent(BusyEvent busyEvent, Date now) {
        return (busyEvent.start.after(now) && (busyEvent.end.after(now)));
    }
}