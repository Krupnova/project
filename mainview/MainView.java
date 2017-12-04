package com.ncedu.nc_project.client.mainview;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.CellPreviewEvent;
import com.ncedu.nc_project.client.MainRestService;
import com.ncedu.nc_project.client.loginview.LoginView;
import com.ncedu.nc_project.client.mainview.widgets.EmployeeWeekInfoCellTable;
import com.ncedu.nc_project.client.mainview.widgets.IntRangeListBox;
import com.ncedu.nc_project.shared.entities.Employee;
import com.ncedu.nc_project.shared.entities.EmployeeWeekInfo;
import com.ncedu.nc_project.shared.entities.Roles;
import com.ncedu.nc_project.shared.entities.UserLoginInfo;
import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Oleg Vinogradov on 16.05.2017.
 */
public class MainView extends Composite {

    static {
        Defaults.setDateFormat(null);
    }

    UserLoginInfo userLoginInfo;
    AddEmployeeDialogBox addEmployeeDialogBox;
    RemoveEmployeeDialogBox removeEmployeeDialogBox;
    EditEmployeeDataDialogBox editEmployeeDataDialogBox;
    AddAdminDialogBox addAdminDialogBox;
    ChangeEmployeesTeamDialogBox changeEmployeesTeamDialogBox;

    Integer row;// to hold row index
    Integer column;// to hold column index
    EmployeeWeekInfo empWeekInfo;

    private Grid loginInfoPanel = new Grid(1, 2);
    private Button logoutButton = new Button("logout");
    private Label userLoginInfoLabel = new Label();
    private PushButton addEmployeeButton = new PushButton("Add Employee");
    private PushButton addWorkingTimeButton = new PushButton("Add Working Time");
    private PushButton updateWorkingTimeButton = new PushButton("Update Working Time");
    private PushButton repeatWorkingTimeButton = new PushButton("Reject Working Time");
    private PushButton removeEmployeeButton = new PushButton("Remove Employee");
    private PushButton workdayHolidayButton = new PushButton("Workdays/Holidays");
    private PushButton editEmployeeDataButton = new PushButton("Edit Employee Data");
    private PushButton addAdminButton = new PushButton("Add Admin");
    private PushButton changeEmployeesTeamButton = new PushButton("Change Employee's Team");

    //добавляю кнопку опций, где будет возможность выбрать изменение своего пароля
    private PushButton optionsButton = new PushButton("Options");
    private List<EmployeeWeekInfo> employeeWeekInfoListMainView;
    private Employee employeeNew;
    private long employeeRemoveId;
    private EmployeeWeekInfoCellTable employeeWeekInfoCellTable = new EmployeeWeekInfoCellTable();
    private Button previousWeekButton = new Button("<");
    private IntRangeListBox weekNumberListBox = new IntRangeListBox();
    private Button nextWeekButton = new Button(">");
    private MainRestService restService = (MainRestService) GWT.create(MainRestService.class);
    private String roleEmployee = null;

