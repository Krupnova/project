package com.project.client.loginview;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.project.client.MainRestService;
import com.project.client.mainview.MainView;
import com.project.shared.entities.UserLoginInfo;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class LoginView extends Composite {

    MainRestService restService = (MainRestService) GWT.create(MainRestService.class);

    public LoginView() {

        Grid grid = new Grid(2, 2);

        grid.setCellSpacing(10);

        TextBox userLogin = new TextBox();
        PasswordTextBox userPassword = new PasswordTextBox();
        Button loginButton = new Button("Login");

        VerticalPanel mainPanel = new VerticalPanel();

        userLogin.setWidth("150px");
        userPassword.setWidth("150px");

        grid.setWidget(0, 0, new Label("Username"));
        grid.setWidget(0, 1, userLogin);
        grid.setWidget(1, 0, new Label("Password"));
        grid.setWidget(1, 1, userPassword);

        Label infoLabel = new Label();

        mainPanel.add(grid);
        loginButton.addClickHandler(clickEvent -> {
            loginButton.setEnabled(false);

            Timer timer = new Timer() {
                @Override
                public void run() {
                    loginButton.setEnabled(true);
                }
            };

            timer.schedule(5000);

            String login = userLogin.getText();
            String password = userPassword.getText();

            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                byte[] array = messageDigest.digest(password.getBytes());
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < array.length; i++) {
                    stringBuffer.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
                    password = stringBuffer.toString();
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            Cookies.setCookie("login", login);
            Cookies.setCookie("password", password);

            restService.loginUser(login, password, new MethodCallback<UserLoginInfo>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {

                }

                @Override
                public void onSuccess(Method method, UserLoginInfo userLoginInfo) {
                    if (userLoginInfo != null) {
                        Cookies.setCookie("sessionID", String.valueOf(new Random().nextInt(999999)));


                        RootPanel.get("login_form").remove(mainPanel);
                        RootPanel.get().add(new MainView(userLoginInfo));
                    } else {
                        infoLabel.setText("Wrong login or password!");
                    }
                }
            });
        });
        mainPanel.add(infoLabel);
        mainPanel.add(loginButton);
        RootPanel.get("login_form").add(mainPanel);
    }
}
