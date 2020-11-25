package Doable.RowMapper;

import Doable.model.info;
import Doable.model.scheduledEvent;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class infoRowMapper implements RowMapper<info> {

    @Override
    public info mapRow(ResultSet resultSet, int i) throws SQLException {
        return new info(resultSet.getString("username"),
                        resultSet.getString("password")
                );
    }
}
