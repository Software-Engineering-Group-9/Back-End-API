package Doable.RowMapper;

import Doable.model.BusyEvent;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BusyEventRowMapper implements RowMapper<BusyEvent> {

    @Override
    public BusyEvent mapRow(ResultSet resultSet, int i) throws SQLException {
        return new BusyEvent(resultSet.getString("aid"),
                resultSet.getString("title"),
                resultSet.getString("userid"),
                resultSet.getString("start_time"),
                resultSet.getString("end_time"),
                resultSet.getString("color"));
    }

}
