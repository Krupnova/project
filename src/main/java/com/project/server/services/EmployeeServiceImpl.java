package com.project.server.services;

import com.project.server.dao.EmployeeDAO;
import com.project.shared.entities.Employee;
import com.project.shared.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeDAO employeeDAO;

    @Override
    public void saveEmployee(Employee employee) throws Exception {
        employeeDAO.persist(employee);
    }

    @Override
    public Employee findEmployeeById(long employeeId) {
        return employeeDAO.findById(employeeId);
    }

    @Override
    public void updateEmployee(Employee employee) throws Exception {
        employeeDAO.update(employee);
    }

    @Override
    public void deleteEmployee(Long employeeId) throws Exception {
        employeeDAO.delete(findEmployeeById(employeeId));
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAll();
    }

    @Override
    public Employee findEmployeeByFirstNameAndLastName(String firstName, String lastName) {
        return employeeDAO.findEmployeeByFirstNameAndLastName(firstName, lastName);
    }
}
