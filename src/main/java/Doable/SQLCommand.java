package Doable;

public class SQLCommand {
    private static final String user = "user3";
    private static final String event = "todoEvent1";

    // QUERY TABLE COMMAND
    public static final String USER_QUERY_BY_EMAIL = "SELECT * FROM " + user + " WHERE EMAIL = ?";

    // INSERT INTO TABLE COMMAND
    public static final String USER_INSERT = "INSERT INTO " + user +" VALUES (?, ?, ?, ?)";
    public static final String EVENT_INSERT = "INSERT INTO " + event + " VALUES (?, ?, ?, ?, ?, ?)";

    // UPDATE COMMAND
    public static final String USER_TOKEN_UPDATE_BY_ID = "UPDATE " + user + " SET token = ? WHERE 'ID' = ?";

    // QUERY DATABASE COMMAND
    public static final String CHECK_IF_TABLE_EXISTS = "select count(*) from user_tables where table_name = ?";

    // CREATE TABLE
    public static final String CREATE_EVENT_TABLE = "create table " + event + "( eid VARCHAR2(50) NOT NULL, title varchar2(50), duedate VARCHAR2(10), duetime VARCHAR2(5), timeneed int, userid VARCHAR2(50), PRIMARY KEY(eid), FOREIGN KEY (userid) REFERENCES " + user +"(uuid))";
    public static final String CREATE_USER_TABLE = "CREATE TABLE " + user + "(uuid VARCHAR2(50) NOT NULL, email varchar2(50), password varchar2(20), token varchar2(250), PRIMARY KEY(uuid))";
    public static final String CREATE_AVAILABILITY_TABLE = "CREATE TABLE availability1(aid VARCHAR2(50) NOT NULL, userid VARCHAR2(50), starttime varchar2(5), endtime varchar2(5), PRIMARY KEY(aid), FOREIGN KEY (userid) REFERENCES " + user + "(uuid))";
    public static final String CREATE_SCHEUDLED_TABLE = "CREATE TABLE scheudledEvent1(sid VARCHAR2(50) NOT NULL, userid VARCHAR2(50), starttime varchar2(5), endtime varchar2(5), PRIMARY KEY(sid), FOREIGN KEY(userid) REFERENCES " + user + "(uuid))";
}
