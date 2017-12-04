package com.project.server.controllers;

import com.project.shared.entities.Status;
import com.project.shared.entities.User;
import com.project.shared.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class UserController {

    static final Logger logger = Logger.getLogger(RestController.class);
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/save_user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Status saveUser(@RequestBody User user) {
        try {
            User userFromDB = userService.findUserByLoginAndPassword(
                    user.getUserLogin(),
                    user.getUserPassword()
            );
            if (userFromDB == null) {
                userService.saveUser(user);
                return new Status(1, "User successfully saved.");
            } else {
                return new Status(-1, "This user already exists in the database!");
            }
        } catch (Exception e) {
            return new Status(0, e.getMessage());
        }
    }

    @RequestMapping(value = "/delete_user_{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Status deleteEmployee(@PathVariable("id") long id) {
        try {
            userService.deleteUser(id);
            return new Status(1, "Employee successfully deleted");
        } catch (Exception e) {
            return new Status(0, e.toString());
        }
    }

    @RequestMapping(value = "/get_all_users", method = RequestMethod.GET)
    public
    @ResponseBody
    List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/find_user_{login}_{password}", method = RequestMethod.GET)
    public
    @ResponseBody
    User findUserByLoginAndPassword(@PathVariable("login") String login, @PathVariable("password") String password) {
        return userService.findUserByLoginAndPassword(login, password);
    }

    @RequestMapping(value = "/find_user_{login}", method = RequestMethod.GET)
    public
    @ResponseBody
    User findUserByLogin(@PathVariable("login") String login) {
        return userService.findUserByLogin(login);
    }

    @RequestMapping(value = "/update_user", method = RequestMethod.POST)
    public
    @ResponseBody
    Status updateUser(@RequestBody User user) throws Exception {
        try {
            userService.updateUser(user);
            //Cookies.removeCookie("password");
            return new Status(1, "User successfully update.");
        } catch (Exception e){
            return new Status(0, e.toString());
        }
    }

}
