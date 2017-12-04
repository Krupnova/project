package com.project.shared.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee implements Serializable {

    @OneToOne
    @PrimaryKeyJoinColumn
    @JsonIgnore
    User user;

    @Id
    @Column(name = "employee_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long employeeId;

    @Column(name = "employee_first_name", nullable = false)
    private String employeeFirstName;

    @Column(name = "employee_last_name", nullable = false)
    private String employeeLastName;

    @Column(name = "employee_email", nullable = false)
    private String employeeEmail;

    @Column(name = "isAdmin")
    private String admin;

    @ManyToOne
    @JoinColumn(name = "team_lead")
    @JsonIgnoreProperties("employeeList")
    private Employee teamLead;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "teamLead")
    @JsonIgnoreProperties("teamLead")
    private List<Employee> employeeList = new ArrayList<>();

    public Employee() {
    }

    public Employee(String employeeFirstName, String employeeLastName) {
        this.employeeFirstName = employeeFirstName;
        this.employeeLastName = employeeLastName;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Employee getTeamLead() {
        return teamLead;
    }

    public void setTeamLead(Employee teamLead) {
        this.teamLead = teamLead;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;

        Employee employee = (Employee) o;

        if (employeeId != employee.employeeId) return false;
        if (employeeFirstName != null ? !employeeFirstName.equals(employee.employeeFirstName) : employee.employeeFirstName != null)
            return false;
        if (employeeLastName != null ? !employeeLastName.equals(employee.employeeLastName) : employee.employeeLastName != null)
            return false;
        if (user != null ? !user.equals(employee.user) : employee.user != null) return false;
        if (teamLead != null ? !teamLead.equals(employee.teamLead) : employee.teamLead != null) return false;
        return employeeList != null ? employeeList.equals(employee.employeeList) : employee.employeeList == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (employeeId ^ (employeeId >>> 32));
        result = 31 * result + (employeeFirstName != null ? employeeFirstName.hashCode() : 0);
        result = 31 * result + (employeeLastName != null ? employeeLastName.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (teamLead != null ? teamLead.hashCode() : 0);
        result = 31 * result + (employeeList != null ? employeeList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", employeeFirstName='" + employeeFirstName + '\'' +
                ", employeeLastName='" + employeeLastName + '\'' +
                '}';
    }
}
