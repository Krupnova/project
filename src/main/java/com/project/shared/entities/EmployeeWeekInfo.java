package com.project.shared.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class EmployeeWeekInfo implements Serializable {

    private Employee employee;
    private List<WorkingTime> monday;
    private List<WorkingTime> tuesday;
    private List<WorkingTime> wednesday;
    private List<WorkingTime> thursday;
    private List<WorkingTime> friday;
    private List<WorkingTime> saturday;
    private List<WorkingTime> sunday;
    private int hoursPerWeek;
    private int minutesPerWeek;
    private int holidaysCount;

    public EmployeeWeekInfo() {
    }

    public EmployeeWeekInfo(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<WorkingTime> getMonday() {
        return monday;
    }

    public void setMonday(List<WorkingTime> monday) {
        this.monday = monday;
    }

    public List<WorkingTime> getTuesday() {
        return tuesday;
    }

    public void setTuesday(List<WorkingTime> tuesday) {
        this.tuesday = tuesday;
    }

    public List<WorkingTime> getWednesday() {
        return wednesday;
    }

    public void setWednesday(List<WorkingTime> wednesday) {
        this.wednesday = wednesday;
    }

    public List<WorkingTime> getThursday() {
        return thursday;
    }

    public void setThursday(List<WorkingTime> thursday) {
        this.thursday = thursday;
    }

    public List<WorkingTime> getFriday() {
        return friday;
    }

    public void setFriday(List<WorkingTime> friday) {
        this.friday = friday;
    }

    public List<WorkingTime> getSaturday() {
        return saturday;
    }

    public void setSaturday(List<WorkingTime> saturday) {
        this.saturday = saturday;
    }

    public List<WorkingTime> getSunday() {
        return sunday;
    }

    public void setSunday(List<WorkingTime> sunday) {
        this.sunday = sunday;
    }

    public int getHoursPerWeek() {
        return hoursPerWeek;
    }

    public void setHoursPerWeek(int hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

    public int getMinutesPerWeek() {
        return minutesPerWeek;
    }

    public void setMinutesPerWeek(int minutesPerWeek) {
        this.minutesPerWeek = minutesPerWeek;
    }

    public int getHolidaysCount() {
        return holidaysCount;
    }

    public void setHolidaysCount(int holidaysCount) {
        this.holidaysCount = holidaysCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeWeekInfo that = (EmployeeWeekInfo) o;
        return hoursPerWeek == that.hoursPerWeek &&
                minutesPerWeek == that.minutesPerWeek &&
                Objects.equals(employee, that.employee) &&
                Objects.equals(monday, that.monday) &&
                Objects.equals(tuesday, that.tuesday) &&
                Objects.equals(wednesday, that.wednesday) &&
                Objects.equals(thursday, that.thursday) &&
                Objects.equals(friday, that.friday) &&
                Objects.equals(saturday, that.saturday) &&
                Objects.equals(sunday, that.sunday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, monday, tuesday, wednesday, thursday, friday, saturday, sunday, hoursPerWeek, minutesPerWeek);
    }

    @Override
    public String toString() {
        return "EmployeeWeekInfo{" +
                "employee=" + employee +
                ", monday=" + monday +
                ", tuesday=" + tuesday +
                ", wednesday=" + wednesday +
                ", thursday=" + thursday +
                ", friday=" + friday +
                ", saturday=" + saturday +
                ", sunday=" + sunday +
                ", hoursPerWeek=" + hoursPerWeek +
                ", minutesPerWeek=" + minutesPerWeek +
                '}';
    }
}
