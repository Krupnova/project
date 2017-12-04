package com.project.shared.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EmployeeWithDays implements Serializable {

    private List<Date> selectedDays;
    private Long employeeId;
    private List<WorkingTime> workingTimes;

    public EmployeeWithDays() {
    }

    public EmployeeWithDays(List<Date> selectedDays, Long employeeId, List<WorkingTime> workingTimes) {
        this.selectedDays = selectedDays;
        this.employeeId = employeeId;
        this.workingTimes = workingTimes;
    }

    public List<Date> getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(List<Date> selectedDays) {
        this.selectedDays = selectedDays;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public List<WorkingTime> getWorkingTimes() {
        return workingTimes;
    }

    public void setWorkingTimes(List<WorkingTime> workingTimes) {
        this.workingTimes = workingTimes;
    }
}
