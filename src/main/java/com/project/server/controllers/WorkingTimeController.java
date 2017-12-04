package com.project.server.controllers;

import com.project.shared.entities.Status;
import com.project.shared.entities.WorkingTime;
import com.project.shared.services.EmployeeService;
import com.project.shared.services.WorkingTimeService;
import com.project.shared.entities.Employee;
import com.project.shared.entities.EmployeeWithDays;
import org.apache.log4j.Logger;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class WorkingTimeController {

    static final Logger logger = Logger.getLogger(RestController.class);
    @Autowired
    private WorkingTimeService workingTimeService;
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/save_working_times", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Status saveWorkingTimes(@RequestBody List<WorkingTime> workingTimesFromClient) {

        try {

//        All working times belong to the same employee
            Employee employee = workingTimesFromClient.get(0).getEmployee();
            List<WorkingTime> workingTimesFromDB = workingTimeService.getWorkingTimesOfSelectedEmployee(employee);
            List<Interval> intervals = workingTimesFromDB.stream()
                    .map(workingTime -> new Interval(workingTime.getWorkingTimeStart().getTime(),
                            workingTime.getWorkingTimeEnd().getTime()))
                    .collect(Collectors.toList());

            for (WorkingTime workingTimeFromClient : workingTimesFromClient) {

                Interval intervalFromClient = new Interval(workingTimeFromClient.getWorkingTimeStart().getTime(),
                        workingTimeFromClient.getWorkingTimeEnd().getTime());

                if (intervals.stream().anyMatch(intervalFromClient::overlaps)) {
                    return new Status(-1,
                            "Selected time interval overlaps the interval(s) at least in one of the selected days");
                }
            }

            for (WorkingTime workingTime : workingTimesFromClient) {
                workingTimeService.saveWorkingTime(workingTime);
            }

            return new Status(1, "Working times successfully saved.");
        } catch (Exception e) {
            return new Status(0, e.getMessage());
        }
    }

    @RequestMapping(value = "/remove_working_times", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    void removeWorkingTimes(@RequestBody EmployeeWithDays employeeWithDays) {

        Calendar calendar = Calendar.getInstance();
        Date nextDay;
        List<WorkingTime> workingTimesBetweenTwoDates;

        Employee selectedEmployee = employeeService.findEmployeeById(employeeWithDays.getEmployeeId());

        for (Date selectedDay : employeeWithDays.getSelectedDays()) {
            calendar.setTime(selectedDay);
            calendar.add(Calendar.DATE, 1); //add 1 day
            nextDay = calendar.getTime();

            workingTimesBetweenTwoDates = workingTimeService.getWorkingTimesBetweenTwoDates(selectedEmployee, selectedDay, nextDay);

            for (WorkingTime workingTime : workingTimesBetweenTwoDates) {
                try {
                    workingTimeService.deleteWorkingTime(workingTime.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping(value = "/update_working_time", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    void updateWorkingTimes(@RequestBody EmployeeWithDays employeeWithDays) {

        removeWorkingTimes(employeeWithDays);
        saveWorkingTimes(employeeWithDays.getWorkingTimes());
    }

    @RequestMapping(value = "/save_as_workday_or_holiday_{is_holiday}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Status saveWorkdaysHolidays(@RequestBody List<Date> selectedDays, @PathVariable("is_holiday") boolean isHoliday) {

        Calendar calendar = Calendar.getInstance();

        try {
            List<Employee> employees = employeeService.getAllEmployees();
            employees.forEach(employee -> {
                for (Date selectDay : selectedDays) {
                    calendar.setTime(selectDay);
                    calendar.add(Calendar.DATE, 1);
                    Date nextDay = calendar.getTime();

                    List<WorkingTime> workingTimes = workingTimeService.getWorkingTimesBetweenTwoDates(employee,
                            selectDay, nextDay);

                    if (!workingTimes.isEmpty()) {
                        if (workingTimes.get(0).isHoliday() == isHoliday) {
                            continue;
                        } else {
                            removeWorkingTimes(new EmployeeWithDays(java.util.Arrays.asList(selectDay), employee.getEmployeeId(), null));
                        }
                    }

                    calendar.setTime(selectDay);
                    if (isHoliday != (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {

                        long minutes = 60 * 1000L;
                        long hours = 60 * minutes;

                        long timeStart = 8 * hours;
                        long timeEnd = 16 * hours;

                        try {
                            workingTimeService.saveWorkingTime(new WorkingTime(employee, new Date(selectDay.getTime()
                                    + timeStart), new Date(selectDay.getTime() + timeEnd), isHoliday));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            return new Status(0, "Holidays successfully saved.");
        } catch (Exception e) {
            return new Status(-1, e.getMessage());
        }
    }
}