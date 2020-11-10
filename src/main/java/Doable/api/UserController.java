package Doable.api;

import Doable.model.User;
import Doable.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


// todo: establish a connection with the oracle sql server

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
    public String register(@RequestParam("email") String email, @RequestParam("password") String pwd) {
        // todo: Check if user already exists in the database else return error
        // todo: add user to the database if user doesn't exists and return 200
        User u = new User(UUID.randomUUID(), email, pwd);
        String token = getJWTToken(u);
        return token;
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String pwd) {
        // todo: check if username and password matches in the database else return error
        // todo: if autheication is successful return code 200 and the authenication  token (if still valid) else create new token with getJWTToken method  */
        // todo: change to GET Mapping

        return "temp";
    }

    /**
     * Create a JWT token
     * @param user new user data
     * @return JWT token with HS512 signature
     */
    private String getJWTToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("userId", user.getId() + "");

        // Create Role user
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        // Create token based on given information
        String token = Jwts.builder()
                .setClaims(claims)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512, "mySecretKey")
                .compact();

        return "Bearer " + token;
    }
}
