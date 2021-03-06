package com.project.client.mainview.widgets;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.ToggleButton;

import java.util.Date;


public class DateToggleButton extends ToggleButton {

    private Date date;

    public DateToggleButton() {
        setDate(null);
    }

    public DateToggleButton(Date date) {
        setDate(date);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        setText(date != null
                ? DateTimeFormat.getFormat("dd MMM, EEE").format(date)
                : "null");
    }
}
