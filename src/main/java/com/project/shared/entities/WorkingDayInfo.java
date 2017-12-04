package com.project.shared.entities;

import java.io.Serializable;
import java.util.Date;

public class WorkingDayInfo implements Serializable {

    private Date date;
    private Boolean holiday;

    public WorkingDayInfo() {
    }

    public WorkingDayInfo(Date date, Boolean holiday) {
        this.date = date;
        this.holiday = holiday;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getHoliday() {
        return holiday;
    }

    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
    }
}
