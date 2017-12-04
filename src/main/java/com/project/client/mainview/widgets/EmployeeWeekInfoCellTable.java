package com.project.client.mainview.widgets;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.project.shared.entities.EmployeeWeekInfo;
import com.project.shared.entities.WorkingTime;

import java.util.Date;
import java.util.List;

public class EmployeeWeekInfoCellTable extends CellTable<EmployeeWeekInfo> {

    private String getWorkingHoursFromWorkingTimeList(List<WorkingTime> workingTimeList) {

        if (workingTimeList == null || workingTimeList.isEmpty()) {
            return "";
        } else {
            DateTimeFormat timeFormat = DateTimeFormat.getFormat("HH:mm");
            StringBuilder stringBuilder = new StringBuilder();
            for (WorkingTime workingTime : workingTimeList) {
                if (workingTime.isHoliday()) {
                    stringBuilder.append("Holiday");
                    break;
                } else {
                    stringBuilder.append(timeFormat.format(workingTime.getWorkingTimeStart()))
                            .append("-")
                            .append(timeFormat.format(workingTime.getWorkingTimeEnd()))
                            .append("\n");
                }
            }
            return stringBuilder.toString();
        }
    }

    public void init(List<Date> dateList) {

        while (getColumnCount() > 0) {
            removeColumn(0);
        }

        TextColumn<EmployeeWeekInfo> firstNameColumn = new TextColumn<EmployeeWeekInfo>() {
            @Override
            public String getValue(EmployeeWeekInfo object) {
                return object.getEmployee().getEmployeeFirstName();
            }
        };

        TextColumn<EmployeeWeekInfo> lastNameColumn = new TextColumn<EmployeeWeekInfo>() {
            @Override
            public String getValue(EmployeeWeekInfo object) {
                return object.getEmployee().getEmployeeLastName();
            }
        };

        TextColumn<EmployeeWeekInfo> mondayColumn = new TextColumn<EmployeeWeekInfo>() {
            @Override
            public String getValue(EmployeeWeekInfo object) {
                return getWorkingHoursFromWorkingTimeList(object.getMonday());
            }
        };

        TextColumn<EmployeeWeekInfo> tuesdayColumn = new TextColumn<EmployeeWeekInfo>() {
            @Override
            public String getValue(EmployeeWeekInfo object) {
                return getWorkingHoursFromWorkingTimeList(object.getTuesday());
            }
        };

        TextColumn<EmployeeWeekInfo> wednesdayColumn = new TextColumn<EmployeeWeekInfo>() {
            @Override
            public String getValue(EmployeeWeekInfo object) {
                return getWorkingHoursFromWorkingTimeList(object.getWednesday());
            }
        };

        TextColumn<EmployeeWeekInfo> thursdayColumn = new TextColumn<EmployeeWeekInfo>() {
            @Override
            public String getValue(EmployeeWeekInfo object) {
                return getWorkingHoursFromWorkingTimeList(object.getThursday());
            }
        };

        TextColumn<EmployeeWeekInfo> fridayColumn = new TextColumn<EmployeeWeekInfo>() {
            @Override
            public String getValue(EmployeeWeekInfo object) {
                return getWorkingHoursFromWorkingTimeList(object.getFriday());
            }
        };

        TextColumn<EmployeeWeekInfo> saturdayColumn = new TextColumn<EmployeeWeekInfo>() {
            @Override
            public String getValue(EmployeeWeekInfo object) {
                return getWorkingHoursFromWorkingTimeList(object.getSaturday());
            }
        };

        TextColumn<EmployeeWeekInfo> sundayColumn = new TextColumn<EmployeeWeekInfo>() {
            @Override
            public String getValue(EmployeeWeekInfo object) {
                return getWorkingHoursFromWorkingTimeList(object.getSunday());
            }
        };

        TextColumn<EmployeeWeekInfo> weeklyTotalColumn = new TextColumn<EmployeeWeekInfo>() {
            @Override
            public String getValue(EmployeeWeekInfo object) {
                return NumberFormat.getFormat("00 hours ").format(object.getHoursPerWeek())
                        + NumberFormat.getFormat("00 minutes").format(object.getMinutesPerWeek());
            }
        };

        DateTimeFormat dayMonthFormat = DateTimeFormat.getFormat("dd MMMM, EEEE");

        addColumn(firstNameColumn, "First name");
        addColumn(lastNameColumn, "Last name");
        addColumn(mondayColumn, dayMonthFormat.format(dateList.get(0)));
        addColumn(tuesdayColumn, dayMonthFormat.format(dateList.get(1)));
        addColumn(wednesdayColumn, dayMonthFormat.format(dateList.get(2)));
        addColumn(thursdayColumn, dayMonthFormat.format(dateList.get(3)));
        addColumn(fridayColumn, dayMonthFormat.format(dateList.get(4)));
        addColumn(saturdayColumn, dayMonthFormat.format(dateList.get(5)));
        addColumn(sundayColumn, dayMonthFormat.format(dateList.get(6)));
        addColumn(weeklyTotalColumn, "Weekly total");
    }