    public MainView(UserLoginInfo userLoginInfo) {

        this.userLoginInfo = userLoginInfo;

        init();

        ShowDialogBoxClickHandler showDialogBoxClickHandler = new ShowDialogBoxClickHandler();
        addEmployeeButton.addClickHandler(showDialogBoxClickHandler);
        editEmployeeDataButton.addClickHandler(showDialogBoxClickHandler);
        addAdminButton.addClickHandler(showDialogBoxClickHandler);
        changeEmployeesTeamButton.addClickHandler(showDialogBoxClickHandler);
        addWorkingTimeButton.addClickHandler(showDialogBoxClickHandler);
        updateWorkingTimeButton.addClickHandler(showDialogBoxClickHandler);
        removeEmployeeButton.addClickHandler(showDialogBoxClickHandler);
        repeatWorkingTimeButton.addClickHandler(showDialogBoxClickHandler);
        workdayHolidayButton.addClickHandler(showDialogBoxClickHandler);
        optionsButton.addClickHandler(showDialogBoxClickHandler);

        addEmployeeButton.setStyleName("buttonToolbar");
        addAdminButton.setStyleName("buttonToolbar");
        editEmployeeDataButton.setStyleName("buttonToolbar");
        changeEmployeesTeamButton.setStyleName("buttonToolbar");
        addWorkingTimeButton.setStyleName("buttonToolbar");
        updateWorkingTimeButton.setStyleName("buttonToolbar");
        repeatWorkingTimeButton.setStyleName("buttonToolbar");
        removeEmployeeButton.setStyleName("buttonToolbar");
        workdayHolidayButton.setStyleName("buttonToolbar");
        optionsButton.setStyleName("buttonToolbar");

        switch (userLoginInfo.getRole()) {
            case Admin:
                RootPanel.get("add_employee_button").add(addEmployeeButton);
                RootPanel.get("add_admin_button").add(addAdminButton);
                RootPanel.get("change_employees_team_button").add(changeEmployeesTeamButton);
                RootPanel.get("edit_employee_data_button").add(editEmployeeDataButton);
                RootPanel.get("add_working_time_button").add(addWorkingTimeButton);
                RootPanel.get("update_working_time_button").add(updateWorkingTimeButton);
                RootPanel.get("remove_employee_button").add(removeEmployeeButton);
                RootPanel.get("workday_or_holiday_button").add(workdayHolidayButton);
                RootPanel.get("options_button").add(optionsButton);
                break;
            case Teamlead:
                RootPanel.get("add_working_time_button").add(addWorkingTimeButton);
                RootPanel.get("update_working_time_button").add(updateWorkingTimeButton);
                RootPanel.get("repeat_working_time_button").add(repeatWorkingTimeButton);
                RootPanel.get("options_button").add(optionsButton);
                break;
            case Employee:
                RootPanel.get("add_working_time_button").add(addWorkingTimeButton);
                RootPanel.get("update_working_time_button").add(updateWorkingTimeButton);
                RootPanel.get("options_button").add(optionsButton);
                break;
        }

        employeeWeekInfoCellTable.setStyleName("cellTable");
        employeeWeekInfoCellTable.setUserLoginInfo(userLoginInfo);
        RootPanel.get("main_table").add(employeeWeekInfoCellTable);

        row = 0;
        column = 0;

        employeeWeekInfoCellTable.addCellPreviewHandler(new CellPreviewEvent.Handler<EmployeeWeekInfo>() {
            @Override
            public void onCellPreview(CellPreviewEvent<EmployeeWeekInfo> cellPreviewEvent) {

                if (BrowserEvents.CLICK.equalsIgnoreCase(cellPreviewEvent.getNativeEvent().getType())) {
                    row = cellPreviewEvent.getIndex();
                    column=cellPreviewEvent.getColumn();
                }
            }
        });

        //because Doubleclick handler doesn't give row index or column index we will use addCellPreviewHandler to return row index or column index.
        employeeWeekInfoCellTable.addDomHandler(new DoubleClickHandler() {

            @Override
            public void onDoubleClick(final DoubleClickEvent event) {
                DialogBox dialogBox;
                Integer x = 0;
                Integer y = 0;
                    if (userLoginInfo.getRole().equals(Roles.Admin)) {
                        if (column >= 2 && column <= 6) {
                            dialogBox = new AddUpdateRemoveWorkingTimeInCellDialogBox(userLoginInfo, row, column, employeeWeekInfoCellTable);
                            x = event.getX() + 20;
                            y = event.getY() + 230;

                            dialogBox.addCloseHandler(closeEvent -> {
                                redrawMainTable(Integer.parseInt(weekNumberListBox.getSelectedValue()));
                            });
                            dialogBox.setPopupPosition(x, y);
                            dialogBox.show();
                            row = -1;
                            column = -1;
                        }
                    } else {
                        if ((userLoginInfo.getEmployeeId() == employeeWeekInfoCellTable.getVisibleItem(row).getEmployee().getEmployeeId())
                                && (column >= 2 && column <= 6)) {
                            dialogBox = new AddUpdateRemoveWorkingTimeInCellDialogBox(userLoginInfo, row, column, employeeWeekInfoCellTable);
                            x = event.getX() + 20;
                            y = event.getY() + 230;

                            dialogBox.addCloseHandler(closeEvent -> {
                                redrawMainTable(Integer.parseInt(weekNumberListBox.getSelectedValue()));
                            });
                            dialogBox.setPopupPosition(x, y);
                            dialogBox.show();
                            row = -1;
                            column = -1;
                        }
                    }
            }
        }, DoubleClickEvent.getType());

        previousWeekButton.addClickHandler(clickEvent -> {
            int previousIndex = weekNumberListBox.getSelectedIndex() - 1;
            if (previousIndex >= 0) {
                weekNumberListBox.setSelectedIndex(previousIndex);
            }
        });

        weekNumberListBox.addChangeHandler(changeEvent -> {
            int selectedIndex = weekNumberListBox.getSelectedIndex();
            previousWeekButton.setEnabled(selectedIndex != 0);
            nextWeekButton.setEnabled(selectedIndex != weekNumberListBox.getItemCount() - 1);
            redrawMainTable(Integer.parseInt(weekNumberListBox.getSelectedValue()));
        });

        nextWeekButton.addClickHandler(clickEvent -> {
            int nextIndex = weekNumberListBox.getSelectedIndex() + 1;
            if (nextIndex < weekNumberListBox.getItemCount()) {
                weekNumberListBox.setSelectedIndex(nextIndex);
            }
        });

        Grid mainTableNavBarGrid = new Grid(1, 3);
        mainTableNavBarGrid.setWidget(0, 0, previousWeekButton);
        mainTableNavBarGrid.setWidget(0, 1, weekNumberListBox);
        mainTableNavBarGrid.setWidget(0, 2, nextWeekButton);
        RootPanel.get("main_table_nav_bar").add(mainTableNavBarGrid);

        userLoginInfoLabel.setText("Hello " + userLoginInfo.getEmployeeFirstName() + " " + userLoginInfo.getEmployeeLastName()
                + ". " + "You are " + userLoginInfo.getRole().toString() + ". Session ID: " + Cookies.getCookie("sessionID"));

        logoutButton.addClickHandler(clickEvent -> {
            Cookies.removeCookie("sessionID");
            Cookies.removeCookie("login");
            Cookies.removeCookie("password");

            RootPanel.get("add_employee_button").remove(addEmployeeButton);
            RootPanel.get("add_admin_button").remove(addAdminButton);
            RootPanel.get("edit_employee_data_button").remove(editEmployeeDataButton);
            RootPanel.get("change_employees_team_button").remove(changeEmployeesTeamButton);
            RootPanel.get("add_working_time_button").remove(addWorkingTimeButton);
            RootPanel.get("update_working_time_button").remove(updateWorkingTimeButton);
            RootPanel.get("remove_employee_button").remove(removeEmployeeButton);
            RootPanel.get("repeat_working_time_button").remove(repeatWorkingTimeButton);
            RootPanel.get("workday_or_holiday_button").remove(workdayHolidayButton);
            RootPanel.get("main_table_nav_bar").remove(mainTableNavBarGrid);
            RootPanel.get("main_table").remove(employeeWeekInfoCellTable);
            RootPanel.get("main_table_nav_bar").remove(mainTableNavBarGrid);
            RootPanel.get("login_info_panel").remove(loginInfoPanel);
            RootPanel.get("options_button").remove(optionsButton);

            RootPanel.get("login_form").add(new LoginView());
        });

        loginInfoPanel.setWidget(0, 0, userLoginInfoLabel);
        loginInfoPanel.setWidget(0, 1, logoutButton);

        RootPanel.get("login_info_panel").add(loginInfoPanel);
    }

