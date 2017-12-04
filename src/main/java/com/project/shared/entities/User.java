package com.project.shared.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(name = "user_login", nullable = false)
    private String userLogin;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Employee employee;

    public User() {
    }

    public User(String userLogin, String userPassword) {
        this.userLogin = userLogin;
        this.userPassword = userPassword;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }


    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (getUserId() != user.getUserId()) return false;
        if (getUserLogin() != null ? !getUserLogin().equals(user.getUserLogin()) : user.getUserLogin() != null)
            return false;
        if (getUserPassword() != null ? !getUserPassword().equals(user.getUserPassword()) : user.getUserPassword() != null)
            return false;
        if (getEmployee() != null ? !getEmployee().equals(user.getEmployee()) : user.getEmployee() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getUserId() ^ (getUserId() >>> 32));
        result = 31 * result + (getUserLogin() != null ? getUserLogin().hashCode() : 0);
        result = 31 * result + (getUserPassword() != null ? getUserPassword().hashCode() : 0);
        result = 31 * result + (getEmployee() != null ? getEmployee().hashCode() : 0);
        return result;
    }
}
