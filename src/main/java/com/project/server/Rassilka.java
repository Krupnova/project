package com.project.server;

import com.project.shared.entities.Employee;
import com.project.shared.entities.EmployeeWeekInfo;
import com.project.shared.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class Rassilka {
    @Autowired
    EmployeeService employeeService;
    @Scheduled(cron = "0 0 8 * * 2")
    public void rassilka(){
        String msg="";
        List<Employee> employees = employeeService.getAllEmployees();

        for (Employee employee : employees) {
            EmployeeWeekInfo employeeWeekInfo = new EmployeeWeekInfo(employee);
            int h=employeeWeekInfo.getHoursPerWeek();
            if (h<20){
                msg="Hello "+employee.getEmployeeFirstName()+" "+employee.getEmployeeLastName()+
                        " you need fill in the working time for the last week";
                new Messageee("alexsmit19@rambler.ru","alexsmit19").send("Hello",msg,employee.getEmployeeEmail());
            }
        }}
}
