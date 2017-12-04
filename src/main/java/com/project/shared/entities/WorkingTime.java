package com.project.shared.entities;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "working_time")
public class WorkingTime implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne()
    @Cascade({CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "employee_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Employee employee;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "working_time_start", nullable = false)
    private Date workingTimeStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "working_time_end", nullable = false)
    private Date workingTimeEnd;

    @Column(name = "holiday", nullable = false)
    private boolean holiday;

    public WorkingTime() {

    }

    public WorkingTime(Employee employee, Date workingTimeStart, Date workingTimeEnd) {
        this.employee = employee;
        this.workingTimeStart = workingTimeStart;
        this.workingTimeEnd = workingTimeEnd;
        holiday = false;
    }

    public WorkingTime(Employee employee, Date workingTimeStart, Date workingTimeEnd, boolean holiday) {
        this.employee = employee;
        this.workingTimeStart = workingTimeStart;
        this.workingTimeEnd = workingTimeEnd;
        this.holiday = holiday;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getWorkingTimeStart() {
        return workingTimeStart;
    }

    public void setWorkingTimeStart(Date start) {
        this.workingTimeStart = start;
    }

    public Date getWorkingTimeEnd() {
        return workingTimeEnd;
    }

    public void setWorkingTimeEnd(Date end) {
        this.workingTimeEnd = end;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkingTime that = (WorkingTime) o;

        if (id != that.id) return false;
        if (!employee.equals(that.employee)) return false;
        if (!workingTimeStart.equals(that.workingTimeStart)) return false;
        if (holiday != that.holiday) return false;
        return workingTimeEnd.equals(that.workingTimeEnd);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + employee.hashCode();
        result = 31 * result + workingTimeStart.hashCode();
        result = 31 * result + workingTimeEnd.hashCode();
        result = 31 * result + (holiday ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WorkingTime{" +
                "id=" + id +
                ", employeeId=" + (employee == null ? null : employee.getEmployeeId()) +
                ", start=" + workingTimeStart +
                ", end=" + workingTimeEnd +
                (holiday ? ", it's holiday" : ", it's workday") +
                '}';
    }
}
