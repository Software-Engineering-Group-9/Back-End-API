package com.company; /**
 * Main function used to test Schedule object and Scheduler function
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //instantiate new calendar with empty lists
        Schedule calendar = new Schedule();


        //create sample to do and busyevents due between 11-22 and 11-23
        try {
            //todoEvents for 11-22
            calendar.todoEvents.add(new TodoEvent("HW1", df.parse("2020-11-24 23:59"), 1));
            calendar.todoEvents.add(new TodoEvent("HW2", df.parse("2020-11-24 20:00"), 1));
            calendar.todoEvents.add(new TodoEvent("HW3", df.parse("2020-11-24 12:00"), 1));
            //todoEvents for 11-23
            calendar.todoEvents.add(new TodoEvent("HW4", df.parse("2020-11-25 20:00"), 1));
            calendar.todoEvents.add(new TodoEvent("HW5", df.parse("2020-11-25 23:59"), 1));

            //busyEvents for 11-22
            calendar.busyEvents.add(new BusyEvent("sleep", df.parse("2020-11-24 00:00"), df.parse("2020-11-24 8:00"))); //sleep 0-8am
            calendar.busyEvents.add(new BusyEvent("work", df.parse("2020-11-24 9:00"), df.parse("2020-11-24 17:00"))); //work 9am-5pm
            //busyEvents for 11-23
            calendar.busyEvents.add(new BusyEvent("sleep", df.parse("2020-11-25 00:00"), df.parse("2020-11-25 8:00")));// sleep 0-8am
            calendar.busyEvents.add(new BusyEvent("work", df.parse("2020-11-25 9:00"), df.parse("2020-11-25 12:00"))); //work 9am-12pm
        }catch(Exception e){
            System.out.println(e);
        }

        //call Scheduler function to get managed schedule
        ArrayList<CalendarEvent> calendarEvents = calendar.Scheduler();
        System.out.println(calendarEvents);
    }
}
