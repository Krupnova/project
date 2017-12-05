package com.project.client.mainview.widgets.dialogboxes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.project.client.MainRestService;
import com.project.shared.entities.UserLoginInfo;


public class OptionsDialogBox extends DialogBox {
    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);

    public OptionsDialogBox(UserLoginInfo userLoginInfo) {
        VerticalPanel verticalPanel = new VerticalPanel();
        //расстояние
        verticalPanel.setSpacing(10);

        //добавляю кнопку при нажатии на которую появляется dialog box где можно будет поменять пароль
        PushButton changePswrdButton = new PushButton("Change password");

        changePswrdButton.addClickHandler(clickEvent -> {
            DialogBox dialogBox = new ChangePasswordDialogBox(userLoginInfo);
            int x = changePswrdButton.getElement().getAbsoluteRight();
            int y = changePswrdButton.getElement().getAbsoluteTop();
            dialogBox.setPopupPosition(x + 10, y - 20);
            dialogBox.show();
            dialogBox.addCloseHandler(closeEvent -> hide());
        });

        verticalPanel.add(changePswrdButton);

        //добавляю на нижнюю горизонтальную панель кнопку cancel
        HorizontalPanel buttonsHorizontalPanel = new HorizontalPanel();

        PushButton cancelButton = new PushButton("Cancel");
        cancelButton.addClickHandler(clickEvent -> hide());
        buttonsHorizontalPanel.add(cancelButton);

        verticalPanel.add(buttonsHorizontalPanel);

        setWidget(verticalPanel);
    }


}
