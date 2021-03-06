package Doable.service;

import Doable.dao.UserDao;
import Doable.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(@Qualifier("fakeDao") UserDao userDao) {
        this.userDao = userDao;
    }

    public int addUser(User user){
        return userDao.insertUser(user);
    }

    public List<User> getAllUsers(){
        return userDao.selectAllUsers();
    }

    public Optional<User> getUserById(String id){
        return userDao.selectUserById(id);
    }

    public Optional<User> getUserByEmail(String email){
        return userDao.selectUserByEmail(email);
    }

    public int deleteUser(String id){
        return userDao.deleteUserById(id);
    }

    public int updateUser(String id, User newUser){
        return userDao.updateUserById(id, newUser);
    }

}
