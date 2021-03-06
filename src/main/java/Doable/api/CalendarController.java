package Doable.api;

import Doable.RowMapper.BusyEventRowMapper;
import Doable.RowMapper.ScheduledEventRowMapper;
import Doable.RowMapper.EventRowMapper;
import Doable.model.AvailableEvent;
import Doable.model.BusyEvent;
import Doable.model.TodoEvent;
import Doable.model.ScheduledEvent;
import Doable.service.CreateTableService;
import Doable.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static Doable.api.Endpoint.*;
import static Doable.api.SQLCommand.*;

@RequestMapping("api/v1/calendar")
@RestController
@CrossOrigin
public class CalendarController {


    private final CreateTableService createTableService;

    private final JdbcTemplate jdbcTemplate;

    private final JwtTokenService jwtTokenService;

    public CalendarController(JdbcTemplate jdbcTemplate, JwtTokenService jwtTokenService, CreateTableService createTableService) {
        this.jdbcTemplate = jdbcTemplate;
        this.jwtTokenService = jwtTokenService;
        this.createTableService = createTableService;
    }

    /**
     * Create user event
     *
     * @param EventInfo event information
     * @param request   http request information
     */
    @PostMapping(CREATE_EVENT)
    public void addEvent(@Valid @NotNull @RequestBody String EventInfo, HttpServletRequest request) {
        createTableService.createTodoTable(todoEvent);
        JSONObject jObject = new JSONObject(EventInfo);
        if (jdbcTemplate.update(EVENT_INSERT, jObject.getString("id"),
                jObject.getString("title"),
                jObject.getString("dueDate"),
                jObject.getString("dueTime"),
                jObject.getInt("timeNeeded"),
                jwtTokenService.getSubjectFromToken(getToken(request))) != 1)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Our backend devs suck");
    }

    /**
     * Create user availability
     *
     * @param EventInfo event information
     * @param request   http request information
     */
    @PostMapping(CREATE_AVAILABILITY)
    public void addAvailability(@Valid @NotNull @RequestBody String EventInfo, HttpServletRequest request) {
        createTableService.createBusyTable(busyScheduledEvent);
        JSONObject jObject = new JSONObject(EventInfo);
        if (jdbcTemplate.update(BUSY_INSERT, jObject.getString("id"),
                jObject.getString("title"),
                jObject.getString("bgColor"),
                jObject.getString("start"),
                jObject.getString("end"),
                jwtTokenService.getSubjectFromToken(getToken(request))) != 1)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Our backend devs suck");

    }

