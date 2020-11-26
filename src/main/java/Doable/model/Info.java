package Doable.model;

public class Info {

    private String username;
    private String password;

    public Info(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

}
