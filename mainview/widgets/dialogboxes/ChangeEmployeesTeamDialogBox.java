package com.ncedu.nc_project.client.mainview.widgets.dialogboxes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.ncedu.nc_project.client.MainRestService;
import com.ncedu.nc_project.client.mainview.widgets.EmployeeListBox;
import com.ncedu.nc_project.shared.entities.Employee;
import com.ncedu.nc_project.shared.entities.Status;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeEmployeesTeamDialogBox extends DialogBox {

    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);
    private Employee employeeExport;

    public ChangeEmployeesTeamDialogBox() {

        DialogBox boxForLogging = new DialogBox();
        boxForLogging.setStyleName("boxForLogging");
        boxForLogging.setGlassStyleName("glassStyle");

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(10);

        EmployeeListBox employeesListBox = new EmployeeListBox();
        EmployeeListBox teamleadListBox = new EmployeeListBox();
        List<Employee> teamleadList = new ArrayList<>();

        restService.getAllEmployees(new MethodCallback<List<Employee>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {

            }

            @Override
            public void onSuccess(Method method, List<Employee> employees) {
                employeesListBox.setEmployees(employees.stream().filter(employee -> (employee.getTeamLead() != null) && (!employee.getAdmin()))
                        .collect(Collectors.toList()));
                teamleadListBox.setEmployees(employees.stream().filter(employee -> !employee.getEmployeeList().isEmpty())
                        .collect(Collectors.toList()));
            }
        });

        Label whoToChange = new Label("Choose whom to change the team:");
        Label whomToChange = new Label("Choose the team:");

        verticalPanel.add(whoToChange);
        verticalPanel.add(employeesListBox);
        verticalPanel.add(whomToChange);
        verticalPanel.add(teamleadListBox);

        Label messageLabel = new Label();
        verticalPanel.add(messageLabel);

        employeesListBox.addChangeHandler(changeEvent -> messageLabel.setText(""));
        teamleadListBox.addChangeHandler(changeEvent -> messageLabel.setText(""));

        HorizontalPanel buttonsHorizontalPanel = new HorizontalPanel();

        PushButton changeButton = new PushButton("Change");
        changeButton.addClickHandler(clickEvent -> {
            employeeExport = employeesListBox.getSelectedEmployee();
            Employee teamleadExport = teamleadListBox.getSelectedEmployee();
            if (employeeExport.getTeamLead().getEmployeeId() == teamleadExport.getEmployeeId()) {
                messageLabel.setText("Please select a correct employee or team lead!");
            } else {
                employeeExport.setTeamLead(teamleadExport);
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
                            boxForLogging.setText("Team of employee successfully changed!");
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
