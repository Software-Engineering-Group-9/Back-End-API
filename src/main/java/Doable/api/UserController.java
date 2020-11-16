package Doable.api;

import Doable.model.User;
import Doable.model.userRowMapper;
import Doable.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static Doable.SQLQuery.*;

// todo: add password hashing -> unhash and verify password
// todo: clean up both endpoints
// todo: comments
// todo: testing both endpoints to find bugs

@CrossOrigin
@RequestMapping("api/v1/user")
@RestController
public class UserController {

    @Autowired
    final JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenService jwtTokenService;

    public UserController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    /**
     *
     * @param email
     * @param pwd
     * @return
     */
    @PostMapping("/register")
    public String register(@RequestParam("email") String email, @RequestParam("password") String pwd) {
        try {
            // Check if user exists
            jdbcTemplate.queryForObject(USER_QUERY_BY_EMAIL, new Object[]{email}, Integer.class);
        } catch (EmptyResultDataAccessException e){
            // Create new user
            String token = jwtTokenService.generateToken(email);
            User u = new User(UUID.randomUUID(), email, pwd, token);
            if(jdbcTemplate.update(USER_INSERT, u.getId().toString(), u.getEmail(), u.getPassword(), token) == 1)
                return "Bearer " + token;
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Our backend dev suck");
        }  catch (IncorrectResultSetColumnCountException e2){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
    }

    /**
     *
     * @param email
     * @param pwd
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String pwd) {
        try {
            // Check if user exists
            User user = jdbcTemplate.queryForObject(USER_QUERY_BY_EMAIL, new Object[]{email}, new userRowMapper());
            if(user != null && verifyPassword(pwd, user.getPassword())){
                if(jwtTokenService.validateToken(user.getToken()))
                    return user.getToken();
                else {
                    user.setToken(jwtTokenService.generateToken(user.getEmail()));
                    jdbcTemplate.update(USER_TOKEN_UPDATE_BY_ID, user.getToken(), user.getId().toString());
                    return "Bearer " + user.getToken();
                }
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





}
