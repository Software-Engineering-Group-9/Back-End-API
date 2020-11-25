package Doable.RowMapper;

import Doable.model.scheduledEvent;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class scheduledEventRowMapper implements RowMapper<scheduledEvent> {

    @Override
    public scheduledEvent mapRow(ResultSet resultSet, int i) throws SQLException {
           return  new scheduledEvent(resultSet.getString("sid"),
                    resultSet.getString("userid"),
                    resultSet.getString("starttime"),
                    resultSet.getString("endtime"));



    }

}
