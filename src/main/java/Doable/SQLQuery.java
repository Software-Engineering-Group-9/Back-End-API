package Doable;

public class SQLQuery {
    public static final String USER_QUERY_BY_EMAIL = "SELECT * FROM user2 WHERE EMAIL = ?";
    public static final String USER_INSERT = "INSERT INTO user2 VALUES (?, ?, ?, ?)";
    public static final String USER_TOKEN_UPDATE_BY_ID = "UPDATE user2 SET token = ? WHERE id = ?";
}
