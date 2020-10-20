package Doable.dao;

import Doable.model.User;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

public interface UserDao {

    int insertUser(UUID id, User user);

    default int insertUser(User user){
        UUID id = UUID.randomUUID();
        return insertUser(id, user);
    }

    List<User> selectAllUsers();
}
