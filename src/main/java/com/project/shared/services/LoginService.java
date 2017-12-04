package com.project.shared.services;

import com.project.shared.entities.UserLoginInfo;


public interface LoginService {
    UserLoginInfo loginUser(String login, String password);
}
