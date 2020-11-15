package Doable.model;

import java.util.UUID;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

// todo: comment

public class User {

    private final UUID id;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String token;

    public User(@JsonProperty("id") UUID id,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password,
                String token) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public User(String email,String password){
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
        System.out.println(email);
    }

    public void setToken(String newToken){
        this.token = newToken;
    }

    public UUID getId() {
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
