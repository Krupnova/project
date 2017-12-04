package com.project.shared.services;

import com.project.shared.entities.User;

import java.util.List;


public interface UserService {

    void saveUser(User user) throws Exception;

    User findUserById(long userId);

    void updateUser(User user) throws Exception;

    void deleteUser(Long userId) throws Exception;

    List<User> getAllUsers();

    User findUserByLoginAndPassword(String login, String password);

    User findUserByLogin(String login);
}
