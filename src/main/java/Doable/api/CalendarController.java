package Doable.api;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CrossOrigin
@RequestMapping("api/v1/calendar")
@RestController
public class CalendarController {

    @PostMapping("/create")
    public void addEvent(@Valid @NotNull @RequestBody String EventInfo){
        JSONObject jObject = new JSONObject(EventInfo);
    }


    @PutMapping("/update")
    public void updateEvent(@Valid @NotNull @RequestBody String EventInfo){
        JSONObject jObject = new JSONObject(EventInfo);
    }

    @DeleteMapping("/delete")
    public void deleteEvent(@Valid @NotNull @RequestBody String EventInfo){
        JSONObject jObject = new JSONObject(EventInfo);
    }


}
