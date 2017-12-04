package com.ncedu.nc_project.client.mainview.widgets;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Created by Nikita on 07.04.2017.
 */

public class IntRangeListBox extends ListBox {

    public IntRangeListBox() {
    }

    public IntRangeListBox(int initialValue, int finalValue, int step) {
        setRange(initialValue, finalValue, step);
    }

    public void setRange(int initialValue, int finalValue, int step) {
        for (int i = initialValue; i <= finalValue; i += step) {
            addItem(NumberFormat.getFormat("00").format(i));
        }
    }

    @Override
    public void setSelectedIndex(int index) {
        super.setSelectedIndex(index);
        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), this);
    }
}
