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
    public String addEvent(@Valid @NotNull @RequestBody String EventInfo, HttpServletRequest request) {
        createTableService.createTodoTable(event);
        JSONObject jObject = new JSONObject(EventInfo);
        return jObject.put("1", "bobo is gay").toString();
    }

    /**
     * Create user availability
     *
     * @param EventInfo event information
     * @param request   http request information
     */
    @PostMapping(CREATE_AVAILABILITY)
    public void addAvailability(@Valid @NotNull @RequestBody String EventInfo, HttpServletRequest request) {
        createTableService.createAvailabilityTable(BusyScheudled);
        JSONObject jObject = new JSONObject(EventInfo);
        if(jdbcTemplate.update(BUSY_INSERT, jObject.getInt("id"),
                jObject.getString("title"),
                jObject.getString("start"),
                jObject.getString("end"),
                jObject.getString("color"),
                jwtTokenService.getSubjectFromToken(getToken(request))) != 1)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Our backend devs suck");

    }

    /**
     * Get the list of scheudled events based on the userid
     *
     * @param request
     * @return
     */
    @GetMapping(GET_SCHEDULED_EVENT)
    public String getScheduledEvent(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        List<ScheduledEvent> list = jdbcTemplate.query(SCHEDULED_EVENT_QUERY_BY_UUID, new Object[]{jwtTokenService.getSubjectFromToken(getToken(request))}, new ScheduledEventRowMapper());
        obj.put("0", list.size());
        for (int i = 0; i < list.size(); i++) {
            String a;
            try {
                a = mapper.writeValueAsString(list.get(i));
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Our backend dev sucks");
            }
            obj.put(Integer.toString(i + 1), a);
        }
        System.out.println(obj.toString());
        return obj.toString().replaceAll("\\\\", "");
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

    /**
     * Add scheudledEvent to calendars/dbs
     *
     * @param start_time start time of the scheudled event
     * @param end_time   end time of scheudled event
     * @param uuid       user uuid
     */
    public void addScheudledEvent(String start_time, String end_time, String uuid) {
        createTableService.createScheudledEventTable(scheudledEvent);
        jdbcTemplate.update(SCHEUDLED_EVENT_INSERT, shortUUID(), uuid, start_time, end_time);
    }

    @PutMapping("/update")
    public void updateEvent(@Valid @NotNull @RequestBody String EventInfo, HttpServletRequest request) {
        JSONObject jObject = new JSONObject(EventInfo);

    }

    @DeleteMapping("/delete")
    public void deleteEvent(@Valid @NotNull @RequestBody String EventInfo, HttpServletRequest request) {
        JSONObject jObject = new JSONObject(EventInfo);

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
        sendEmail("sarkis8@uwindsor.ca");
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

    void sendEmail(String email) {
        Info info = jdbcTemplate.queryForObject(GET_INFO, new InfoRowMapper());

        if (info != null) {
            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
            // Get a Properties object
            Properties props = System.getProperties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.port", "465");
            props.setProperty("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.auth", "true");
            props.put("mail.store.protocol", "pop3");
            props.put("mail.transport.protocol", "smtp");
            String username = info.getUsername();
            String password = info.getPassword();
            try {
                Session session = Session.getDefaultInstance(props,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

                Message msg = new MimeMessage(session);

                msg.setFrom(new InternetAddress("Noreply@Doable.com", "Noreply"));
                msg.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(email, false));
                msg.setSubject("Welcome to doable");
                msg.setText("Welcome to doable mother fucker!");
                msg.setSentDate(new Date());
                Transport.send(msg);
            } catch (MessagingException e) {
                System.out.println("Error, cause: " + e);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}