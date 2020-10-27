package Doable.api;

import Doable.model.User;
import Doable.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RequestMapping("api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void addUser(@Valid @NotNull @RequestBody String string){
        JSONObject jObject = new JSONObject(string);
        userService.addUser(new User(jObject.getString("email"), jObject.getString("password")));
    }

//    @PostMapping
//    public void addUser(@Valid @NotNull @RequestBody User user){
//        userService.addUser(user);
//    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping(path = "{id}")
    public User getUserById(@PathVariable("id") UUID id){
        return userService.getUserById(id).orElse(null);
    }

    @DeleteMapping(path = "{id}")
    public void deleteUserById(@PathVariable("id") UUID id){
        userService.deleteUser(id);
    }

    @PutMapping(path = "{id}")
    public void updateUser(@PathVariable("id") UUID id, @Valid @NotNull @RequestBody User userToUpdate){
        userService.updateUser(id, userToUpdate);
    }

}
