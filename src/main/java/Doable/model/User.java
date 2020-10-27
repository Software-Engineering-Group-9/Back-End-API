package Doable.model;

import java.util.UUID;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private final UUID id;

    @NotBlank
    private String email;
    @NotBlank
    private String password;

    public User(@JsonProperty("id") UUID id,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password)
                {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User(@JsonProperty("email") String email, @JsonProperty("password") String password){
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
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

}