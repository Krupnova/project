package com.project.shared.services;

import com.project.shared.entities.Employee;
import com.project.shared.entities.WorkingTime;

import java.util.Date;
import java.util.List;


public interface WorkingTimeService {

    void saveWorkingTime(WorkingTime workingTime) throws Exception;

    WorkingTime findWorkingTimeById(long workingTimeId);

    void updateWorkingTime(WorkingTime workingTime) throws Exception;

    void deleteWorkingTime(Long id) throws Exception;

    List<WorkingTime> getAllWorkingTimes() throws Exception;

    List<WorkingTime> getWorkingTimesOfSelectedEmployee(Employee employee);

    List<WorkingTime> getWorkingTimesBetweenTwoDates(Employee employee, Date fromDate, Date toDate);

    List<WorkingTime> getWorkingTimesBetweenTwoDates(Date fromDate, Date toDate);
}
