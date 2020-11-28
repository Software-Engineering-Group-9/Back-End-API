package Doable.api;

import Doable.RowMapper.InfoRowMapper;
import Doable.RowMapper.ScheduledEventRowMapper;
import Doable.RowMapper.EventRowMapper;
import Doable.model.Event;
import Doable.model.Info;
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

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
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
                jObject.getString("color"),
                jObject.getString("duedate"),
                jObject.getString("duetime"),
                jObject.getInt("timeneed"),
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
        createTableService.createAvailabilityTable(busyScheduledEvent);
        JSONObject jObject = new JSONObject(EventInfo);
        if (jdbcTemplate.update(BUSY_INSERT, jObject.getString("id"),
                jObject.getString("title"),
                jObject.getString("color"),
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
        System.out.println(jwtTokenService.getSubjectFromToken(getToken(request)));
        Collection<Event> events = jdbcTemplate.query(EVENT_QUERY_BY_UUID, new Object[]{jwtTokenService.getSubjectFromToken(getToken(request))}, new EventRowMapper());
        events.forEach(e -> System.out.println(e.toString()));
    }


    public void addScheudledEvent(String sid, String start_time, String end_time, String uuid) {
        createTableService.createScheudledEventTable(scheduledEvent);
        jdbcTemplate.update(SCHEDULED_EVENT_INSERT, sid, uuid, start_time, end_time);
    }

    /**
     * Get the list of scheudled events based on the userid
     *
     * @param request http request information
     * @return list of scheduled
     */
    @GetMapping(GET_SCHEDULED_EVENT)
    public String getScheduledEvent(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        List<ScheduledEvent> list = jdbcTemplate.query(SCHEDULED_EVENT_QUERY_BY_UUID
                , new Object[]{jwtTokenService.getSubjectFromToken(getToken(request))}
                , new ScheduledEventRowMapper());
        for (int i = 0; i < list.size(); i++) {
            String a;
            try {
                a = mapper.writeValueAsString(list.get(i)).replaceAll("\"", "'");

            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Our backend dev sucks");
            }
            obj.put(Integer.toString(i + 1), a);
        }

        return obj.toString().replaceAll("\\\\", "").replaceAll("\"", "");

    }


    @GetMapping(GET_TODO_EVENT)
    public String getTodoEvent(HttpServletRequest request){
        JSONObject obj = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        List<ScheduledEvent> list = jdbcTemplate.query(SCHEDULED_TODO_EVENT_QUERY_BY_UUID
                , new Object[]{jwtTokenService.getSubjectFromToken(getToken(request))}
                , new ScheduledEventRowMapper());
        for (int i = 0; i < list.size(); i++) {
            String a;
            try {
                a = mapper.writeValueAsString(list.get(i)).replaceAll("\"", "'");

            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Our backend dev sucks");
            }
            obj.put(Integer.toString(i + 1), a);
        }

        return obj.toString().replaceAll("\\\\", "").replaceAll("\"", "");

    }


    /**
     * Get authentication token
     *
     * @param request HTTP request information
     * @return token from the HTTP request header
     */
    String getToken(HttpServletRequest request) {
        String PREFIX = "Bearer ";
        String HEADER = "Authorization";
        return request.getHeader(HEADER).replace(PREFIX, "");
    }

    /**
     * Test method used for testing http request
     *
     * @param request request info
     */
    @PostMapping("/hello")

    public void Hello(HttpServletRequest request) throws JsonProcessingException {

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