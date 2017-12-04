package com.ncedu.nc_project.client.mainview.widgets.dialogboxes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import com.ncedu.nc_project.client.MainRestService;
import com.ncedu.nc_project.client.mainview.widgets.DateToggleButton;
import com.ncedu.nc_project.client.mainview.widgets.EmployeeListBox;
import com.ncedu.nc_project.client.mainview.widgets.WorkingDaysHorizontalPanel;
import com.ncedu.nc_project.shared.entities.Employee;
import com.ncedu.nc_project.shared.entities.EmployeeWithDays;
import com.ncedu.nc_project.shared.entities.UserLoginInfo;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by apars on 12.05.2017.
 */
public class RepeatWorkingTimeDialogBox extends DialogBox {

    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);

    public RepeatWorkingTimeDialogBox(UserLoginInfo userLoginInfo) {

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(10);

        EmployeeListBox employeesListBox = new EmployeeListBox();
        List<Employee> employeeList = new ArrayList<>();

        restService.findEmployeeByFirstNameAndLastName(userLoginInfo.getEmployeeFirstName(), userLoginInfo.getEmployeeLastName(), new MethodCallback<Employee>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {

            }

            @Override
            public void onSuccess(Method method, Employee employee) {
                employeeList.addAll(employee.getEmployeeList());
                employeesListBox.setEmployees(employeeList);
            }
        });

        RadioButton currentWeekRadioButton = new RadioButton("selectedWeek", "Current week");
        RadioButton nextWeekRadioButton = new RadioButton("selectedWeek", "Next week");

        Grid employeeAndWeekGrid = new Grid(1, 3);
        employeeAndWeekGrid.setWidget(0, 0, employeesListBox);
        employeeAndWeekGrid.setWidget(0, 1, currentWeekRadioButton);
        employeeAndWeekGrid.setWidget(0, 2, nextWeekRadioButton);
        verticalPanel.add(employeeAndWeekGrid);

        WorkingDaysHorizontalPanel workingDaysHorizontalPanel = new WorkingDaysHorizontalPanel();

        currentWeekRadioButton.addClickHandler(clickEvent -> {
            workingDaysHorizontalPanel.setCurrentWeek();
        });

        nextWeekRadioButton.addClickHandler(clickEvent -> {
            workingDaysHorizontalPanel.setNextWeek();
        });

        currentWeekRadioButton.setValue(true);

        verticalPanel.add(workingDaysHorizontalPanel);

        Label messageLabel = new Label();
        messageLabel.setVisible(false);
        verticalPanel.add(messageLabel);

        HorizontalPanel buttonsHorizontalPanel = new HorizontalPanel();

        PushButton rejectButton = new PushButton("Reject");
        rejectButton.addClickHandler(clickEvent -> {

            DateToggleButton[] selectedWeekButtonsList = currentWeekRadioButton.getValue()
                    ? workingDaysHorizontalPanel.getCurrentWeekButtonsList()
                    : workingDaysHorizontalPanel.getNextWeekButtonsList();
            List<Date> selectedDays = Arrays.stream(selectedWeekButtonsList)
                    .filter(DateToggleButton::isDown)
                    .map(DateToggleButton::getDate)
                    .collect(Collectors.toList());

            Employee selectedEmployee = employeesListBox.getSelectedEmployee();

            restService.removeWorkingTimes(new EmployeeWithDays(selectedDays, selectedEmployee.getEmployeeId(), null), new MethodCallback<Void>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {

                }

                @Override
                public void onSuccess(Method method, Void aVoid) {
                    hide();
                }
            });
        });
        buttonsHorizontalPanel.add(rejectButton);

        PushButton cancelButton = new PushButton("Cancel");
        cancelButton.addClickHandler(clickEvent -> hide());
        buttonsHorizontalPanel.add(cancelButton);

        verticalPanel.add(buttonsHorizontalPanel);

        setWidget(verticalPanel);
    }
}
