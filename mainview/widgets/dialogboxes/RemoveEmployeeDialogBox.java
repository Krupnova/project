package com.ncedu.nc_project.client.mainview.widgets.dialogboxes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.ncedu.nc_project.client.MainRestService;
import com.ncedu.nc_project.client.mainview.widgets.EmployeeListBox;
import com.ncedu.nc_project.shared.entities.Employee;
import com.ncedu.nc_project.shared.entities.User;
import com.ncedu.nc_project.shared.entities.UserLoginInfo;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

/**
 * Created by Nikita on 15.04.2017.
 */

public class RemoveEmployeeDialogBox extends DialogBox {

    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);
    private long employeeIdExport;
    private UserLoginInfo userLoginInfo;

    public RemoveEmployeeDialogBox() {

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
                for (Employee employee : employees) {
                    if (employee.getEmployeeId() == userLoginInfo.getEmployeeId()) {
                        employees.remove(employee);
                    }
                }
                employeesListBox.setEmployees(employees);
            }
        });
        verticalPanel.add(employeesListBox);

        HorizontalPanel buttonsHorizontalPanel = new HorizontalPanel();

        PushButton removeButton = new PushButton("Remove");
        removeButton.addClickHandler(clickEvent -> {
            long selectedEmployeeId = employeesListBox.getSelectedEmployee().getEmployeeId();
            employeeIdExport = selectedEmployeeId;
            restService.deleteEmployee(selectedEmployeeId, new MethodCallback<Employee>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {

                }

                @Override
                public void onSuccess(Method method, Employee employee) {
                    restService.deleteUser(employeeIdExport, new MethodCallback<User>() {
                        @Override
                        public void onFailure(Method method, Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(Method method, User user) {
                            boxForLogging.setGlassEnabled(true);
                            boxForLogging.setText("Employee successfully removed!");
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
                    });
                }
            });

        });
        buttonsHorizontalPanel.add(removeButton);

        PushButton cancelButton = new PushButton("Cancel");
        cancelButton.addClickHandler(clickEvent -> hide());
        buttonsHorizontalPanel.add(cancelButton);

        verticalPanel.add(buttonsHorizontalPanel);

        setWidget(verticalPanel);
    }

    public void setUserLoginInfo(UserLoginInfo userLoginInfo) {
        this.userLoginInfo = userLoginInfo;
    }

    public long getEmployeeIdExport() {
        return employeeIdExport;
    }
}
