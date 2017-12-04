package com.project.client.mainview.widgets.dialogboxes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import com.project.client.MainRestService;
import com.project.client.mainview.widgets.DateToggleButton;
import com.project.client.mainview.widgets.EmployeeListBox;
import com.project.client.mainview.widgets.WorkingDaysHorizontalPanel;
import com.project.client.mainview.widgets.WorkingTimeHorizontalPanel;
import com.project.shared.entities.*;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class AddWorkingTimeDialogBox extends DialogBox {

    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);
    private Employee employeeExport;

    public AddWorkingTimeDialogBox(UserLoginInfo userLoginInfo) {

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(10);

        EmployeeListBox employeesListBox = new EmployeeListBox();
        List<Long> employeeIdList = new ArrayList<>();

        restService.getAllEmployees(new MethodCallback<List<Employee>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {

            }

            @Override
            public void onSuccess(Method method, List<Employee> employees) {
                restService.findEmployeeByFirstNameAndLastName(userLoginInfo.getEmployeeFirstName(), userLoginInfo.getEmployeeLastName(), new MethodCallback<Employee>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Method method, Employee employee) {
                        employeeExport = employee;
                        employeeIdList.add(employee.getEmployeeId());

                        for (Employee emp : employee.getEmployeeList()) {
                            employeeIdList.add(emp.getEmployeeId());
                        }
                    }
                });
                employeesListBox.setEmployees(employees);
            }
        });

        RadioButton currentWeekRadioButton = new RadioButton("selectedWeek", "Current week");
        RadioButton nextWeekRadioButton = new RadioButton("selectedWeek", "Prev week");

        Grid employeeAndWeekGrid = new Grid(1, 3);
        if (userLoginInfo.getRole().equals(Roles.Admin)) {
            employeeAndWeekGrid.setWidget(0, 0, employeesListBox);
        }
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

        WorkingTimeHorizontalPanel workingTimeHorizontalPanel = new WorkingTimeHorizontalPanel();

        verticalPanel.add(workingTimeHorizontalPanel);

        Label messageLabel = new Label();
        messageLabel.setVisible(false);
        verticalPanel.add(messageLabel);

        HorizontalPanel buttonsHorizontalPanel = new HorizontalPanel();

        PushButton saveButton = new PushButton("Save");
        saveButton.addClickHandler(clickEvent -> {

            DateToggleButton[] selectedWeekButtonsList = currentWeekRadioButton.getValue()
                    ? workingDaysHorizontalPanel.getCurrentWeekButtonsList()
                    : workingDaysHorizontalPanel.getNextWeekButtonsList();
            List<Date> selectedDays = Arrays.stream(selectedWeekButtonsList)
                    .filter(DateToggleButton::isDown)
                    .map(DateToggleButton::getDate)
                    .collect(Collectors.toList());

            int startHours = Integer.parseInt(workingTimeHorizontalPanel.getStartHourListBox().getSelectedValue());
            int startMinutes = Integer.parseInt(workingTimeHorizontalPanel.getStartMinutesListBox().getSelectedValue());
            int endHours = Integer.parseInt(workingTimeHorizontalPanel.getEndHourListBox().getSelectedValue());
            int endMinutes = Integer.parseInt(workingTimeHorizontalPanel.getEndMinutesListBox().getSelectedValue());

            if (selectedDays.isEmpty()) {
                messageLabel.setText("Please select at least one day!");
                messageLabel.setVisible(true);
            } else if ((startHours == endHours && startMinutes >= endMinutes)
                    || startHours > endHours) {
                messageLabel.setText("Please select a correct time interval!");
                messageLabel.setVisible(true);
            } else if (!workingDaysHorizontalPanel.getCurrentWeekWorkingDays().containsAll(selectedDays) &&
                    !workingDaysHorizontalPanel.getNextWeekWorkingDays().containsAll(selectedDays)) {
                messageLabel.setText("You can not choose a holiday!");
                messageLabel.setVisible(true);
            } else {

                long minutes = 60 * 1000L;
                long hours = 60 * minutes;

                long enteredTimeStart = startHours * hours + startMinutes * minutes;
                long enteredTimeEnd = endHours * hours + endMinutes * minutes;

                if (userLoginInfo.getRole().equals(Roles.Admin)) {
                    employeeExport = employeesListBox.getSelectedEmployee();
                }

                List<WorkingTime> workingTimes = selectedDays.stream()
                        .map(selectedDay -> new WorkingTime(employeeExport,
                                new Date(selectedDay.getTime() + enteredTimeStart),
                                new Date(selectedDay.getTime() + enteredTimeEnd)))
                        .collect(Collectors.toList());

                restService.saveWorkingTimes(workingTimes, new MethodCallback<Status>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Method method, Status status) {
                        if (-1 == status.getCode()) {
                            messageLabel.setText(status.getMessage());
                            messageLabel.setVisible(true);
                        } else {
                            hide();
                        }
                    }
                });
            }
        });
        buttonsHorizontalPanel.add(saveButton);

        PushButton cancelButton = new PushButton("Cancel");
        cancelButton.addClickHandler(clickEvent -> hide());
        buttonsHorizontalPanel.add(cancelButton);

        verticalPanel.add(buttonsHorizontalPanel);

        setWidget(verticalPanel);
    }
}
