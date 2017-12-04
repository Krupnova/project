package com.project.client.mainview.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.project.client.MainRestService;
import com.project.shared.entities.WorkingDayInfo;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkingDaysHorizontalPanel extends HorizontalPanel {

    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);
    private DateToggleButton[] currentWeekButtonsList;
    private DateToggleButton[] nextWeekButtonsList;
    private List<Date> currentWeekWorkingDays;
    private List<Date> nextWeekWorkingDays;

    public WorkingDaysHorizontalPanel() {
        currentWeekButtonsList = new DateToggleButton[7];
        for (int i = 0; i < currentWeekButtonsList.length; i++) {
            currentWeekButtonsList[i] = new DateToggleButton();
        }

        nextWeekButtonsList = new DateToggleButton[7];
        for (int i = 0; i < nextWeekButtonsList.length; i++) {
            nextWeekButtonsList[i] = new DateToggleButton();
        }

        currentWeekWorkingDays = new ArrayList<>();
        nextWeekWorkingDays = new ArrayList<>();

        restService.getCurrentWeekNumberAndWeeksInYear(new MethodCallback<List<Integer>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {

            }

            @Override
            public void onSuccess(Method method, List<Integer> list) {

                int currentWeekNumber = list.get(0);

                restService.getWorkingWeek(currentWeekNumber, new MethodCallback<List<WorkingDayInfo>>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Method method, List<WorkingDayInfo> workingDayInfoList) {
                        for (int i = 0; i < currentWeekButtonsList.length; i++) {
                            currentWeekButtonsList[i].setDate(workingDayInfoList.get(i).getDate());
                            if (!workingDayInfoList.get(i).getHoliday()) {
                                currentWeekWorkingDays.add(workingDayInfoList.get(i).getDate());
                            } else {
                                currentWeekButtonsList[i].addStyleName("holiday");
                            }
                        }
                    }
                });

                restService.getWorkingWeek(currentWeekNumber - 1, new MethodCallback<List<WorkingDayInfo>>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Method method, List<WorkingDayInfo> workingDayInfoList) {
                        for (int i = 0; i < nextWeekButtonsList.length; i++) {
                            nextWeekButtonsList[i].setDate(workingDayInfoList.get(i).getDate());
                            if (!workingDayInfoList.get(i).getHoliday()) {
                                nextWeekWorkingDays.add(workingDayInfoList.get(i).getDate());
                            } else {
                                nextWeekButtonsList[i].addStyleName("holiday");
                            }
                        }
                    }
                });
            }
        });
        for (DateToggleButton dateToggleButton : currentWeekButtonsList) {
            add(dateToggleButton);
        }
    }

    public DateToggleButton[] getCurrentWeekButtonsList() {
        return currentWeekButtonsList;
    }

    public DateToggleButton[] getNextWeekButtonsList() {
        return nextWeekButtonsList;
    }

    public List<Date> getCurrentWeekWorkingDays() {
        return currentWeekWorkingDays;
    }

    public List<Date> getNextWeekWorkingDays() {
        return nextWeekWorkingDays;
    }

    public void setCurrentWeek() {
        clear();
        for (DateToggleButton dateToggleButton : currentWeekButtonsList) {
            add(dateToggleButton);
        }
    }

    public void setNextWeek() {
        clear();
        for (DateToggleButton dateToggleButton : nextWeekButtonsList) {
            add(dateToggleButton);
        }
    }
}