    private void init() {

        restService.getCurrentWeekNumberAndWeeksInYear(new MethodCallback<List<Integer>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {

            }

            @Override
            public void onSuccess(Method method, List<Integer> list) {
                int currentWeekNumber = list.get(0);
                int weeksInYear = list.get(1);

                weekNumberListBox.setRange(1, weeksInYear, 1);
                weekNumberListBox.setSelectedIndex(currentWeekNumber - 1);

//                redrawMainTable(currentWeekNumber);
            }
        });
    }

    private void redrawMainTable(int weekNumber) {

        restService.getCalendar(weekNumber, new MethodCallback<List<Date>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {

            }

            @Override
            public void onSuccess(Method method, List<Date> dates) {
                employeeWeekInfoCellTable.init(dates);
            }
        });

        restService.getEmployeeWeekInfoRole(weekNumber, userLoginInfo, new MethodCallback<List<EmployeeWeekInfo>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
            }

            @Override
            public void onSuccess(Method method, List<EmployeeWeekInfo> employeeWeekInfoList) {
                employeeWeekInfoListMainView = employeeWeekInfoList;
                employeeWeekInfoCellTable.fulfill(employeeWeekInfoList);
            }
        });
    }

    private class ShowDialogBoxClickHandler implements ClickHandler {

        @Override
        public void onClick(ClickEvent clickEvent) {
            Object clickEventSource = clickEvent.getSource();
            DialogBox dialogBox = new DialogBox();
            byte flagCloseDialogBox = 0;
            int x = 0;
            int y = 0;

            if (Objects.equals(addEmployeeButton, clickEventSource)) {
                addEmployeeDialogBox = new AddEmployeeDialogBox();
                x = addEmployeeButton.getElement().getAbsoluteRight();
                y = addEmployeeButton.getElement().getAbsoluteTop();
                dialogBox = addEmployeeDialogBox;
                flagCloseDialogBox = 1;
            } else if (Objects.equals(addAdminButton, clickEventSource)) {
                addAdminDialogBox = new AddAdminDialogBox();
                x = addAdminButton.getElement().getAbsoluteRight();
                y = addAdminButton.getElement().getAbsoluteTop();
                dialogBox = addAdminDialogBox;
                flagCloseDialogBox = 2;
            } else if (Objects.equals(changeEmployeesTeamButton, clickEventSource)) {
                changeEmployeesTeamDialogBox = new ChangeEmployeesTeamDialogBox();
                x = changeEmployeesTeamButton.getElement().getAbsoluteRight();
                y = changeEmployeesTeamButton.getElement().getAbsoluteTop();
                dialogBox = changeEmployeesTeamDialogBox;
                flagCloseDialogBox = 3;
            } else if (Objects.equals(editEmployeeDataButton, clickEventSource)) {
                editEmployeeDataDialogBox = new EditEmployeeDataDialogBox(userLoginInfo);
                x = editEmployeeDataButton.getElement().getAbsoluteRight();
                y = editEmployeeDataButton.getElement().getAbsoluteTop();
                dialogBox = editEmployeeDataDialogBox;
                flagCloseDialogBox = 4;
            } else if (Objects.equals(removeEmployeeButton, clickEventSource)) {
                removeEmployeeDialogBox = new RemoveEmployeeDialogBox();
                x = removeEmployeeButton.getElement().getAbsoluteRight();
                y = removeEmployeeButton.getElement().getAbsoluteTop();
                dialogBox = removeEmployeeDialogBox;
                removeEmployeeDialogBox.setUserLoginInfo(userLoginInfo);
                flagCloseDialogBox = 5;
            } else if (Objects.equals(addWorkingTimeButton, clickEventSource)) {
                dialogBox = new AddWorkingTimeDialogBox(userLoginInfo);
                x = addWorkingTimeButton.getElement().getAbsoluteRight();
                y = addWorkingTimeButton.getElement().getAbsoluteTop();
                flagCloseDialogBox = 6;
            } else if (Objects.equals(updateWorkingTimeButton, clickEventSource)) {
                dialogBox = new UpdateWorkingTimeDialogBox(userLoginInfo);
                x = updateWorkingTimeButton.getElement().getAbsoluteRight();
                y = updateWorkingTimeButton.getElement().getAbsoluteTop();
                flagCloseDialogBox = 7;
            } else if (Objects.equals(repeatWorkingTimeButton, clickEventSource)) {
                dialogBox = new RepeatWorkingTimeDialogBox(userLoginInfo);
                x = repeatWorkingTimeButton.getElement().getAbsoluteRight();
                y = repeatWorkingTimeButton.getElement().getAbsoluteTop();
                flagCloseDialogBox = 8;
            } else if (Objects.equals(workdayHolidayButton, clickEventSource)) {
                dialogBox = new WorkdayHolidayDialogBox();
                x = workdayHolidayButton.getElement().getAbsoluteRight();
                y = workdayHolidayButton.getElement().getAbsoluteTop();
                flagCloseDialogBox = 9;
            } else if (Objects.equals(optionsButton, clickEventSource)) {
                //добавляю реакцию на клик. создание окна
                dialogBox = new OptionsDialogBox(userLoginInfo);
                x = optionsButton.getElement().getAbsoluteRight();
                y = optionsButton.getElement().getAbsoluteTop();
                flagCloseDialogBox = 10;
            }
            byte finalFlagCloseDialogBox = flagCloseDialogBox;
            dialogBox.addCloseHandler(closeEvent -> {
                redrawMainTable(Integer.parseInt(weekNumberListBox.getSelectedValue()));
//                switch (finalFlagCloseDialogBox) {
//                    case 1:
//                        addEmployeeAction();
//                        break;
//                    case 2:
//                        removeEmployeeAction();
//                        break;
//                    case 3:
//                        redrawMainTable(Integer.parseInt(weekNumberListBox.getSelectedValue()));
//                        break;
//                    case 4:
//                        redrawMainTable(Integer.parseInt(weekNumberListBox.getSelectedValue()));
//                        break;
//                    case 5:
//                        redrawMainTable(Integer.parseInt(weekNumberListBox.getSelectedValue()));
//                        break;
//                    case 6:
//                        redrawMainTable(Integer.parseInt(weekNumberListBox.getSelectedValue()));
//                        break;
//                }
            });
            dialogBox.setPopupPosition(x + 10, y - 20);
            dialogBox.show();
        }
    }

//    private void removeEmployeeAction(){
//        for(int i = 0; i < employeeWeekInfoListMainView.size(); i++){
//            if(employeeWeekInfoListMainView.get(i).getEmployee().getEmployeeId() == employeeRemoveId){
//                employeeWeekInfoListMainView.remove(i);
//                break;
//            }
//        }
//        employeeWeekInfoCellTable.fulfill(employeeWeekInfoListMainView);
//    }
//
//    private void addEmployeeAction(){;
//        employeeWeekInfoListMainView.add(new EmployeeWeekInfo(addEmployeeDialogBox.getEmployeeExport()));
//        employeeWeekInfoCellTable.fulfill(employeeWeekInfoListMainView);
//    }
}
