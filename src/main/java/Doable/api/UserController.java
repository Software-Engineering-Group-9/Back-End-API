package Doable.api;

import Doable.RowMapper.infoRowMapper;
import Doable.model.User;
import Doable.RowMapper.userRowMapper;
import Doable.model.info;
import Doable.service.CreateTableService;
import Doable.service.JwtTokenService;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import static Doable.api.Endpoint.*;
import static Doable.api.SQLCommand.*;

// todo: clean up both endpoints
// todo: testing both endpoints to find bugs

@CrossOrigin(origins = "*")
@RequestMapping(USER)
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
     * Register endpoint, use to register user
     *
     * @param registerInfo user inputted information (email and password)
     * @return token if success
     */
    @PostMapping(REGISTER)
    public String register(@RequestBody String registerInfo) {
        createTableService.createUserTable(user);
        String email = new JSONObject(registerInfo).getString("email");
        String pwd = new JSONObject(registerInfo).getString("password");

        Integer check = jdbcTemplate.queryForObject(USER_QUERY_BY_EMAIL2, new Object[]{email}, Integer.class);
        if (check == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Our backend dev suck");
        } else if (check == 0) {
            String id = shortUUID();
            String token = jwtTokenService.generateToken(id);
            System.out.println(jwtTokenService.getSubjectFromToken(token));
            User u = new User(id, email.toLowerCase(), hashPassword(pwd), token);
            if (jdbcTemplate.update(USER_INSERT, u.getId(), u.getEmail(), u.getPassword(), token) == 1)
                return new JSONObject("{\"token\": Bearer " + token + "}").toString();
            else
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Our backend dev suck");
        } else
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
    }

    /**
     * Login endpoint user to authenticate user
     *
     * @param loginInfo user inputted information (email and password)
     * @return JWT token if success
     */
    @PostMapping(LOGIN)
    public String login(@RequestBody String loginInfo) {
        createTableService.createUserTable(user);
        String email = new JSONObject(loginInfo).getString("email");
        String pwd = new JSONObject(loginInfo).getString("password");

        Integer check = jdbcTemplate.queryForObject(USER_QUERY_BY_EMAIL2, new Object[]{email}, Integer.class);
        if (check == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Our backend dev suck");
        } else if (check == 1) {
            User user = jdbcTemplate.queryForObject(USER_QUERY_BY_EMAIL, new Object[]{email.toLowerCase()}, new userRowMapper());
            if (user != null && checkPass(pwd, user.getPassword())) {
                System.out.println(user.getId());
                if (!jwtTokenService.validateToken(user.getToken())) {
                    user.setToken(jwtTokenService.generateToken(UUID.randomUUID().toString()));
                    jdbcTemplate.update(USER_TOKEN_UPDATE_BY_ID, user.getToken(), user.getId());
                }
                return new JSONObject("{\"token\": Bearer " + user.getToken() + "}").toString();
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username and password don't match");
            }
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "We couldn't find user with that email");
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

    /**
     * Hash password to be stored in the database
     *
     * @param plainTextPassword plain text password to be hash
     * @return newly created hash password
     */
    private String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * Check if user inputted password (unhash) match with the hash password in the dbs
     *
     * @param plainPassword  plain text password (unhash)
     * @param hashedPassword hash password
     * @return true if both match else false
     */
    private boolean checkPass(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    void sendEmail() {
        info info = jdbcTemplate.queryForObject(GET_INFO, new infoRowMapper());
        if(info != null) {

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
            final String username = info.getUsername();
            final String password = info.getPassword();
            try {
                Session session = Session.getDefaultInstance(props,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

                // -- Create a new message --
                Message msg = new MimeMessage(session);

                // -- Set the FROM and TO fields --
                msg.setFrom(new InternetAddress("Noreply@Doable.com", "Noreply"));
                msg.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse("shinypichu11@hotmail.com", false));
                msg.setSubject("Welcome to doable");
                msg.setText("Thank you for registering!");
                msg.setSentDate(new Date());
                Transport.send(msg);
                System.out.println("msg send");
            } catch (MessagingException e) {
                System.out.println("Erreur d'envoi, cause: " + e);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

}
