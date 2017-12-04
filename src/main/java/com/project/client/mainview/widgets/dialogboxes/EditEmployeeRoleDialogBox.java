package com.project.client.mainview.widgets.dialogboxes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import com.project.client.MainRestService;
import com.project.client.mainview.widgets.EmployeeListBox;
import com.project.shared.entities.Employee;
import com.project.shared.entities.Status;
import com.project.shared.entities.UserLoginInfo;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class EditEmployeeRoleDialogBox extends DialogBox {

    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);
    private Employee employeeExport;

    public EditEmployeeRoleDialogBox(UserLoginInfo userLoginInfo) {

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(10);

        EmployeeListBox employeesListBox = new EmployeeListBox();
        restService.getAllEmployees(new MethodCallback<List<Employee>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {

            }

            @Override
            public void onSuccess(Method method, List<Employee> employees) {
                for (Employee employee : employees) {
                    if (employee.getEmployeeId() == userLoginInfo.getEmployeeId()) {
                        employees.remove(employee);
                    }
                }
                employeesListBox.setEmployees(employees);
            }
        });

        Label messageLabel = new Label();

        verticalPanel.add(employeesListBox);
        RadioButton makeAdminRadioButton = new RadioButton("selectedRole", "Make Admin");
        RadioButton unMakeAdminRadioButton = new RadioButton("selectedRole", "Unmake Admin");

        makeAdminRadioButton.setVisible(false);
        unMakeAdminRadioButton.setVisible(false);

        Grid grid = new Grid(1, 1);

        employeesListBox.addChangeHandler(changeEvent -> {
            employeeExport = employeesListBox.getSelectedEmployee();
            makeAdminRadioButton.setValue(false);
            unMakeAdminRadioButton.setValue(false);
            if ("true".equals(employeeExport.getAdmin())) {
                grid.setWidget(0, 0, unMakeAdminRadioButton);
                unMakeAdminRadioButton.setVisible(true);
            } else {
                grid.setWidget(0, 0, makeAdminRadioButton);
                makeAdminRadioButton.setVisible(true);
            }
        });


        PushButton editButton = new PushButton("Edit");
        editButton.addClickHandler(clickEvent -> {
            if ((!unMakeAdminRadioButton.getValue()) && (!makeAdminRadioButton.getValue())) {
                messageLabel.setText("Please select at least one role!");
            } else if (unMakeAdminRadioButton.getValue()) {
                employeeExport.setAdmin(null);
            } else if (makeAdminRadioButton.getValue()) {
                employeeExport.setAdmin("true");
            }

            if (messageLabel.getText().equals("")) {
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
        verticalPanel.add(grid);
        verticalPanel.add(messageLabel);

        HorizontalPanel buttonsHorizontalPanel = new HorizontalPanel();

        buttonsHorizontalPanel.add(editButton);

        PushButton cancelButton = new PushButton("Cancel");
        cancelButton.addClickHandler(clickEvent -> hide());
        buttonsHorizontalPanel.add(cancelButton);

        verticalPanel.add(buttonsHorizontalPanel);

        setWidget(verticalPanel);
    }
}