package com.project.server.dao;

import com.project.shared.entities.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.logging.Logger;

@Repository("userDAO")
public class UserDAO extends GenericDAO<User, Long> {

    private Logger logger = Logger.getLogger(UserDAO.class.getName());

    public UserDAO() {
        super(User.class);
    }

    public User findUserByLoginAndPassword(String login, String password) {
        Query q = getCurrentSession()
                .createQuery("from User " +
                        "where userLogin = :login " +
                        "and userPassword = :password")
                .setParameter("login", login)
                .setParameter("password", password);
        try {
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User findUserByLogin(String login) {
        Query q = getCurrentSession()
                .createQuery("from User " +
                        "where userLogin = :login")
                .setParameter("login", login);
        try {
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
