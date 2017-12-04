package com.project.server.controllers;

import com.project.shared.entities.UserLoginInfo;
import com.project.shared.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login_{login}_{password}", method = RequestMethod.GET)
    public
    @ResponseBody
    UserLoginInfo loginUser(@PathVariable("login") String login, @PathVariable("password") String password) {
        return loginService.loginUser(login, password);
    }
}
