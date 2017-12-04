package com.project.server.dao;

import com.project.shared.entities.Employee;
import com.project.shared.entities.WorkingTime;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository("workingTimeDAO")
public class WorkingTimeDAO extends GenericDAO<WorkingTime, Long> {

    public WorkingTimeDAO() {
        super(WorkingTime.class);
    }

    @SuppressWarnings("unchecked")
    public List<WorkingTime> getWorkingTimesOfSelectedEmployee(Employee employee) {

        Query q = getCurrentSession()
                .createQuery("from WorkingTime where employee = :employee ")
                .setParameter("employee", employee);

        List<WorkingTime> list = (List<WorkingTime>) q.getResultList();
        return list == null ? new ArrayList<>() : list;
    }

    @SuppressWarnings("unchecked")
    public List<WorkingTime> getWorkingTimesBetweenTwoDates(Employee employee, Date fromDate, Date toDate) {

        Query q = getCurrentSession()
                .createQuery("from WorkingTime " +
                        "where employee = :employee " +
                        "and workingTimeStart > :fromDate " +
                        "and workingTimeEnd < :toDate ")
                .setParameter("employee", employee)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate);

        List<WorkingTime> list = (List<WorkingTime>) q.getResultList();
        return list == null ? new ArrayList<>() : list;
    }

    @SuppressWarnings("unchecked")
    public List<WorkingTime> getWorkingTimesBetweenTwoDates(Date fromDate, Date toDate) {

        Query q = getCurrentSession()
                .createQuery("from WorkingTime " +
                        "where workingTimeStart > :fromDate " +
                        "and workingTimeEnd < :toDate ")
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate);

        List<WorkingTime> list = (List<WorkingTime>) q.getResultList();

        return list.isEmpty() ? new ArrayList<>() : list;
    }
}
