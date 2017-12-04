package com.project.server.controllers;

import com.project.shared.entities.Employee;
import com.project.shared.entities.Status;
import com.project.shared.services.EmployeeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController {

    static final Logger logger = Logger.getLogger(RestController.class);
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/save_employee", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Status saveEmployee(@RequestBody Employee employee) {
        try {

            Employee employeeFromDB = employeeService.findEmployeeByFirstNameAndLastName(
                    employee.getEmployeeFirstName(),
                    employee.getEmployeeLastName()
            );

            if (employeeFromDB == null) {
                employeeService.saveEmployee(employee);
                return new Status(1, "Employee successfully saved.");
            } else {
                return new Status(-1, "This employee already exists in the database!");
            }
        } catch (Exception e) {
            return new Status(0, e.getMessage());
        }
    }

    @RequestMapping(value = "/update_employee", method = RequestMethod.POST)
    public
    @ResponseBody
    Status updateEmployee(@RequestBody Employee employee) {
        try {
            employeeService.updateEmployee(employee);
            return new Status(1, "Employee successfully updated.");
        } catch (Exception e) {
            return new Status(0, e.toString());
        }
    }

    @RequestMapping(value = "/delete_employee_{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Status deleteEmployee(@PathVariable("id") long id) {
        try {
            employeeService.deleteEmployee(id);
            return new Status(1, "Employee successfully deleted.");
        } catch (Exception e) {
            return new Status(0, e.toString());
        }
    }

    @RequestMapping(value = "/get_all_employees", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @RequestMapping(value = "/find_employee_{first_name}_{last_name}", method = RequestMethod.GET)
    public
    @ResponseBody
    Employee findEmployeeByFirstNameAndLastName(@PathVariable("first_name") String firstName, @PathVariable("last_name") String lastName) {
        return employeeService.findEmployeeByFirstNameAndLastName(firstName, lastName);
    }
}