    /**
     * todo: create a method to convert busy schedules to available schedules
     * todo: create a method that optimally put todo event into the scheduled event
     * assigned to : Brandon and Salvador
     *
     * @param request
     */
    @PostMapping(OPTIMIZE)
    public void optimize(HttpServletRequest request) {
        createTableService.createBusyTable(busyScheduledEvent);
        createTableService.createScheudledEventTable(scheduledEvent);
        createTableService.createTodoTable(todoEvent);

        String subject = jwtTokenService.getSubjectFromToken(getToken(request));

        List<TodoEvent> todoList = jdbcTemplate.query(TODO_QUERY_BY_UUID
                , new Object[]{subject}
                , new EventRowMapper());

        List<BusyEvent> busyList = jdbcTemplate.query(BUSY_QUERY_BY_UUID
                , new Object[]{subject}
                , new BusyEventRowMapper());

        ArrayList<TodoEvent> todoEvents = new ArrayList<>();
        todoEvents.addAll(todoList);
        ArrayList<BusyEvent> busyEvents = new ArrayList<>();


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        //create sample to do and busyevents due between 11-22 and 11-23
        try {
            todoEvents.add(new TodoEvent("1", "1", "HW1", df.parse("2020-11-27 23:59"), 5, "#AAAAAA"));
            todoEvents.add(new TodoEvent("1", "2", "HW2", df.parse("2020-11-27 20:00"), 1, "#AAAAAA"));
            todoEvents.add(new TodoEvent("1", "3", "HW3", df.parse("2020-11-27 12:00"), 1, "#AAAAAA"));

            todoEvents.add(new TodoEvent("1", "4", "HW4", df.parse("2020-11-28 20:00"), 1, "#AAAAAA"));
            todoEvents.add(new TodoEvent("1", "5", "HW5", df.parse("2020-11-28 23:59"), 6, "#AAAAAA"));

            todoEvents.add(new TodoEvent("1", "6", "HW6", df.parse("2020-11-29 20:00"), 1, "#AAAAAA"));
            todoEvents.add(new TodoEvent("1", "7", "HW7", df.parse("2020-11-29 23:59"), 6, "#AAAAAA"));

            todoEvents.add(new TodoEvent("1", "8", "HW8", df.parse("2020-11-30 20:00"), 1, "#AAAAAA"));
            todoEvents.add(new TodoEvent("1", "9", "HW9", df.parse("2020-11-30 23:59"), 6, "#AAAAAA"));

            busyEvents.add(new BusyEvent("1", "2", "sleep",   df.parse("2020-11-27 00:00"),   df.parse("2020-11-27 08:00"), "#BBBBBB")); //sleep 0-8am
            busyEvents.add(new BusyEvent("1", "2", "work",    df.parse("2020-11-27 9:00"),    df.parse("2020-11-27 17:00"), "#BBBBBB")); //work 9am-5pm
            busyEvents.add(new BusyEvent("1", "2", "sleep",   df.parse("2020-11-28 00:00"),   df.parse("2020-11-28 08:00"), "#BBBBBB"));// sleep 0-8am
            busyEvents.add(new BusyEvent("1", "2", "work",    df.parse("2020-11-28 9:00"),    df.parse("2020-11-28 12:00"), "#BBBBBB")); //work 9am-12pm
            busyEvents.add(new BusyEvent("1", "2", "sleep",   df.parse("2020-11-29 00:00"),   df.parse("2020-11-29 08:00"), "#BBBBBB")); //sleep 0-8am
            busyEvents.add(new BusyEvent("1", "2", "work",    df.parse("2020-11-29:00"),      df.parse("2020-11-29 17:00"), "#BBBBBB")); //work 9am-5pm
            busyEvents.add(new BusyEvent("1", "2", "sleep",   df.parse("2020-11-30 00:00"),   df.parse("2020-11-30 08:00"), "#BBBBBB"));// sleep 0-8am
            busyEvents.add(new BusyEvent("1", "2", "work",    df.parse("2020-11-30 9:00"),    df.parse("2020-11-30 12:00"), "#BBBBBB")); //work 9am-12pm
        }catch(Exception e){
            System.out.println(e);
        }

        System.out.println("\n\nAvailable Events: ");
        ArrayList<AvailableEvent> availableEvents = GetAvailability(busyEvents, todoEvents);
        for(AvailableEvent availableEvent : availableEvents){
            System.out.println(availableEvent.toString());
        }

        //call Scheduler function to get managed schedule
        ArrayList<ScheduledEvent> scheduledEvents = Scheduler(todoEvents, busyEvents, subject);
        System.out.println(scheduledEvents.size());
        for(ScheduledEvent scheduledEvent : scheduledEvents){
            System.out.println(scheduledEvent);
        }
    }

    public ArrayList<ScheduledEvent> Scheduler(ArrayList<TodoEvent> todoEvents, ArrayList<BusyEvent> busyEvents, String subject){
        SortTodoEvents(todoEvents);

        ArrayList<ScheduledEvent> scheduledEvents = new ArrayList<>();
        List<AvailableEvent> availableEvents;
        List<AvailableEvent> tempAvailableEvents;

        for(TodoEvent todoEvent : todoEvents){

            if(todoEvent.isBefore(new Date())){
                System.err.println("Event past due!");
            }

            availableEvents = GetAvailability(busyEvents, todoEvents);
            tempAvailableEvents = new ArrayList<>();

            //files tempAvailableEvents list with possible times for each todoEvent
            for(AvailableEvent availableEvent : availableEvents){
                if(availableEvent.getStart().before(todoEvent.getDueAsDate())){
                    if(availableEvent.getEnd().before(todoEvent.getDueAsDate())){
                        tempAvailableEvents.add(availableEvent);
                    }else{
                        tempAvailableEvents.add(new AvailableEvent(availableEvent.getStart(), todoEvent.getDueAsDate()));
                    }
                }
            }

            //if no availables exist for todoEvent
            if(tempAvailableEvents.size() == 0){
                continue;
            }

            float hoursAvailable = 0;
            for(AvailableEvent availableEvent : tempAvailableEvents){
                hoursAvailable += availableEvent.GetSize();
            }

            float hrsPerEvent = todoEvent.getTimeNeed() / hoursAvailable;

            //not enough time!
            if(todoEvent.getTimeNeed() > hoursAvailable){
                System.err.println("Not enough time Available for event! Event: " + todoEvent.getTitle() + " Needed: " + todoEvent.getTimeNeed() + " HoursAvailable: " + hoursAvailable);
            }

            float hrsLeft = todoEvent.getTimeNeed();
            for(AvailableEvent availableEvent : tempAvailableEvents){
                float hrsThisEvent = availableEvent.GetSize()*hrsPerEvent;
                if(hrsThisEvent % 0.5 != 0){
                    hrsThisEvent += (0.5 - (hrsThisEvent % 0.5));
                }

                Calendar cal = Calendar.getInstance();
                cal.setTime(availableEvent.getStart());
                cal.add(Calendar.MINUTE, (int) (hrsThisEvent * 60));
                Date end = cal.getTime();

                if(hrsThisEvent == 0) continue;

                hrsLeft -= GetDifferenceInHours(availableEvent.getStart(), end);
                if(hrsLeft < 0){
                    cal.add(Calendar.MINUTE, - (int) (Math.abs(hrsLeft * 60)));
                    end = cal.getTime();
                }

                scheduledEvents.add(new ScheduledEvent(shortUUID(), subject,  todoEvent.getTitle(), availableEvent.getStart(), end,"#BBBBBB" ));
                busyEvents.add(new BusyEvent(subject, shortUUID(), todoEvent.getTitle(), availableEvent.getStart(), end, "#BBBBBB"));
            }
        }
        return scheduledEvents;
    }

