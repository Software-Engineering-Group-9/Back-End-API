package Doable.RowMapper;

import Doable.model.User;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class userRowMapper implements RowMapper<User> {

    /**
     * Create new user object with data from the DBs
     * @param resultSet
     * @param i
     * @return
     * @throws SQLException
     */
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        return new User(resultSet.getString("uuid"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("token"));
    }
}
