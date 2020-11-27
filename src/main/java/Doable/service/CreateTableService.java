package Doable.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import static Doable.api.SQLCommand.*;

@Service
public class CreateTableService {

    @Autowired
    JdbcTemplate jdbcTemplate;

   public void createTodoTable(String table_name){
       if(checkTable(table_name))
        jdbcTemplate.update(CREATE_EVENT_TABLE);
    }

    public void createAvailabilityTable(String table_name){
        if(checkTable(table_name))
            jdbcTemplate.update(CREATE_AVAILABILITY_TABLE);
    }

    public void createScheudledEventTable(String table_name){
        if(checkTable(table_name))
            jdbcTemplate.update(CREATE_SCHEUDLED_TABLE);
    }

    public void createUserTable(String table_name){
        if(checkTable(table_name))
            jdbcTemplate.update(CREATE_USER_TABLE);
    }

    public boolean checkTable(String table_name){

        Integer result = jdbcTemplate.queryForObject(CHECK_IF_TABLE_EXISTS, Integer.class, table_name.toUpperCase());
        return result == 0;
    }

}
