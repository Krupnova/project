package com.project.client.mainview.widgets.dialogboxes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.*;
import com.project.client.MainRestService;
import com.project.shared.entities.Status;
import com.project.shared.entities.User;
import com.project.shared.entities.UserLoginInfo;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

public class ChangePasswordDialogBox extends DialogBox {
    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);

    User user;

    ChangePasswordDialogBox(UserLoginInfo userLoginInfo){
        user = null;

        VerticalPanel verticalPanel = new VerticalPanel();
        //расстояние
        verticalPanel.setSpacing(10);

        Label messageLabel = new Label();
        verticalPanel.add(messageLabel);

        //добавляю табличку, где можно поменять пароль с кнопкой принятия изменений
        Grid grid = new Grid(3,2);
        grid.setCellSpacing(10);

        PasswordTextBox oldUserPassword = new PasswordTextBox();
        PasswordTextBox newUserPassword1 = new PasswordTextBox();
        PasswordTextBox newUserPassword2 = new PasswordTextBox();

        PushButton changePswrdButton = new PushButton("Change");

        grid.setWidget(0,0, new Label("Old password: "));
        grid.setWidget(0,1, oldUserPassword);
        grid.setWidget(1,0, new Label("New password: "));
        grid.setWidget(1,1, newUserPassword1);
        grid.setWidget(2,0, new Label("Repeat new password: "));
        grid.setWidget(2,1, newUserPassword2);

        verticalPanel.add(grid);

        changePswrdButton.addClickHandler((ClickEvent clickEvent) -> {
            String oldPassword = oldUserPassword.getText();
            String newPassword1 = newUserPassword1.getText();
            String newPassword2 = newUserPassword2.getText();

            //доступные буквы для ввода пароля
            final String PASSWORD_PATTERN = "^[a-zA-Z0-9]{3,15}$";

            if (oldPassword.isEmpty() || newPassword1.isEmpty() || newPassword2.isEmpty()) {
                messageLabel.setText("Please enter all information about password!");
            } else if (!oldPassword.matches(PASSWORD_PATTERN)) {
                messageLabel.setText("Please enter correct old password (3-15 english letters and digits)!");
            } else if (!newPassword1.matches(PASSWORD_PATTERN)) {
                messageLabel.setText("Please enter correct new first password (3-15 english letters and digits)!");
            } else if (!newPassword2.matches(PASSWORD_PATTERN)) {
                messageLabel.setText("Please enter correct new second password (3-15 english letters and digits)!");
            } else if(newPassword1.compareTo(newPassword2) != 0) {
                messageLabel.setText("New passwords do not match!");
            } else if(oldPassword.compareTo(newPassword1) == 0) {
                messageLabel.setText("Old and new passwords must not match!");
            } else {
                restService.findUserByLogin(Cookies.getCookie("login"), new MethodCallback<User>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Method method, User usr) {
                        if(usr != null) {
                            user = usr;
                        } else {
                            messageLabel.setText("User is not found!");
                        }
                    }
                });

                if(user != null){
                    if (oldPassword.compareTo(user.getUserPassword()) == 0) {
                        user.setUserPassword(newPassword1);
                        restService.updateUser(user, new MethodCallback<Status>() {
                            @Override
                            public void onFailure(Method method, Throwable throwable) {
                                messageLabel.setText("Fail update user.");
                            }

                            @Override
                            public void onSuccess(Method method, Status status) {
                                if (status.getCode() == 0) {
                                    messageLabel.setText(status.getMessage());
                                } else {
                                    Cookies.removeCookie("sessionID");
                                    Cookies.removeCookie("login");
                                    Cookies.removeCookie("password");

                                    hide();
                                }
                            }
                        });
                    } else {
                        messageLabel.setText("Old and current passwords are incorrect!");
                    }
                }
            }
        });




        //добавляю на нижнюю горизонтальную панель кнопку cancel
        HorizontalPanel buttonsHorizontalPanel = new HorizontalPanel();

        PushButton cancelButton = new PushButton("Cancel");
        cancelButton.addClickHandler(clickEvent -> hide());
        buttonsHorizontalPanel.add(changePswrdButton);
        buttonsHorizontalPanel.add(cancelButton);

        verticalPanel.add(buttonsHorizontalPanel);

        setWidget(verticalPanel);
    }
}