    public float GetDifferenceInHours(Date d1, Date d2){
        float difference = Math.abs(d2.getTime() - d1.getTime());
        return difference / (1000 * 60 * 60);
    }

    //bubble sorts todoEvents by dueDate where the earlier dueDate appear earlier in the sorted list
    public ArrayList<TodoEvent> SortTodoEvents(ArrayList<TodoEvent> todoEvents) {
        for (int i = 0; i < todoEvents.size(); i++) {
            for (int j = 1; j < todoEvents.size() - i; j++) {
                if (todoEvents.get(j - 1).getDueAsDate().after(todoEvents.get(j).getDueAsDate())){
                    TodoEvent tempEvent = todoEvents.get(j - 1);
                    todoEvents.set(j - 1, todoEvents.get(j));
                    todoEvents.set(j, tempEvent);
                }
            }
        }
        return todoEvents;
    }

    //bubble sorts todoEvents by dueDate where the earlier dueDate appear earlier in the sorted list
    public void SortBusyEvents(ArrayList<BusyEvent> busyEvents) {
        for (int i = 0; i < busyEvents.size(); i++) {
            for (int j = 1; j < busyEvents.size() - i; j++) {
                if (busyEvents.get(j - 1).getStart().after(busyEvents.get(j).getStart())){
                    BusyEvent tempEvent = busyEvents.get(j - 1);
                    busyEvents.set(j - 1, busyEvents.get(j));
                    busyEvents.set(j, tempEvent);
                }
            }
        }
    }

    /**
     * Converts the list of busy events from the user into a list of availability for use by the scheduler method
     * @param busyEvents is a list of all times when the user cannot complete a task
     * @return is the list of all time the user has not marked themselves busy
     */
    public ArrayList<AvailableEvent> GetAvailability(ArrayList<BusyEvent> busyEvents, ArrayList<TodoEvent> todoEvents){
        ArrayList<AvailableEvent> availableEvents = new ArrayList<>();

        //set now to next quarter hour
        Date tempDate = new Date();
        tempDate = Date.from(tempDate.toInstant().truncatedTo(ChronoUnit.MINUTES));
        if(tempDate.getMinutes() % 15 != 0){
            tempDate.setMinutes(tempDate.getMinutes() + (15 - (tempDate.getMinutes() % 15)));
        }

        if (busyEvents.isEmpty()){
            availableEvents.add(new AvailableEvent(tempDate, todoEvents.get(0).getDueAsDate()));
            return availableEvents;
        }

        SortBusyEvents(busyEvents);

        for(BusyEvent busyEvent : busyEvents){
            if(busyEvent.IsPastBusyEvent()) continue;
            //if the current gap is longer than 30 minutes, add the event
            if(IsValidFutureEvent(tempDate, busyEvent.getStart())){
                availableEvents.add(new AvailableEvent(tempDate, GetEndFromEventStart(busyEvent.getStart())));
            }
            //System.out.println("now set from " + now + " to " + busyEvent.end);
            tempDate = busyEvent.getEnd();

        }
        if(todoEvents.size() > 0 && IsValidFutureEvent(tempDate, todoEvents.get(todoEvents.size() - 1).getDueAsDate())){
            availableEvents.add(new AvailableEvent(tempDate, GetEndFromEventStart(todoEvents.get(todoEvents.size() - 1).getDueAsDate())));
        }
        return availableEvents;
    }

