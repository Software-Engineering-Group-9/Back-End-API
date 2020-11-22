package Doable.RowMapper;

import Doable.model.Event;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class eventRowMapper implements RowMapper<Event> {

    @Override
    public Event mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Event(resultSet.getString("eid"),
                resultSet.getString("title"),
                resultSet.getString("duedate"),
                resultSet.getString("duetime"),
                resultSet.getInt("timeneed"),
                resultSet.getString("userid"));
    }

}
