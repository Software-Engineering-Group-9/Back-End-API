package Doable.RowMapper;

import Doable.model.TodoEvent;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventRowMapper implements RowMapper<TodoEvent> {

    @Override
    public TodoEvent mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TodoEvent(resultSet.getString("eid"),
                resultSet.getString("userid"),
                resultSet.getString("title"),
                resultSet.getString("dueDate"),
                resultSet.getString("dueTime"),
                resultSet.getInt("timeneed"),
                resultSet.getString("color"));
    }
}
