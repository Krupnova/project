package com.project.client.mainview.widgets.dialogboxes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.project.client.MainRestService;
import com.project.client.mainview.widgets.EmployeeListBox;
import com.project.shared.constants.ConstantsProvider;
import com.project.shared.entities.Employee;
import com.project.shared.entities.Status;
import com.project.shared.entities.UserLoginInfo;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;
import java.util.stream.Collectors;


public class ChangeEmployeeNameDialogBox extends DialogBox {

    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);
    private Employee employeeExport;

    public ChangeEmployeeNameDialogBox(UserLoginInfo userLoginInfo) {

        DialogBox boxForLogging = new DialogBox();
        boxForLogging.setStyleName("boxForLogging");
        boxForLogging.setGlassStyleName("glassStyle");

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(10);

        EmployeeListBox employeesListBox = new EmployeeListBox();
        restService.getAllEmployees(new MethodCallback<List<Employee>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {

            }

            @Override
            public void onSuccess(Method method, List<Employee> employees) {
                employeesListBox.setEmployees(employees.stream().filter(employee -> employee.getEmployeeId() != userLoginInfo.getEmployeeId())
                        .collect(Collectors.toList()));
            }
        });

        TextBox firstNameTextBox = new TextBox();
        TextBox lastNameTextBox = new TextBox();

        Grid editGrid = new Grid(3, 2);
        editGrid.setWidget(0, 0, employeesListBox);
        editGrid.setWidget(1, 0, new Label("First name:"));
        editGrid.setWidget(1, 1, firstNameTextBox);
        editGrid.setWidget(2, 0, new Label("Last name:"));
        editGrid.setWidget(2, 1, lastNameTextBox);
        verticalPanel.add(editGrid);

        Label messageLabel = new Label();
        verticalPanel.add(messageLabel);

        firstNameTextBox.addChangeHandler(changeEvent -> messageLabel.setText(""));
        lastNameTextBox.addChangeHandler(changeEvent -> messageLabel.setText(""));

        HorizontalPanel buttonsHorizontalPanel = new HorizontalPanel();

        PushButton changeButton = new PushButton("Change");
        changeButton.addClickHandler(clickEvent -> {

            String firstName = firstNameTextBox.getText();
            String lastName = lastNameTextBox.getText();

            if (!firstName.matches(ConstantsProvider.FIRST_NAME_PATTERN)) {
                messageLabel.setText("Please enter correct first name (3-15 english letters)!");
            } else if (!lastName.matches(ConstantsProvider.LAST_NAME_PATTERN)) {
                messageLabel.setText("Please enter correct last name (3-15 english letters)!");
            } else {
                employeeExport = employeesListBox.getSelectedEmployee();
                employeeExport.setEmployeeFirstName(firstName);
                employeeExport.setEmployeeLastName(lastName);
                restService.findEmployeeByFirstNameAndLastName(firstName, lastName, new MethodCallback<Employee>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Method method, Employee employee) {
                        if (employee == null) {
                            restService.updateEmployee(employeeExport, new MethodCallback<Status>() {
                                @Override
                                public void onFailure(Method method, Throwable throwable) {

                                }

                                @Override
                                public void onSuccess(Method method, Status status) {
                                    if (-1 == status.getCode()) {
                                        messageLabel.setText(status.getMessage());
                                    } else {
                                        boxForLogging.setGlassEnabled(true);
                                        boxForLogging.setText("Name and surname of employee successfully changed!");
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
                        } else {
                            messageLabel.setText("This employee already exists in the database!");
                        }
                    }
                });

            }
        });
        buttonsHorizontalPanel.add(changeButton);

        PushButton cancelButton = new PushButton("Cancel");
        cancelButton.addClickHandler(clickEvent -> hide());
        buttonsHorizontalPanel.add(cancelButton);

        verticalPanel.add(buttonsHorizontalPanel);

        setWidget(verticalPanel);
    }
}
