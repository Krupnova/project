package com.project.client.mainview.widgets.dialogboxes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import com.project.client.MainRestService;
import com.project.client.mainview.widgets.EmployeeListBox;
import com.project.shared.entities.Employee;
import com.project.shared.entities.Roles;
import com.project.shared.entities.Status;
import com.project.shared.entities.UserLoginInfo;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.List;
public class AddEmployeeToTeamDialogBox extends DialogBox {

    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);
    private Employee employeeExport;
    private Employee employeeExportToAdd;

    public AddEmployeeToTeamDialogBox(UserLoginInfo userLoginInfo) {

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(10);

        EmployeeListBox employeesListBox = new EmployeeListBox();
        EmployeeListBox employeesListBoxToAdd = new EmployeeListBox();
        Label labelWhoToAdd = new Label("Choose who to add");
        Label labelWhomToAdd = new Label("Choose whom to add");

        if (userLoginInfo.getRole().equals(Roles.Admin)) {
            restService.getAllEmployees(new MethodCallback<List<Employee>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {

                }

                @Override
                public void onSuccess(Method method, List<Employee> employees) {
                    List<Employee> employeeList = new ArrayList<>();
                    for (Employee employee : employees) {
                        if (employee.getTeamLead() == null) {
                            employeeList.add(employee);
                        }
                    }
                    employeesListBox.setEmployees(employeeList);
                }
            });
            verticalPanel.add(labelWhomToAdd);
            verticalPanel.add(employeesListBox);
            verticalPanel.add(labelWhoToAdd);
        } else {
            restService.findEmployeeByFirstNameAndLastName(userLoginInfo.getEmployeeFirstName(),
                    userLoginInfo.getEmployeeLastName(), new MethodCallback<Employee>() {
                        @Override
                        public void onFailure(Method method, Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(Method method, Employee employee) {
                            employeeExport = employee;
                        }
                    });
        }

        restService.getAllEmployees(new MethodCallback<List<Employee>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {

            }

            @Override
            public void onSuccess(Method method, List<Employee> employees) {
                List<Employee> employeeListToAdd = new ArrayList<>();
                for (Employee employee : employees) {
                    if (!employee.getEmployeeList().isEmpty()) {
                        employees.remove(employee);
                    }
                }
                for (Employee employee : employees) {
                    if (employee.getTeamLead() == null) {
                        employeeListToAdd.add(employee);
                    }
                }
                if (userLoginInfo.getRole().equals(Roles.Admin)) {
                    for (Employee employee : employeeListToAdd) {
                        if (employee.getEmployeeId() == employeesListBox.getSelectedEmployee().getEmployeeId()) {
                            employeeListToAdd.remove(employee);
                        }
                    }
                }
                employeesListBoxToAdd.setEmployees(employeeListToAdd);
            }
        });

        verticalPanel.add(employeesListBoxToAdd);
        Label messageLabel = new Label();
        verticalPanel.add(messageLabel);

        HorizontalPanel buttonsHorizontalPanel = new HorizontalPanel();

        PushButton addButton = new PushButton("Add");
        addButton.addClickHandler(clickEvent -> {
            if (userLoginInfo.getRole().equals(Roles.Admin)) {
                employeeExport = employeesListBox.getSelectedEmployee();
                employeeExportToAdd = employeesListBoxToAdd.getSelectedEmployee();
            } else {
                employeeExportToAdd = employeesListBoxToAdd.getSelectedEmployee();
            }

            if ((employeeExport.getEmployeeId() == employeeExportToAdd.getEmployeeId())
                    && (userLoginInfo.getRole().equals(Roles.Admin))) {
                messageLabel.setText("Please select the correct employee!");
            } else {
                employeeExportToAdd.setTeamLead(employeeExport);
                restService.updateEmployee(employeeExportToAdd, new MethodCallback<Status>() {
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
        buttonsHorizontalPanel.add(addButton);

        PushButton cancelButton = new PushButton("Cancel");
        cancelButton.addClickHandler(clickEvent -> hide());
        buttonsHorizontalPanel.add(cancelButton);

        verticalPanel.add(buttonsHorizontalPanel);

        setWidget(verticalPanel);
    }
}
