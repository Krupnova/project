package com.ncedu.nc_project.client.mainview.widgets;

import com.google.gwt.user.client.ui.ListBox;
import com.ncedu.nc_project.shared.entities.Employee;

import java.util.List;

public class EmployeeListBox extends ListBox {

    private List<Employee> employees;

    public EmployeeListBox() {
    }

    public EmployeeListBox(List<Employee> employees) {
        setEmployees(employees);
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
        employees.stream()
                .map(e -> e.getEmployeeFirstName() + " " + e.getEmployeeLastName())
                .forEach(this::addItem);
    }

    public Employee getSelectedEmployee() {

        String[] selectedValue = getSelectedValue().split(" ");
        String selectedFirstName = selectedValue[0];
        String selectedLastName = selectedValue[1];

        return employees.stream()
                .filter(employee -> selectedFirstName.equals(employee.getEmployeeFirstName())
                        && selectedLastName.equals(employee.getEmployeeLastName()))
                .findFirst()
                .orElse(null);
    }
}
