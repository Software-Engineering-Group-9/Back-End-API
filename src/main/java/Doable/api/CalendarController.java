package Doable.api;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CrossOrigin
@RequestMapping("api/v1/calendar")
@RestController
public class CalendarController {

    // todo: Get the token from the header

    @PostMapping("/create")
    public void addEvent(@Valid @NotNull @RequestBody String EventInfo, HttpServletRequest request) {
        JSONObject jObject = new JSONObject(EventInfo);
        //Get token info
        Claims body = Jwts.parser()
                .setSigningKey("mySecretKey")
                .parseClaimsJws(request.getHeader("Authorization"))
                .getBody();
        System.out.println(body.getSubject());
    }

    @PutMapping("/update")
    public void updateEvent(@Valid @NotNull @RequestBody String EventInfo, HttpServletRequest request) {
        JSONObject jObject = new JSONObject(EventInfo);
        /*Claims body = Jwts.parser()
                .setSigningKey("mySecretKey")
                .parseClaimsJws(token)
                .getBody(); */
    }

    @DeleteMapping("/delete")
    public void deleteEvent(@Valid @NotNull @RequestBody String EventInfo, HttpServletRequest request) {
        JSONObject jObject = new JSONObject(EventInfo);

        /*Claims body = Jwts.parser()
                .setSigningKey("mySecretKey")
                .parseClaimsJws(token)
                .getBody();
                */
    }

    /**
     * Test method used for testing http request
     * @param request request info
     */
    @PostMapping("/hello")
    public void Hello(HttpServletRequest request){
        System.out.println("you have the token, nice!");
        String token = request.getHeader("Authorization").replace("Bearer", "");
        System.out.println(token);
        Claims body = Jwts.parser()
                .setSigningKey("mySecretKey")
                .parseClaimsJws(token)
                .getBody();
        System.out.println(body.getSubject());
    }

}
