package Doable.model;

import java.util.UUID;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

// todo: comment

public class User {

    private final String id;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String token;

    public User(@JsonProperty("id") String id,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password,
                String token) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public void setToken(String newToken){
        this.token = newToken;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getToken(){
        return token;
    }

}