    public Date GetEndFromEventStart(Date eventStart){
        if(eventStart.getMinutes() == 59) return eventStart;
        Calendar cal = Calendar.getInstance();
        cal.setTime(eventStart);
        cal.add(Calendar.MINUTE, -1);
        return cal.getTime();
    }

    public boolean IsValidFutureEvent(Date tempDate, Date eventDate){
        //System.out.println((now.before(eventDate) && ChronoUnit.MINUTES.between(now.toInstant(), eventDate.toInstant()) >= 30) + " --- " + now + " --- " + eventDate);
        return (tempDate.before(eventDate) && ChronoUnit.MINUTES.between(tempDate.toInstant(), eventDate.toInstant()) >= 30);
    }

    /**
     * Add a scheduled event to the database
     *
     * @param sid id of the scheduled event
     * @param title title of the event
     * @param color color of the event (on the calendar)
     * @param starttime start time of the event (includes date)
     * @param endtime end time of the event (includes date)
     * @param userid userid id, the event belongs to 
     */
    public void addScheduledEvent(String sid, String title, String color, String starttime, String endtime, String userid) {
        createTableService.createScheudledEventTable(scheduledEvent);
        jdbcTemplate.update(SCHEDULED_EVENT_INSERT, sid, title, color, starttime, endtime, userid);
    }

    /**
     * Get the list of scheudled events based on the userid
     *
     * @param request http request information
     * @return list of scheduled
     */
    @GetMapping(GET_SCHEDULED_EVENT)
    public String getScheduledEvent(HttpServletRequest request) {

        List<ScheduledEvent> list = jdbcTemplate.query(SCHEDULED_EVENT_QUERY_BY_UUID
                , new Object[]{jwtTokenService.getSubjectFromToken(getToken(request))}
                , new ScheduledEventRowMapper());
        return convertListToJson(list);

    }

    /**
     * get the list of busyEvent by userid
     *
     * @param request HTTP request information
     * @return list of todoEvent in JSON string
     */
    @GetMapping(GET_BUSY_EVENT)
    public String getBusyEvent(HttpServletRequest request){
        List<BusyEvent> list = jdbcTemplate.query(BUSY_QUERY_BY_UUID
                , new Object[]{jwtTokenService.getSubjectFromToken(getToken(request))}
                , new BusyEventRowMapper());
        return convertListToJson(list);

    }

    /**
     * Get the list of todoEvent by userid
     *
     * @param request HTTP request information
     * @return list of todoEvent in JSON string
     */
    @GetMapping(GET_TODO_EVENT)
    public String getTodoEvent(HttpServletRequest request){
        List<TodoEvent> list = jdbcTemplate.query(TODO_QUERY_BY_UUID
                , new Object[]{jwtTokenService.getSubjectFromToken(getToken(request))}
                , new EventRowMapper());

        return convertListToJson(list);
    }

    /**
     * Convert list of objects to JSON String
     *
     * @param list list of any objects
     * @return JSON String of all the object in the list
     */
    String convertListToJson(List<?> list){
        JSONObject obj = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < list.size(); i++) {
            String a;
            try {
                a = mapper.writeValueAsString(list.get(i)).replaceAll("\"", "'");

            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Our backend dev sucks");
            }
            obj.put(Integer.toString(i + 1), a);
        }
        return obj.toString().replaceAll("\\\\", "");
    }


    /**
     * Get authentication token
     *
     * @param request HTTP request information
     * @return token from the HTTP request header
     */
    String getToken(HttpServletRequest request) {
        final String PREFIX = "Bearer ";
        final String HEADER = "Authorization";
        return request.getHeader(HEADER).replace(PREFIX, "");
    }

    /**
     * Test method used for testing http request
     *
     * @param request request info
     */
    @PostMapping("/hello")

    public void Hello(HttpServletRequest request) {
    }

    /**
     * Create a UUID of length 13
     *
     * @return newly created uui
     */
    public static String shortUUID() {
        UUID uuid = UUID.randomUUID();
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        return Long.toString(l, Character.MAX_RADIX);
    }

}