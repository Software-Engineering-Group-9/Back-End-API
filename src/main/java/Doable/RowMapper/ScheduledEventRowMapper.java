package Doable.RowMapper;

import Doable.model.ScheduledEvent;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScheduledEventRowMapper implements RowMapper<ScheduledEvent> {

    @Override
    public ScheduledEvent mapRow(ResultSet resultSet, int i) throws SQLException {
           return  new ScheduledEvent(resultSet.getString("sid"),
                    resultSet.getString("userid"),
                    resultSet.getString("starttime"),
                    resultSet.getString("endtime"));



    }

}
