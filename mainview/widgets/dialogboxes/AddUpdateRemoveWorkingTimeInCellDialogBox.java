package com.ncedu.nc_project.client.mainview.widgets.dialogboxes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.ncedu.nc_project.client.MainRestService;
import com.ncedu.nc_project.client.mainview.widgets.DateToggleButton;
import com.ncedu.nc_project.client.mainview.widgets.EmployeeWeekInfoCellTable;
import com.ncedu.nc_project.client.mainview.widgets.WorkingDaysHorizontalPanel;
import com.ncedu.nc_project.client.mainview.widgets.WorkingTimeHorizontalPanel;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AddUpdateRemoveWorkingTimeInCellDialogBox extends DialogBox {
    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);

    public AddUpdateRemoveWorkingTimeInCellDialogBox(UserLoginInfo userLoginInfo, Integer row, Integer column, EmployeeWeekInfoCellTable employeeWeekInfoCellTable) {

        DialogBox boxForLogging = new DialogBox();
        boxForLogging.setStyleName("boxForLogging");
        boxForLogging.setGlassStyleName("glassStyle");

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(10);

        Label messageLabel = new Label();
        verticalPanel.add(messageLabel);

        Label messageLabel2 = new Label();
        verticalPanel.add(messageLabel2);

        messageLabel.setText(employeeWeekInfoCellTable.getVisibleItem(row).getEmployee().getEmployeeFirstName() + " " + employeeWeekInfoCellTable.getVisibleItem(row).getEmployee().getEmployeeLastName());

        WorkingDaysHorizontalPanel workingDaysHorizontalPanel = new WorkingDaysHorizontalPanel();
        workingDaysHorizontalPanel.setCurrentWeek();

        WorkingTimeHorizontalPanel workingTimeHorizontalPanel = new WorkingTimeHorizontalPanel();
        verticalPanel.add(workingTimeHorizontalPanel);




        HorizontalPanel buttonsHorizontalPanel = new HorizontalPanel();

        PushButton saveButton = new PushButton("Save");

        saveButton.addClickHandler(clickEvent -> {

            Date currentDay = workingDaysHorizontalPanel.getCurrentWeekWorkingDays().get(column - 2);

            int startHours = Integer.parseInt(workingTimeHorizontalPanel.getStartHourListBox().getSelectedValue());
            int startMinutes = Integer.parseInt(workingTimeHorizontalPanel.getStartMinutesListBox().getSelectedValue());
            int endHours = Integer.parseInt(workingTimeHorizontalPanel.getEndHourListBox().getSelectedValue());
            int endMinutes = Integer.parseInt(workingTimeHorizontalPanel.getEndMinutesListBox().getSelectedValue());

            if ((startHours == endHours && startMinutes >= endMinutes)
                    || startHours > endHours) {
                messageLabel2.setText("Please select a correct time interval!");
                messageLabel2.setVisible(true);
            } else {
                long minutes = 60 * 1000L;
                long hours = 60 * minutes;

                long enteredTimeStart = startHours * hours + startMinutes * minutes;
                long enteredTimeEnd = endHours * hours + endMinutes * minutes;

                WorkingTime workingTime = new WorkingTime(employeeWeekInfoCellTable.getVisibleItem(row).getEmployee()
                        ,new Date(currentDay.getTime() + enteredTimeStart),
                        new Date(currentDay.getTime() + enteredTimeEnd));

                List<WorkingTime> workingTimes = new ArrayList<>();
                workingTimes.add(workingTime);

                restService.saveWorkingTimes(workingTimes, new MethodCallback<Status>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Method method, Status status) {
                        if (-1 == status.getCode()) {
                            messageLabel2.setText(status.getMessage());
                            messageLabel2.setVisible(true);
                        } else {
                            boxForLogging.setGlassEnabled(true);
                            boxForLogging.setText("Working Time successfully added!");
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

        PushButton updateButton = new PushButton("Update");

        updateButton.addClickHandler(clickEvent -> {
            List<Date> selectedDays = new ArrayList<>();
            Date currentDay = workingDaysHorizontalPanel.getCurrentWeekWorkingDays().get(column - 2);
            selectedDays.add(currentDay);

            int startHours = Integer.parseInt(workingTimeHorizontalPanel.getStartHourListBox().getSelectedValue());
            int startMinutes = Integer.parseInt(workingTimeHorizontalPanel.getStartMinutesListBox().getSelectedValue());
            int endHours = Integer.parseInt(workingTimeHorizontalPanel.getEndHourListBox().getSelectedValue());
            int endMinutes = Integer.parseInt(workingTimeHorizontalPanel.getEndMinutesListBox().getSelectedValue());

            if ((startHours == endHours && startMinutes >= endMinutes)
                    || startHours > endHours) {
                messageLabel2.setText("Please select a correct time interval!");
                messageLabel2.setVisible(true);
            } else {
                long minutes = 60 * 1000L;
                long hours = 60 * minutes;

                long enteredTimeStart = startHours * hours + startMinutes * minutes;
                long enteredTimeEnd = endHours * hours + endMinutes * minutes;

                WorkingTime workingTime = new WorkingTime(employeeWeekInfoCellTable.getVisibleItem(row).getEmployee()
                        , new Date(currentDay.getTime() + enteredTimeStart),
                        new Date(currentDay.getTime() + enteredTimeEnd));

                List<WorkingTime> workingTimes = new ArrayList<>();
                workingTimes.add(workingTime);

                restService.updateWorkingTime(new EmployeeWithDays(selectedDays, employeeWeekInfoCellTable.getVisibleItem(row).getEmployee().getEmployeeId(),
                        workingTimes), new MethodCallback<Void>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Method method, Void aVoid) {
                        boxForLogging.setGlassEnabled(true);
                        boxForLogging.setText("Working Time successfully updated!");
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

        PushButton removeTimePushButton = new PushButton("Remove Time");

        removeTimePushButton.addClickHandler(clickEvent -> {
            List<Date> selectedDays = new ArrayList<>();
            Date currentDay = workingDaysHorizontalPanel.getCurrentWeekWorkingDays().get(column - 2);
            selectedDays.add(currentDay);

            restService.removeWorkingTimes(new EmployeeWithDays(selectedDays, employeeWeekInfoCellTable.getVisibleItem(row).getEmployee().getEmployeeId(),
                    null), new MethodCallback<Void>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {

                }

                @Override
                public void onSuccess(Method method, Void aVoid) {
                    boxForLogging.setGlassEnabled(true);
                    boxForLogging.setText("Working Time successfully removed!");
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

        });

        PushButton cancelButton = new PushButton("Cancel");
        cancelButton.addClickHandler(clickEvent -> hide());
        buttonsHorizontalPanel.add(saveButton);
        buttonsHorizontalPanel.add(updateButton);
        buttonsHorizontalPanel.add(removeTimePushButton);
        buttonsHorizontalPanel.add(cancelButton);

        verticalPanel.add(buttonsHorizontalPanel);

        setWidget(verticalPanel);
    }
}
