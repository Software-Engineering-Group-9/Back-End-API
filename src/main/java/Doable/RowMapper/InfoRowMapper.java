package Doable.RowMapper;

import Doable.model.Info;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InfoRowMapper implements RowMapper<Info> {

    @Override
    public Info mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Info(resultSet.getString("username"),
                        resultSet.getString("password")
                );
    }
}