    public int rateWorkPerWeek(EmployeeWeekInfo employeeWeekInfo) {
        return 20 - (employeeWeekInfo.getHolidaysCount() * 4);
    }

    public void fulfill(List<EmployeeWeekInfo> employeeWeekInfoList) {
        setRowCount(employeeWeekInfoList.size());
        setRowData(0, employeeWeekInfoList);
        for (int i = 0; i < employeeWeekInfoList.size(); i++) {
            EmployeeWeekInfo employeeWeekInfo = employeeWeekInfoList.get(i);
            if (employeeWeekInfo.getHoursPerWeek() < rateWorkPerWeek(employeeWeekInfo)) {
                getRowElement(i).addClassName("redRow");
            }

            if (!employeeWeekInfoList.get(i).getMonday().isEmpty())
                if (employeeWeekInfoList.get(i).getMonday().get(0).getWorkingTimeStart().toString().substring(11, 13).equals("00"))
                    getRowElement(i).addClassName("rejectRow");
            if (!employeeWeekInfoList.get(i).getTuesday().isEmpty())
                if (employeeWeekInfoList.get(i).getTuesday().get(0).getWorkingTimeStart().toString().substring(11, 13).equals("00"))
                    getRowElement(i).addClassName("rejectRow");
            if (!employeeWeekInfoList.get(i).getWednesday().isEmpty())
                if (employeeWeekInfoList.get(i).getWednesday().get(0).getWorkingTimeStart().toString().substring(11, 13).equals("00"))
                    getRowElement(i).addClassName("rejectRow");
            if (!employeeWeekInfoList.get(i).getThursday().isEmpty())
                if (employeeWeekInfoList.get(i).getThursday().get(0).getWorkingTimeStart().toString().substring(11, 13).equals("00"))
                    getRowElement(i).addClassName("rejectRow");
            if (!employeeWeekInfoList.get(i).getFriday().isEmpty())
                if (employeeWeekInfoList.get(i).getFriday().get(0).getWorkingTimeStart().toString().substring(11, 13).equals("00"))
                    getRowElement(i).addClassName("rejectRow");
            if (!employeeWeekInfoList.get(i).getSaturday().isEmpty())
                if (employeeWeekInfoList.get(i).getSaturday().get(0).getWorkingTimeStart().toString().substring(11, 13).equals("00"))
                    getRowElement(i).addClassName("rejectRow");
            if (!employeeWeekInfoList.get(i).getSunday().isEmpty())
                if (employeeWeekInfoList.get(i).getSunday().get(0).getWorkingTimeStart().toString().substring(11, 13).equals("00"))
                    getRowElement(i).addClassName("rejectRow");
        }
    }
}
