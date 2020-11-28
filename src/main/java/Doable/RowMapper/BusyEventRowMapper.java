package Doable.RowMapper;

import Doable.model.BusyEvent;
import Doable.model.TodoEvent;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BusyEventRowMapper  implements RowMapper<BusyEvent> {

    public BusyEvent mapRow(ResultSet resultSet, int i) throws SQLException {
        return new BusyEvent(resultSet.getString("sid"),
                resultSet.getString("userid"),
                resultSet.getString("title"),
                resultSet.getString("starttime"),
                resultSet.getString("endtime"),
                resultSet.getString("color"));
    }

}
