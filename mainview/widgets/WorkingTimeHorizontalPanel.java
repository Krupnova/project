package com.project.client.mainview.widgets;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class WorkingTimeHorizontalPanel extends HorizontalPanel {

    private IntRangeListBox startHourListBox;
    private IntRangeListBox startMinutesListBox;
    private IntRangeListBox endHourListBox;
    private IntRangeListBox endMinutesListBox;

    public WorkingTimeHorizontalPanel() {
        startHourListBox = new IntRangeListBox(7, 22, 1);
        add(startHourListBox);
        Label colon1Label = new Label(":");
        colon1Label.setStyleName("label");
        add(colon1Label);
        startMinutesListBox = new IntRangeListBox(0, 50, 10);
        add(startMinutesListBox);

        Label dashLabel = new Label("-");
        dashLabel.setStyleName("label");
        add(dashLabel);

        endHourListBox = new IntRangeListBox(7, 22, 1);
        add(endHourListBox);
        Label colon2Label = new Label(":");
        colon2Label.setStyleName("label");
        add(colon2Label);
        endMinutesListBox = new IntRangeListBox(0, 50, 10);
        add(endMinutesListBox);
    }

    public IntRangeListBox getStartHourListBox() {
        return startHourListBox;
    }

    public IntRangeListBox getStartMinutesListBox() {
        return startMinutesListBox;
    }

    public IntRangeListBox getEndHourListBox() {
        return endHourListBox;
    }

    public IntRangeListBox getEndMinutesListBox() {
        return endMinutesListBox;
    }
}
