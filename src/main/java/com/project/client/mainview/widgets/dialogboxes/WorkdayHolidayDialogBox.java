package com.project.client.mainview.widgets.dialogboxes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.*;
import com.project.client.MainRestService;
import com.project.client.mainview.widgets.DateToggleButton;
import com.project.client.mainview.widgets.WorkingDaysHorizontalPanel;
import com.project.shared.entities.Status;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class WorkdayHolidayDialogBox extends DialogBox {

    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);

    public WorkdayHolidayDialogBox() {

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(10);

        RadioButton currentWeekRadioButton = new RadioButton("selectedWeek", "Current week");
        RadioButton nextWeekRadioButton = new RadioButton("selectedWeek", "Next week");

        Grid weekGrid = new Grid(1, 2);
        weekGrid.setWidget(0, 0, currentWeekRadioButton);
        weekGrid.setWidget(0, 1, nextWeekRadioButton);
        verticalPanel.add(weekGrid);

        WorkingDaysHorizontalPanel workingDaysHorizontalPanel = new WorkingDaysHorizontalPanel();

        currentWeekRadioButton.addClickHandler(clickEvent -> {
            workingDaysHorizontalPanel.setCurrentWeek();
        });

        nextWeekRadioButton.addClickHandler(clickEvent -> {
            workingDaysHorizontalPanel.setNextWeek();
        });

        currentWeekRadioButton.setValue(true);
        verticalPanel.add(workingDaysHorizontalPanel);

        HorizontalPanel workdayHolidayHorizontalPanel = new HorizontalPanel();

        RadioButton workdayRadioButton = new RadioButton("workday or holiday", "is workday");
        RadioButton holidayRadioButton = new RadioButton("workday or holiday", "is holiday");
        workdayHolidayHorizontalPanel.add(workdayRadioButton);
        workdayHolidayHorizontalPanel.add(holidayRadioButton);
        workdayRadioButton.setValue(true);

        verticalPanel.add(workdayHolidayHorizontalPanel);

        Label messageLabel = new Label();
        messageLabel.setVisible(false);
        verticalPanel.add(messageLabel);

        HorizontalPanel buttonsHorizontalPanel = new HorizontalPanel();

        PushButton saveButton = new PushButton("Save");
        saveButton.addClickHandler((ClickEvent clickEvent) -> {

            DateToggleButton[] selectedWeekButtonsList = currentWeekRadioButton.getValue()
                    ? workingDaysHorizontalPanel.getCurrentWeekButtonsList()
                    : workingDaysHorizontalPanel.getNextWeekButtonsList();
            List<Date> selectedDays = Arrays.stream(selectedWeekButtonsList)
                    .filter(DateToggleButton::isDown)
                    .map(DateToggleButton::getDate)
                    .collect(Collectors.toList());

            if (selectedDays.isEmpty()) {
                messageLabel.setText("Please select at least one day!");
                messageLabel.setVisible(true);
            } else if (selectedWeekButtonsList[6].isDown()) {
                messageLabel.setText("Sunday can not be a working day!");
                messageLabel.setVisible(true);
            } else {
                restService.saveWorkdaysHolidays(selectedDays, holidayRadioButton.getValue(),
                        new MethodCallback<Status>() {
                            @Override
                            public void onFailure(Method method, Throwable throwable) {
                            }

                            @Override
                            public void onSuccess(Method method, Status status) {
                                if (status.getCode() == -1) {
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