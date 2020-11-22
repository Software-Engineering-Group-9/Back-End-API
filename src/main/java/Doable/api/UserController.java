package Doable.api;

import Doable.model.User;
import Doable.model.userRowMapper;
import Doable.service.CreateTableService;
import Doable.service.JwtTokenService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.ByteBuffer;
import java.util.UUID;

import static Doable.SQLCommand.*;

// todo: add password hashing -> unhash and verify password
// todo: clean up both endpoints
// todo: comments
// todo: testing both endpoints to find bugs

@CrossOrigin(origins = "*")
@RequestMapping("api/v1/user")
@RestController
public class UserController {

    @Autowired
    final JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private final CreateTableService createTableService;

    public UserController(JdbcTemplate jdbcTemplate, CreateTableService createTableService) {
        this.jdbcTemplate = jdbcTemplate;
        this.createTableService = createTableService;
    }


    /**
     * Register endpoint
     * @param registerInfo user inputted information
     * @return token if success
     */
    @PostMapping("/register")
    public String register(@RequestBody String registerInfo) {
        createTableService.createUserTable("user3");
        String email = new JSONObject(registerInfo).getString("email");
        String pwd = new JSONObject(registerInfo).getString("password");

        try {
            // Check if user exists
            jdbcTemplate.queryForObject(USER_QUERY_BY_EMAIL, new Object[]{email}, Integer.class);
        } catch (EmptyResultDataAccessException e){
            // Create new user
            String id = shortUUID();
            String token = jwtTokenService.generateToken(id);
            User u = new User(id, email.toLowerCase(), pwd, token);
            if(jdbcTemplate.update(USER_INSERT, u.getId(), u.getEmail(), u.getPassword(), token) == 1) {
                return new JSONObject("{\"token\": Bearer "+token+"}").toString();
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Our backend dev suck");
        }  catch (IncorrectResultSetColumnCountException e2){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
    }

    /**
     *
     * @param loginInfo user inputted information
     * @return JWT token if success
     */
    @PostMapping("/login")
    public String login(@RequestBody String loginInfo) {
        String email = new JSONObject(loginInfo).getString("email");
        String pwd = new JSONObject(loginInfo).getString("password");
        try {
            // Check if user exists
            User user = jdbcTemplate.queryForObject(USER_QUERY_BY_EMAIL, new Object[]{email.toLowerCase()}, new userRowMapper());
            if(user != null && verifyPassword(pwd, user.getPassword())){
                if (!jwtTokenService.validateToken(user.getToken())) {
                    user.setToken(jwtTokenService.generateToken(UUID.randomUUID().toString()));
                    jdbcTemplate.update(USER_TOKEN_UPDATE_BY_ID, user.getToken(), user.getId());
                }
                return new JSONObject("{\"token\": Bearer "+user.getToken()+"}").toString();
            }else{
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username and password don't match");
            }
        } catch (EmptyResultDataAccessException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "We couldn't find user with that email");
        }
    }

    /**
     *
     * @param userpwd
     * @param pwd
     * @return
     */
    boolean verifyPassword(String userpwd, String pwd){
        return userpwd.equals(pwd);
    }

    public static String shortUUID() {
        UUID uuid = UUID.randomUUID();
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        return Long.toString(l, Character.MAX_RADIX);
    }




}
