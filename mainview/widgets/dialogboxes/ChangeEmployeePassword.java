package com.ncedu.nc_project.client.mainview.widgets.dialogboxes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.ncedu.nc_project.client.MainRestService;
import com.ncedu.nc_project.client.mainview.widgets.EmployeeListBox;
import com.ncedu.nc_project.shared.constants.ConstantsProvider;
import com.ncedu.nc_project.shared.entities.Employee;
import com.ncedu.nc_project.shared.entities.Status;
import com.ncedu.nc_project.shared.entities.User;
import com.ncedu.nc_project.shared.entities.UserLoginInfo;
import com.ncedu.nc_project.shared.utils.MD5Util;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class that represents the form of a pop-up window
 * that implements the function of changed the password of employee
 *
 * @author Sekachkin Mikhail
 */
public class ChangeEmployeePassword extends DialogBox {

    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);
    private User userExport;

    public ChangeEmployeePassword(UserLoginInfo userLoginInfo) {

        DialogBox boxForLogging = new DialogBox();
        boxForLogging.setStyleName("boxForLogging");
        boxForLogging.setGlassStyleName("glassStyle");

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(10);

        List<User> userList = new ArrayList<>();
        EmployeeListBox employeesListBox = new EmployeeListBox();

        restService.getAllUsers(new MethodCallback<List<User>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {

            }

            @Override
            public void onSuccess(Method method, List<User> users) {
                userList.addAll(users);
            }
        });

        restService.getAllEmployees(new MethodCallback<List<Employee>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {

            }

            @Override
            public void onSuccess(Method method, List<Employee> employees) {
                employees = employees.stream().filter(employee -> employee.getEmployeeId() != userLoginInfo.getEmployeeId())
                        .collect(Collectors.toList());
                employeesListBox.setEmployees(employees);
            }
        });

        PasswordTextBox passwordTextBoxFirst = new PasswordTextBox();
        PasswordTextBox passwordTextBoxSecond = new PasswordTextBox();

        Grid grid = new Grid(3, 2);
        grid.setWidget(0, 0, employeesListBox);
        grid.setWidget(1, 0, new Label("New password:"));
        grid.setWidget(1, 1, passwordTextBoxFirst);
        grid.setWidget(2, 0, new Label("Repeat new password:"));
        grid.setWidget(2, 1, passwordTextBoxSecond);

        verticalPanel.add(grid);

        Label messageLabel = new Label();
        verticalPanel.add(messageLabel);

        HorizontalPanel buttonsHorizontalPanel = new HorizontalPanel();

        PushButton changeButton = new PushButton("Change");
        changeButton.addClickHandler(clickEvent -> {

            String firstPassword = passwordTextBoxFirst.getText();
            String secondPassword = passwordTextBoxSecond.getText();

            if (firstPassword.isEmpty() || secondPassword.isEmpty()) {
                messageLabel.setText("Please enter all information about password!");
            } else if (!firstPassword.matches(ConstantsProvider.PASSWORD_PATTERN)) {
                messageLabel.setText("Please enter correct new first password (3-20 english letters and digits)!");
            } else if (!secondPassword.matches(ConstantsProvider.PASSWORD_PATTERN)) {
                messageLabel.setText("Please enter correct new second password (3-20 english letters and digits)!");
            } else if (firstPassword.compareTo(secondPassword) != 0) {
                messageLabel.setText("New passwords do not match!");
            } else {
                Employee employeeHelper = employeesListBox.getSelectedEmployee();
                for (User user : userList) {
                    if (user.getUserId() == employeeHelper.getEmployeeId()) {
                        userExport = user;
                    }
                }
                userExport.setUserPassword(MD5Util.md5Hex(firstPassword));
                restService.updateUser(userExport, new MethodCallback<Status>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Method method, Status status) {
                        if (status.getCode() == 0) {
                            messageLabel.setText(status.getMessage());
                        } else {
                            boxForLogging.setGlassEnabled(true);
                            boxForLogging.setText("Employee password successfully changed!");
                            Timer timer = new Timer() {
                                @Override
                                public void run() {
                                    boxForLogging.hide();
                                }
                            };
                            timer.schedule(2000);
                            boxForLogging.center();
                            hide();
                        }
                    }
                });

            }
        });
        buttonsHorizontalPanel.add(changeButton);

        PushButton cancelButton = new PushButton("Cancel");
        cancelButton.addClickHandler(event -> hide());
        buttonsHorizontalPanel.add(cancelButton);

        verticalPanel.add(buttonsHorizontalPanel);

        setWidget(verticalPanel);
    }
}
