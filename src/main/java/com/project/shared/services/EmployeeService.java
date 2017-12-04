package com.project.shared.services;

import com.project.shared.entities.Employee;

import java.util.List;


public interface EmployeeService {

    void saveEmployee(Employee employee) throws Exception;

    Employee findEmployeeById(long employeeId);

    void updateEmployee(Employee employee) throws Exception;

    void deleteEmployee(Long employeeId) throws Exception;

    List<Employee> getAllEmployees();

    Employee findEmployeeByFirstNameAndLastName(String firstName, String lastName);
}
