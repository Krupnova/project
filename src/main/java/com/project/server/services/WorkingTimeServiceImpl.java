package com.project.server.services;

import com.project.server.dao.WorkingTimeDAO;
import com.project.shared.entities.Employee;
import com.project.shared.entities.WorkingTime;
import com.project.shared.services.WorkingTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("workingTimeService")
public class WorkingTimeServiceImpl implements WorkingTimeService {

    @Autowired
    private WorkingTimeDAO workingTimeDAO;

    @Override
    public void saveWorkingTime(WorkingTime workingTime) throws Exception {
        workingTimeDAO.persist(workingTime);
    }

    @Override
    public WorkingTime findWorkingTimeById(long workingTimeId) {
        return workingTimeDAO.findById(workingTimeId);
    }

    @Override
    public void updateWorkingTime(WorkingTime workingTime) throws Exception {
        workingTimeDAO.update(workingTime);
    }

    @Override
    public void deleteWorkingTime(Long id) throws Exception {
        workingTimeDAO.delete(findWorkingTimeById(id));
    }

    @Override
    public List<WorkingTime> getAllWorkingTimes() throws Exception {
        return workingTimeDAO.getAll();
    }

    @Override
    public List<WorkingTime> getWorkingTimesOfSelectedEmployee(Employee employee) {
        return workingTimeDAO.getWorkingTimesOfSelectedEmployee(employee);
    }

    @Override
    public List<WorkingTime> getWorkingTimesBetweenTwoDates(Employee employee, Date fromDate, Date toDate) {
        return workingTimeDAO.getWorkingTimesBetweenTwoDates(employee, fromDate, toDate);
    }

    @Override
    public List<WorkingTime> getWorkingTimesBetweenTwoDates(Date fromDate, Date toDate) {
        return workingTimeDAO.getWorkingTimesBetweenTwoDates(fromDate, toDate);
    }
}
