package Doable.api;

public class SQLCommand {
    public static final String user = "user5";
    public static final String event = "todoEvent2";
    public static final String BusyScheudled = "BusyScheudled3";
    public static final String scheudledEvent = "scheudledEvent3";

    // QUERY TABLE COMMAND
    public static final String USER_QUERY_BY_EMAIL = "SELECT * FROM " + user + " WHERE EMAIL = ?";
    public static final String EVENT_QUERY_BY_UUID = "SELECT * FROM " + event + " WHERE USERID = ?";
    public static final String USER_QUERY_BY_EMAIL2 = "SELECT count(*) from " + user + " where EMAIL = ?";
    public static final String SCHEDULED_EVENT_QUERY_BY_UUID = "select * from " + scheudledEvent + " where userid = ?";
    public static final String GET_INFO = "SELECT * FROM info WHERE id = '1'";

    // INSERT INTO TABLE COMMAND
    public static final String USER_INSERT = "INSERT INTO " + user +" VALUES (?, ?, ?, ?)";
    public static final String EVENT_INSERT = "INSERT INTO " + event + " VALUES (?, ?, ?, ?, ?, ?)";
    public static final String AVAILABILITY_INSERT = "INSERT INTO " + BusyScheudled + " VALUES (?, ?, ?, ?, ?)";
    public static final String SCHEUDLED_EVENT_INSERT = "INSERT INTO " + scheudledEvent + " VALUES  (?, ?, ?, ?)";

    // UPDATE COMMAND
    public static final String USER_TOKEN_UPDATE_BY_ID = "UPDATE " + user + " SET token = ? WHERE 'ID' = ?";

    // QUERY DATABASE COMMAND
    public static final String CHECK_IF_TABLE_EXISTS = "select count(*) from user_tables where table_name = ?";

    // CREATE TABLE
    public static final String CREATE_EVENT_TABLE = "create table " + event + "( eid VARCHAR2(50) NOT NULL, title varchar2(50), duedate VARCHAR2(10), duetime VARCHAR2(5), timeneed int, userid VARCHAR2(50), PRIMARY KEY(eid), FOREIGN KEY (userid) REFERENCES " + user +"(uuid))";
    public static final String CREATE_USER_TABLE = "CREATE TABLE " + user + "(uuid VARCHAR2(50) NOT NULL, email varchar2(50), password varchar2(100), token varchar2(250), PRIMARY KEY(uuid))";
    public static final String CREATE_AVAILABILITY_TABLE = "CREATE TABLE " + BusyScheudled + "(aid VARCHAR2(50) NOT NULL, userid VARCHAR2(50), dates varchar2(10), starttime varchar2(5), endtime varchar2(5), PRIMARY KEY(aid), FOREIGN KEY (userid) REFERENCES " + user + "(uuid))";
    public static final String CREATE_SCHEUDLED_TABLE = "CREATE TABLE "+ scheudledEvent + "(sid VARCHAR2(50) NOT NULL, userid VARCHAR2(50), starttime varchar2(5), endtime varchar2(5), PRIMARY KEY(sid), FOREIGN KEY(userid) REFERENCES " + user + "(uuid))";
}
