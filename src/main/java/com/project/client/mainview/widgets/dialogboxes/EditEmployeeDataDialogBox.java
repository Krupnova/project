package com.project.client.mainview.widgets.dialogboxes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import com.project.client.MainRestService;
import com.project.client.mainview.widgets.EmployeeListBox;
import com.project.shared.entities.Employee;
import com.project.shared.entities.Status;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;


public class EditEmployeeDataDialogBox extends DialogBox {

    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);
    private Employee employeeExport;

    public EditEmployeeDataDialogBox() {

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(10);

        EmployeeListBox employeesListBox = new EmployeeListBox();
        restService.getAllEmployees(new MethodCallback<List<Employee>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {

            }

            @Override
            public void onSuccess(Method method, List<Employee> employees) {
                employeesListBox.setEmployees(employees);
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

        HorizontalPanel buttonsHorizontalPanel = new HorizontalPanel();

        PushButton editButton = new PushButton("Edit");
        editButton.addClickHandler(clickEvent -> {

            String firstName = firstNameTextBox.getText();
            String lastName = lastNameTextBox.getText();

            final String FIRST_NAME_PATTERN = "^[a-zA-Z]{3,15}$";
            final String LAST_NAME_PATTERN = "^[a-zA-Z]{3,15}$";

            if (!firstName.matches(FIRST_NAME_PATTERN)) {
                messageLabel.setText("Please enter correct first name (3-15 english letters)!");
            } else if (!lastName.matches(LAST_NAME_PATTERN)) {
                messageLabel.setText("Please enter correct last name (3-15 english letters)!");
            } else {
                employeeExport = employeesListBox.getSelectedEmployee();
                employeeExport.setEmployeeFirstName(firstName);
                employeeExport.setEmployeeLastName(lastName);
                restService.updateEmployee(employeeExport, new MethodCallback<Status>() {
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
        buttonsHorizontalPanel.add(editButton);

        PushButton cancelButton = new PushButton("Cancel");
        cancelButton.addClickHandler(clickEvent -> hide());
        buttonsHorizontalPanel.add(cancelButton);

        verticalPanel.add(buttonsHorizontalPanel);

        setWidget(verticalPanel);
    }
}
