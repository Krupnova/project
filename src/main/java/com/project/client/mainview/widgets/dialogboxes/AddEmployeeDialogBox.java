package com.project.client.mainview.widgets.dialogboxes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import com.project.client.MainRestService;

import com.project.client.mainview.widgets.EmployeeListBox;
import com.project.shared.entities.Employee;
import com.project.shared.entities.Status;
import com.project.shared.entities.User;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;


import java.util.List;


public class AddEmployeeDialogBox extends DialogBox {

    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);
    private Employee employeeExport;
    private Employee employeeIdleader;
    public AddEmployeeDialogBox() {

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(10);

        TextBox firstNameTextBox = new TextBox();
        TextBox lastNameTextBox = new TextBox();
        TextBox emailTextBox = new TextBox();
        TextBox loginTextBox = new TextBox();
        TextBox passwordTextBox = new TextBox();
        EmployeeListBox employeesListBox = new EmployeeListBox();

        Grid grid = new Grid(6, 2);
        grid.setWidget(0, 0, new Label("First name:"));
        grid.setWidget(0, 1, firstNameTextBox);
        grid.setWidget(1, 0, new Label("Last name:"));
        grid.setWidget(1, 1, lastNameTextBox);
        grid.setWidget(2, 0, new Label("Email:"));
        grid.setWidget(2, 1, emailTextBox);
        grid.setWidget(3, 0, new Label("Login:"));
        grid.setWidget(3, 1, loginTextBox);
        grid.setWidget(4, 0, new Label("Password:"));
        grid.setWidget(4, 1, passwordTextBox);
        grid.setWidget(5, 0, new Label("Team Leader:"));
        grid.setWidget(5, 1, employeesListBox);


        restService.getAllEmployees(new MethodCallback<List<Employee>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {

            }

            @Override
            public void onSuccess(Method method, List<Employee> employees) {
                employeesListBox.setEmployees(employees);
            }
        });



        verticalPanel.add(grid);

        Label messageLabel = new Label();
        verticalPanel.add(messageLabel);

        HorizontalPanel buttonsHorizontalPanel = new HorizontalPanel();

        PushButton saveButton = new PushButton("Save");
        saveButton.addClickHandler(event -> {
            employeeIdleader = employeesListBox.getSelectedEmployee();
            String firstName = firstNameTextBox.getText();
            String lastName = lastNameTextBox.getText();
            String email = emailTextBox.getText();
            String login = loginTextBox.getText();
            String password = passwordTextBox.getText();


            final String FIRST_NAME_PATTERN = "^[a-zA-Z]{3,15}$";
            final String LAST_NAME_PATTERN = "^[a-zA-Z]{3,15}$";
            final String LOGIN_PATTERN = "^[a-zA-Z]{3,15}$";
            final String PASSWORD_PATTERN = "^[a-zA-Z]{3,15}$";

            if (firstName.isEmpty() || lastName.isEmpty() || login.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter all information about employee!");
            } else if (!firstName.matches(FIRST_NAME_PATTERN)) {
                messageLabel.setText("Please enter correct first name (3-15 english letters)!");
            } else if (!lastName.matches(LAST_NAME_PATTERN)) {
                messageLabel.setText("Please enter correct last name (3-15 english letters)!");
            } else if (!firstName.matches(LOGIN_PATTERN)) {
                messageLabel.setText("Please enter correct first name (3-15 english letters)!");
            } else if (!lastName.matches(PASSWORD_PATTERN)) {
                messageLabel.setText("Please enter correct last name (3-15 english letters)!");
            } else {
                
                employeeExport = new Employee(firstName, lastName);
                employeeExport.setTeamLead(employeeIdleader);
                employeeExport.setEmployeeEmail(email);
                restService.saveEmployee(employeeExport, new MethodCallback<Status>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Method method, Status status) {
                        if (-1 == status.getCode()) {
                            messageLabel.setText(status.getMessage());
                        } else {
                            hide();
                        }
                    }
                });
                restService.saveUser(new User(login, password), new MethodCallback<Status>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Method method, Status status) {
                        if (-1 == status.getCode()) {
                            messageLabel.setText(status.getMessage());
                        } else {
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

    public Employee getEmployeeExport() {
        return employeeExport;
    }
}
