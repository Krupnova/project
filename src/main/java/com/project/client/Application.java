package com.project.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.RootPanel;
import com.project.client.loginview.LoginView;
import com.project.client.mainview.MainView;
import com.project.shared.entities.UserLoginInfo;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

public class Application implements EntryPoint {


    MainRestService restService = (MainRestService) GWT.create(MainRestService.class);

    @Override
    public void onModuleLoad() {
        if (Cookies.getCookieNames().contains("sessionID")) {
            restService.loginUser(Cookies.getCookie("login"), Cookies.getCookie("password"), new MethodCallback<UserLoginInfo>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {

                }

                @Override
                public void onSuccess(Method method, UserLoginInfo userLoginInfo) {
                    if (userLoginInfo != null) {
                        RootPanel.get().add(new MainView(userLoginInfo));
                    }
                }
            });
        } else {
            RootPanel.get().add(new LoginView());
        }

    }
}
