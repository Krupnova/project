package com.project.server.dao;

import com.project.shared.entities.Employee;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository("employeeDAO")
public class EmployeeDAO extends GenericDAO<Employee, Long> {

    public EmployeeDAO() {
        super(Employee.class);
    }

    public Employee findEmployeeByFirstNameAndLastName(String firstName, String lastName) {

        Query q = getCurrentSession()
                .createQuery("from Employee " +
                        "where employeeFirstName = :firstName " +
                        "and employeeLastName = :lastName ")
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName);

        try {
            return (Employee) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
