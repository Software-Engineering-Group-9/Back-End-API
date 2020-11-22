package Doable.api;

import Doable.service.CreateTableService;
import Doable.service.JwtTokenService;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.nio.ByteBuffer;
import java.util.UUID;

import static Doable.SQLCommand.AVAILABILITY_INSERT;
import static Doable.SQLCommand.EVENT_INSERT;

@RequestMapping("api/v1/calendar")
@RestController
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
    @PostMapping("/createEvent")
    public void addEvent(@Valid @NotNull @RequestBody String EventInfo, HttpServletRequest request) {
        createTableService.createTodoTable("todoEvent1");
        JSONObject jObject = new JSONObject(EventInfo);
        System.out.println(jdbcTemplate.update(EVENT_INSERT, shortUUID(), jObject.getString("title"), jObject.getString("duedate"), jObject.getString("duetime"), jObject.getString("timeneed"), jwtTokenService.getSubjectFromToken(getToken(request))));

    }

    /**
     * Create user availability
     *
     * @param EventInfo event information
     * @param request   http request information
     */
    @PostMapping("/createAvailability")
    public void addAvailability(@Valid @NotNull @RequestBody String EventInfo, HttpServletRequest request) {
        createTableService.createAvailabilityTable("availabilityTable");
        JSONObject jObject = new JSONObject(EventInfo);
        jdbcTemplate.update(AVAILABILITY_INSERT, shortUUID(), jwtTokenService.getSubjectFromToken(getToken(request)), jObject.getString("starttime"), jObject.getString("endtime"));
    }


    /**
     * Add scheudledEvent to calendars/dbs
     *
     * @param start_time start time of the scheudled event
     * @param end_time   end time of scheudled event
     * @param uuid       user uuid
     */
    public void addScheudledEvent(String start_time, String end_time, String uuid) {
        createTableService.createScheudledEventTable("scheudledEvent1");
        jdbcTemplate.update(AVAILABILITY_INSERT, shortUUID(), uuid, start_time, end_time);
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
     * @param request
     * @return
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
