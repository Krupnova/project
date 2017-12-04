package com.project.server.controllers;

import com.project.shared.entities.*;
import com.project.shared.services.EmployeeService;
import com.project.shared.services.WorkingTimeService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class UtilController {

    static final Logger logger = Logger.getLogger(RestController.class);
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private WorkingTimeService workingTimeService;

    @RequestMapping(value = "/calendar_{week_number}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Date> getCalendarList(@PathVariable("week_number") int weekNumber) {
        List<Date> calendarList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        if (weekNumber == -1) {
            calendar.setTime(new Date());
        } else {
            calendar.set(Calendar.WEEK_OF_YEAR, weekNumber);
        }
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        for (int i = 0; i < 7; i++) {
            calendarList.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_WEEK, 1); //Прибавляем сутки
        }

        return calendarList;
    }

    @RequestMapping(value = "/employee_week_info_role_{week_number}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    List<EmployeeWeekInfo> getEmployeeWeekInfoRole(@PathVariable("week_number") int weekNumber, @RequestBody UserLoginInfo userLoginInfo) {

        List<EmployeeWeekInfo> employeeWeekInfoList = new ArrayList<>();

        List<Date> days = getCalendarList(weekNumber);
        List<Employee> employees = new ArrayList<>();

        Employee currentEmployee = employeeService.findEmployeeById(userLoginInfo.getEmployeeId());

        switch (userLoginInfo.getRole()) {
            case Admin:
                for (Employee employee : employeeService.getAllEmployees()) {
                    employees.add(employee);
                }
                break;
            case Teamlead:
                employees.add(currentEmployee);
                for (Employee employee : currentEmployee.getEmployeeList()) {
                    employees.add(employee);
                }
                break;
            case Employee:
                employees.add(currentEmployee);
                break;
        }

        for (Employee employee : employees) {
            EmployeeWeekInfo employeeWeekInfo = new EmployeeWeekInfo(employee);

            employeeWeekInfo.setMonday(workingTimeService.getWorkingTimesBetweenTwoDates(employee, days.get(0), days.get(1)));
            employeeWeekInfo.setTuesday(workingTimeService.getWorkingTimesBetweenTwoDates(employee, days.get(1), days.get(2)));
            employeeWeekInfo.setWednesday(workingTimeService.getWorkingTimesBetweenTwoDates(employee, days.get(2), days.get(3)));
            employeeWeekInfo.setThursday(workingTimeService.getWorkingTimesBetweenTwoDates(employee, days.get(3), days.get(4)));
            employeeWeekInfo.setFriday(workingTimeService.getWorkingTimesBetweenTwoDates(employee, days.get(4), days.get(5)));
            employeeWeekInfo.setSaturday(workingTimeService.getWorkingTimesBetweenTwoDates(employee, days.get(5), days.get(6)));

            int holidaysCount = 0;
            for (int i = 0; i < 6; i++) {
                List<WorkingTime> wt = workingTimeService.getWorkingTimesBetweenTwoDates(employee, days.get(i), days.get(i + 1));
                if (!wt.isEmpty()) {
                    if (wt.get(0).isHoliday()) {
                        holidaysCount++;
                    }
                }
            }

            employeeWeekInfo.setHolidaysCount(holidaysCount);

            Date sunday = days.get(6);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sunday);
            calendar.add(Calendar.DATE, 1); //add 1 day
            Date dayAfterSunday = calendar.getTime();
            employeeWeekInfo.setSunday(workingTimeService.getWorkingTimesBetweenTwoDates(employee, days.get(6), dayAfterSunday));

            countAndSetHoursWorkedPerWeek(employeeWeekInfo);

            employeeWeekInfoList.add(employeeWeekInfo);
        }

        return employeeWeekInfoList;
    }

    @RequestMapping(value = "/employee_week_info_{week_number}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<EmployeeWeekInfo> getEmployeeWeekInfo(@PathVariable("week_number") int weekNumber) {

        List<EmployeeWeekInfo> employeeWeekInfoList = new ArrayList<>();

        List<Date> days = getCalendarList(weekNumber);
        List<Employee> employees = employeeService.getAllEmployees();

        for (Employee employee : employees) {
            EmployeeWeekInfo employeeWeekInfo = new EmployeeWeekInfo(employee);

            employeeWeekInfo.setMonday(workingTimeService.getWorkingTimesBetweenTwoDates(employee, days.get(0), days.get(1)));
            employeeWeekInfo.setTuesday(workingTimeService.getWorkingTimesBetweenTwoDates(employee, days.get(1), days.get(2)));
            employeeWeekInfo.setWednesday(workingTimeService.getWorkingTimesBetweenTwoDates(employee, days.get(2), days.get(3)));
            employeeWeekInfo.setThursday(workingTimeService.getWorkingTimesBetweenTwoDates(employee, days.get(3), days.get(4)));
            employeeWeekInfo.setFriday(workingTimeService.getWorkingTimesBetweenTwoDates(employee, days.get(4), days.get(5)));
            employeeWeekInfo.setSaturday(workingTimeService.getWorkingTimesBetweenTwoDates(employee, days.get(5), days.get(6)));

            int holidaysCount = 0;
            for (int i = 0; i < 6; i++) {
                List<WorkingTime> wt = workingTimeService.getWorkingTimesBetweenTwoDates(employee, days.get(i), days.get(i + 1));
                if (!wt.isEmpty()) {
                    if (wt.get(0).isHoliday()) {
                        holidaysCount++;
                    }
                }
            }

            employeeWeekInfo.setHolidaysCount(holidaysCount);

            Date sunday = days.get(6);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sunday);
            calendar.add(Calendar.DATE, 1); //add 1 day
            Date dayAfterSunday = calendar.getTime();
            employeeWeekInfo.setSunday(workingTimeService.getWorkingTimesBetweenTwoDates(employee, days.get(6), dayAfterSunday));

            countAndSetHoursWorkedPerWeek(employeeWeekInfo);

            employeeWeekInfoList.add(employeeWeekInfo);
        }

        return employeeWeekInfoList;
    }

    private Period getHoursWorkedPerDay(List<WorkingTime> workingTimeList) {

        Period result = new Period();

        for (WorkingTime workingTime : workingTimeList) {
            if (!workingTime.isHoliday()) {
                DateTime startTime = new DateTime(workingTime.getWorkingTimeStart());
                DateTime endTime = new DateTime(workingTime.getWorkingTimeEnd());

                result = result.plus(new Period(startTime, endTime));
            }
        }

        return result;
    }

    private void countAndSetHoursWorkedPerWeek(EmployeeWeekInfo employeeWeekInfo) {

        Period result = getHoursWorkedPerDay(employeeWeekInfo.getMonday())
                .plus(getHoursWorkedPerDay(employeeWeekInfo.getTuesday()))
                .plus(getHoursWorkedPerDay(employeeWeekInfo.getWednesday()))
                .plus(getHoursWorkedPerDay(employeeWeekInfo.getThursday()))
                .plus(getHoursWorkedPerDay(employeeWeekInfo.getFriday()))
                .plus(getHoursWorkedPerDay(employeeWeekInfo.getSaturday()))
                .plus(getHoursWorkedPerDay(employeeWeekInfo.getSunday()))
                .normalizedStandard();

        int hours = result.getDays() * 24 + result.getHours();
        int minutes = result.getMinutes();

        employeeWeekInfo.setHoursPerWeek(hours);
        employeeWeekInfo.setMinutesPerWeek(minutes);
    }

    @RequestMapping(value = "/week_info", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Integer> getCurrentWeekNumberAndWeeksInYear() {

        List<Integer> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        list.add(calendar.get(Calendar.WEEK_OF_YEAR));
        list.add(calendar.getWeeksInWeekYear());

        return list;
    }

    @RequestMapping(value = "/working_week_{week_number}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<WorkingDayInfo> getWorkingWeek(@PathVariable("week_number") int weekNumber) {
        List<WorkingDayInfo> workingDayInfoList = new ArrayList<>();

        List<Date> datesList = getCalendarList(weekNumber);

        List<WorkingTime> workingTimes;

        for (int i = 0; i < 5; i++) {
            workingTimes = workingTimeService.getWorkingTimesBetweenTwoDates(datesList.get(i), datesList.get(i + 1));
            workingDayInfoList.add(new WorkingDayInfo(datesList.get(i),
                    workingTimes.isEmpty() ? false : workingTimes.get(0).isHoliday()));
        }

        workingTimes = workingTimeService.getWorkingTimesBetweenTwoDates(datesList.get(5), datesList.get(6));
        workingDayInfoList.add(new WorkingDayInfo(datesList.get(5),
                workingTimes.isEmpty() ? true : workingTimes.get(0).isHoliday()));

        workingDayInfoList.add(new WorkingDayInfo(datesList.get(6), true));

        return workingDayInfoList;
    }
}
