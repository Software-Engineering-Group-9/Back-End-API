package Doable.api;

import Doable.RowMapper.InfoRowMapper;
import Doable.model.User;
import Doable.RowMapper.UserRowMapper;
import Doable.model.Info;
import Doable.service.CreateTableService;
import Doable.service.JwtTokenService;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
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


// todo: parse the email
// todo: use string formatter

@RequestMapping(Endpoint.USER)
@RestController
public class UserController {

    final JdbcTemplate jdbcTemplate;


    private JwtTokenService jwtTokenService;

    private final CreateTableService createTableService;

    public UserController(JdbcTemplate jdbcTemplate, CreateTableService createTableService, JwtTokenService jwtTokenService) {
        this.jdbcTemplate = jdbcTemplate;
        this.createTableService = createTableService;
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * Register endpoint, use to register user
     *
     * @param registerInfo user inputted information (email and password)
     * @return token if success
     */
    @PostMapping(Endpoint.REGISTER)
    public String register(@RequestBody String registerInfo) {
        createTableService.createUserTable(SQLCommand.user);
        String email = new JSONObject(registerInfo).getString("email");
        String pwd = new JSONObject(registerInfo).getString("password");

        Integer check = this.jdbcTemplate.queryForObject(SQLCommand.USER_QUERY_BY_EMAIL2, new Object[]{email}, Integer.class);
        if (check == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Our backend dev suck");
        } else if (check == 0) {
            String id = shortUUID();
            String token = this.jwtTokenService.generateToken(id);
            User u = new User(id, email.toLowerCase(), hashPassword(pwd), token);
            if (this.jdbcTemplate.update(SQLCommand.USER_INSERT, u.getId(), u.getEmail(), u.getPassword(), token) == 1)
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
    @PostMapping(Endpoint.LOGIN)
    public String login(@RequestBody String loginInfo) {
        createTableService.createUserTable(SQLCommand.user);
        String email = new JSONObject(loginInfo).getString("email");
        String pwd = new JSONObject(loginInfo).getString("password");

        Integer check = jdbcTemplate.queryForObject(SQLCommand.USER_QUERY_BY_EMAIL2, new Object[]{email}, Integer.class);
        if (check == null)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Our backend dev suck");
        else if (check == 1) {
            User user = jdbcTemplate.queryForObject(SQLCommand.USER_QUERY_BY_EMAIL, new Object[]{email}, new UserRowMapper());
            if (user != null && checkPass(pwd, user.getPassword())) {
                if (!this.jwtTokenService.validateToken(user.getToken())) {
                    user.setToken(jwtTokenService.generateToken(user.getId()));
                    this.jdbcTemplate.update(SQLCommand.USER_TOKEN_UPDATE_BY_ID, user.getToken(), user.getId());
                }
                return new JSONObject("{\"token\": Bearer " + user.getToken() + "}").toString();
            } else
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username and password don't match");
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
        Info info = jdbcTemplate.queryForObject(SQLCommand.GET_INFO, new InfoRowMapper());
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
