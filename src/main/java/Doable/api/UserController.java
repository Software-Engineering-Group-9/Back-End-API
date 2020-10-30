package Doable.api;

import Doable.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@CrossOrigin
@RequestMapping("api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String addUser(@Valid @NotNull @RequestBody String registerInfo){
        JSONObject jObject = new JSONObject(registerInfo);
        return "temp";
    }

    @GetMapping("/login")
    public String login(@Valid @NotNull @RequestBody String loginInfo){
        JSONObject jObject = new JSONObject(loginInfo);
        return "temp";
    }


}
