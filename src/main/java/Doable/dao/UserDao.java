package Doable.dao;

import Doable.model.User;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface UserDao {

    int insertUser(String id, User user);

    default int insertUser(User user){
        String id = UUID.randomUUID().toString();
        return insertUser(id, user);
    }

    List<User> selectAllUsers();

    Optional<User> selectUserById(String id);

    Optional<User> selectUserByEmail(String email);

    int deleteUserById(String id);

    int updateUserById(String id, User user);
}
