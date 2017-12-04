package com.ncedu.nc_project.client.mainview.widgets.dialogboxes;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.ncedu.nc_project.shared.entities.UserLoginInfo;

/**
 * A class that represents the form of a pop-up window
 * that implements the function of editing the data of employee
 *
 * @author Sekachkin Mikhail
 */
public class EditEmployeeDataDialogBox extends DialogBox {

    public EditEmployeeDataDialogBox(UserLoginInfo userLoginInfo) {

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(10);

        PushButton changeEmployeeNameButton = new PushButton("Change First/Last Name");
        PushButton changeEmployeePasswordButton = new PushButton("Change Employee password");

        changeEmployeeNameButton.addClickHandler(clickEvent -> {
            ChangeEmployeeNameDialogBox changeEmployeeNameDialogBox = new ChangeEmployeeNameDialogBox(userLoginInfo);
            int x = changeEmployeeNameButton.getElement().getAbsoluteRight();
            int y = changeEmployeeNameButton.getElement().getAbsoluteTop();
            changeEmployeeNameDialogBox.setPopupPosition(x + 10, y - 20);
            changeEmployeeNameDialogBox.show();
            changeEmployeeNameDialogBox.addCloseHandler(closeEvent -> hide());
        });

        changeEmployeePasswordButton.addClickHandler(clickEvent -> {
            ChangeEmployeePassword changeEmployeePasswordDialogBox = new ChangeEmployeePassword(userLoginInfo);
            int x = changeEmployeePasswordButton.getElement().getAbsoluteRight();
            int y = changeEmployeePasswordButton.getElement().getAbsoluteTop();
            changeEmployeePasswordDialogBox.setPopupPosition(x + 10, y - 20);
            changeEmployeePasswordDialogBox.show();
            changeEmployeePasswordDialogBox.addCloseHandler(closeEvent -> hide());
        });

        verticalPanel.add(changeEmployeeNameButton);
        verticalPanel.add(changeEmployeePasswordButton);

        PushButton cancelButton = new PushButton("Cancel");
        cancelButton.addClickHandler(clickEvent -> hide());
        verticalPanel.add(cancelButton);

        setWidget(verticalPanel);
    }
}
