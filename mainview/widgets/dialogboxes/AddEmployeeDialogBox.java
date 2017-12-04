package com.ncedu.nc_project.client.mainview.widgets.dialogboxes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.ncedu.nc_project.client.MainRestService;
import com.ncedu.nc_project.client.mainview.widgets.EmployeeListBox;
import com.ncedu.nc_project.shared.constants.ConstantsProvider;
import com.ncedu.nc_project.shared.entities.Employee;
import com.ncedu.nc_project.shared.entities.Status;
import com.ncedu.nc_project.shared.utils.MD5Util;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nikita on 15.04.2017.
 */

public class AddEmployeeDialogBox extends DialogBox {

    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);
    private Employee employeeExport;
    private Employee employeeIdLeader;

    public AddEmployeeDialogBox() {

        DialogBox boxForLogging = new DialogBox();
        boxForLogging.setStyleName("boxForLogging");
        boxForLogging.setGlassStyleName("glassStyle");

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(10);

        TextBox firstNameTextBox = new TextBox();
        TextBox lastNameTextBox = new TextBox();
        TextBox loginTextBox = new TextBox();
        PasswordTextBox passwordTextBox = new PasswordTextBox();
        EmployeeListBox employeesListBox = new EmployeeListBox();

        Grid grid = new Grid(5, 2);
        grid.setWidget(0, 0, new Label("First name:"));
        grid.setWidget(0, 1, firstNameTextBox);
        grid.setWidget(1, 0, new Label("Last name:"));
        grid.setWidget(1, 1, lastNameTextBox);
        grid.setWidget(2, 0, new Label("Login:"));
        grid.setWidget(2, 1, loginTextBox);
        grid.setWidget(3, 0, new Label("Password:"));
        grid.setWidget(3, 1, passwordTextBox);
        grid.setWidget(4, 0, new Label("Team Leader:"));
        grid.setWidget(4, 1, employeesListBox);

        restService.getAllEmployees(new MethodCallback<List<Employee>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {

            }

            @Override
            public void onSuccess(Method method, List<Employee> employees) {
                employees = employees.stream().filter(employee -> !employee.getEmployeeList().isEmpty())
                        .collect(Collectors.toList());
                employeesListBox.setEmployees(employees);
            }
        });

        verticalPanel.add(grid);

        Label messageLabel = new Label();
        verticalPanel.add(messageLabel);

        HorizontalPanel buttonsHorizontalPanel = new HorizontalPanel();

        PushButton saveButton = new PushButton("Save");
        saveButton.addClickHandler(event -> {
            employeeIdLeader = employeesListBox.getSelectedEmployee();
            String firstName = firstNameTextBox.getText();
            String lastName = lastNameTextBox.getText();
            String login = loginTextBox.getText();
            String password = passwordTextBox.getText();

            if (firstName.isEmpty() || lastName.isEmpty() || login.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter all information about employee!");
            } else if (!firstName.matches(ConstantsProvider.FIRST_NAME_PATTERN)) {
                messageLabel.setText("Please enter correct first name (3-15 english letters)!");
            } else if (!lastName.matches(ConstantsProvider.LAST_NAME_PATTERN)) {
                messageLabel.setText("Please enter correct last name (3-15 english letters)!");
            } else if (!login.matches(ConstantsProvider.LOGIN_PATTERN)) {
                messageLabel.setText("Please enter correct password (3-15 english letters or digits)!");
            } else if (!password.matches(ConstantsProvider.PASSWORD_PATTERN)) {
                messageLabel.setText("Please enter correct password (3-20 english letters or digits)!");
            } else {
                employeeExport = new Employee(firstName, lastName);
                employeeExport.setTeamLead(employeeIdLeader);
                restService.addNewEmployeeAndUser(employeeExport, login, MD5Util.md5Hex(password), new MethodCallback<Status>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Method method, Status status) {
                        if (-1 == status.getCode()) {
                            messageLabel.setText(status.getMessage());
                        } else {
                            boxForLogging.setGlassEnabled(true);
                            boxForLogging.setText("Employee successfully added!");
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
        buttonsHorizontalPanel.add(saveButton);

        PushButton cancelButton = new PushButton("Cancel");
        cancelButton.addClickHandler(event -> hide());
        buttonsHorizontalPanel.add(cancelButton);

        verticalPanel.add(buttonsHorizontalPanel);

        setWidget(verticalPanel);
    }
}
