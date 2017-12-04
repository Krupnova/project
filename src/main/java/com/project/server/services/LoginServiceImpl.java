package com.project.server.services;

import com.project.server.dao.UserDAO;
import com.project.shared.entities.Roles;
import com.project.shared.entities.User;
import com.project.shared.entities.UserLoginInfo;
import com.project.shared.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserLoginInfo loginUser(String login, String password) {
        User user = userDAO.findUserByLogin(login);

        if (!user.getUserLogin().isEmpty()) {

            String passwordDB = user.getUserPassword();

            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                byte[] array = messageDigest.digest(passwordDB.getBytes());
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < array.length; i++) {
                    stringBuffer.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
                    passwordDB = stringBuffer.toString();
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            if (password.equals(passwordDB)) {
                UserLoginInfo userLoginInfo = new UserLoginInfo();
                userLoginInfo.setEmployeeId(user.getEmployee().getEmployeeId());
                userLoginInfo.setEmployeeFirstName(user.getEmployee().getEmployeeFirstName());
                userLoginInfo.setEmployeeLastName(user.getEmployee().getEmployeeLastName());

                if ("true".equals(user.getEmployee().getAdmin())) {
                    userLoginInfo.setRole(Roles.Admin);
                    if (!user.getEmployee().getEmployeeList().isEmpty()) {
                        userLoginInfo.setTeamlead(true);
                    }
                } else if (!user.getEmployee().getEmployeeList().isEmpty()) {
                    userLoginInfo.setRole(Roles.Teamlead);
                } else {
                    userLoginInfo.setRole(Roles.Employee);
                }

                return userLoginInfo;
            }
        }

        return null;
    }
}
