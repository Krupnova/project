package com.project.server.services;

import com.project.server.dao.UserDAO;
import com.project.shared.entities.User;
import com.project.shared.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public void saveUser(User user) throws Exception {
        userDAO.persist(user);
    }

    @Override
    public User findUserById(long userId) {
        return userDAO.findById(userId);
    }

    @Override
    public void updateUser(User user) throws Exception {
        userDAO.update(user);
    }

    @Override
    public void deleteUser(Long userId) throws Exception {
        userDAO.delete(findUserById(userId));
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    @Override
    public User findUserByLoginAndPassword(String login, String password) {
        return userDAO.findUserByLoginAndPassword(login, password);
    }

    @Override
    public User findUserByLogin(String login){
        return userDAO.findUserByLogin(login);
    }


}
