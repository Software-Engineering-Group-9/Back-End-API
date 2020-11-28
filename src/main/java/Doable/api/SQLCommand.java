package Doable.api;

public class SQLCommand {
    public static final String user = "user6";
    public static final String todoEvent = "todoEvent3";
    public static final String busyEvent = "BusyScheudled7";
    public static final String scheduledEvent = "scheduledEvent4";

    // QUERY TABLE COMMAND
    public static final String USER_QUERY_BY_EMAIL = "SELECT * FROM " + user + " WHERE EMAIL = ?";
    public static final String TODO_QUERY_BY_UUID = "SELECT * FROM " + todoEvent + " WHERE USERID = ?";
    public static final String BUSY_QUERY_BY_UUID = "SELECT * FROM " + busyEvent + " WHERE USERID = ?";
    public static final String SCHEDULED_QUERY_BY_UUID = "select * from " + scheduledEvent + " where userid = ?";
    public static final String USER_QUERY_BY_EMAIL2 = "SELECT count(*) from " + user + " where EMAIL = ?";
    public static final String GET_INFO = "SELECT * FROM info WHERE id = '1'";

    // INSERT INTO TABLE COMMAND
    public static final String USER_INSERT = "INSERT INTO " + user +" VALUES (?, ?, ?, ?)";
    public static final String EVENT_INSERT = "INSERT INTO " + todoEvent + " VALUES (?, ?, ?, ?, ?, ?)";
    public static final String BUSY_INSERT = "INSERT INTO " + busyEvent + " VALUES (?, ?, ?, ?, ?, ?)";
    public static final String SCHEUDLED_EVENT_INSERT = "INSERT INTO " + scheduledEvent + " VALUES  (?, ?, ?, ?)";

    // UPDATE COMMAND
    public static final String USER_TOKEN_UPDATE_BY_ID = "UPDATE " + user + " SET token = ? WHERE 'ID' = ?";

    // QUERY DATABASE COMMAND
    public static final String CHECK_IF_TABLE_EXISTS = "select count(*) from user_tables where table_name = ?";

    // CREATE TABLE
    public static final String CREATE_EVENT_TABLE = "create table " + todoEvent + "( eid VARCHAR2(50) NOT NULL, title varchar2(50), duedate VARCHAR2(10), duetime VARCHAR2(5), timeneed int, userid VARCHAR2(50), PRIMARY KEY(eid), FOREIGN KEY (userid) REFERENCES " + user +"(uuid))";
    public static final String CREATE_USER_TABLE = "CREATE TABLE " + user + "(uuid VARCHAR2(50) NOT NULL, email varchar2(50), password varchar2(100), token varchar2(250), PRIMARY KEY(uuid))";
    public static final String CREATE_AVAILABILITY_TABLE = "CREATE TABLE " + busyEvent + "(aid VARCHAR2(100) NOT NULL, title varchar2(40), start_time varchar2(19), end_time varchar2(19), Color varchar2(7), userid varchar2(50), PRIMARY KEY(aid), FOREIGN KEY (userid) REFERENCES " + user + "(uuid))";
    public static final String CREATE_SCHEUDLED_TABLE = "CREATE TABLE "+ scheduledEvent + "(sid VARCHAR2(50) NOT NULL, userid VARCHAR2(50), starttime varchar2(5), endtime varchar2(5), PRIMARY KEY(sid), FOREIGN KEY(userid) REFERENCES " + user + "(uuid))";
}
