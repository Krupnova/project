package com.project.client.mainview;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.*;
import com.project.client.MainRestService;
import com.project.client.loginview.LoginView;
import com.project.client.mainview.widgets.EmployeeWeekInfoCellTable;
import com.project.client.mainview.widgets.IntRangeListBox;
import com.project.client.mainview.widgets.dialogboxes.*;
import com.project.shared.entities.Employee;
import com.project.shared.entities.EmployeeWeekInfo;
import com.project.shared.entities.Roles;
import com.project.shared.entities.UserLoginInfo;
import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainView extends Composite {

    static {
        Defaults.setDateFormat(null);
    }

    UserLoginInfo userLoginInfo;
    AddEmployeeDialogBox addEmployeeDialogBox;
    RemoveEmployeeDialogBox removeEmployeeDialogBox;
    EditEmployeeDataDialogBox editEmployeeDataDialogBox;
    EditEmployeeRoleDialogBox editEmployeeRoleDialogBox;
    AddEmployeeToTeamDialogBox addEmployeeToTeamDialogBox;

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
    private PushButton editEmployeeRoleButton = new PushButton("Edit Employee Role");
    private PushButton addEmployeeToTeamButton = new PushButton("Add Employee To Team");

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
private int currentWeekNumber;
private Employee currempl;

    public MainView(UserLoginInfo userLoginInfo) {

        this.userLoginInfo = userLoginInfo;

        init();

        ShowDialogBoxClickHandler showDialogBoxClickHandler = new ShowDialogBoxClickHandler();
        addEmployeeButton.addClickHandler(showDialogBoxClickHandler);
        editEmployeeDataButton.addClickHandler(showDialogBoxClickHandler);
        editEmployeeRoleButton.addClickHandler(showDialogBoxClickHandler);
        addEmployeeToTeamButton.addClickHandler(showDialogBoxClickHandler);
        addWorkingTimeButton.addClickHandler(showDialogBoxClickHandler);
        updateWorkingTimeButton.addClickHandler(showDialogBoxClickHandler);
        removeEmployeeButton.addClickHandler(showDialogBoxClickHandler);
        repeatWorkingTimeButton.addClickHandler(showDialogBoxClickHandler);
        workdayHolidayButton.addClickHandler(showDialogBoxClickHandler);
        optionsButton.addClickHandler(showDialogBoxClickHandler);

        addEmployeeButton.setStyleName("buttonToolbar");
        editEmployeeRoleButton.setStyleName("buttonToolbar");
        editEmployeeDataButton.setStyleName("buttonToolbar");
        addEmployeeToTeamButton.setStyleName("buttonToolbar");
        addWorkingTimeButton.setStyleName("buttonToolbar");
        updateWorkingTimeButton.setStyleName("buttonToolbar");
        repeatWorkingTimeButton.setStyleName("buttonToolbar");
        removeEmployeeButton.setStyleName("buttonToolbar");
        workdayHolidayButton.setStyleName("buttonToolbar");
        optionsButton.setStyleName("buttonToolbar");

        switch (userLoginInfo.getRole()) {
            case Admin:
                RootPanel.get("add_employee_button").add(addEmployeeButton);
                RootPanel.get("add_employee_to_team_button").add(addEmployeeToTeamButton);
                RootPanel.get("edit_employee_data_button").add(editEmployeeDataButton);
                RootPanel.get("edit_employee_role_button").add(editEmployeeRoleButton);
                RootPanel.get("add_working_time_button").add(addWorkingTimeButton);
                RootPanel.get("update_working_time_button").add(updateWorkingTimeButton);
                RootPanel.get("remove_employee_button").add(removeEmployeeButton);
                RootPanel.get("repeat_working_time_button").add(repeatWorkingTimeButton);
                RootPanel.get("workday_or_holiday_button").add(workdayHolidayButton);
                RootPanel.get("options_button").add(optionsButton);
                break;
            case Teamlead:
                RootPanel.get("add_employee_to_team_button").add(addEmployeeToTeamButton);
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
        RootPanel.get("main_table").add(employeeWeekInfoCellTable);

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

        if (userLoginInfo.getRole().equals(Roles.Admin)) {
            if (userLoginInfo.getTeamlead()) {
                roleEmployee = "You are " + Roles.Teamlead.toString() + ".You have administrator rights";
            } else {
                roleEmployee = "You are " + Roles.Employee.toString() + ".You have administrator rights";
            }
        } else {
            roleEmployee = "You are " + userLoginInfo.getRole().toString();
        }
        userLoginInfoLabel.setText("Hello " + userLoginInfo.getEmployeeFirstName() + " " + userLoginInfo.getEmployeeLastName()
                + ". " + roleEmployee + ". Session ID: " + Cookies.getCookie("sessionID"));

        restService.findEmployeeByFirstNameAndLastName(userLoginInfo.getEmployeeFirstName(),
                userLoginInfo.getEmployeeLastName(), new MethodCallback<Employee>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Method method, Employee employee) {
                        currempl = employee;
                    }
                });

            logoutButton.addClickHandler(clickEvent -> {
            Cookies.removeCookie("sessionID");
            Cookies.removeCookie("login");
            Cookies.removeCookie("password");

            RootPanel.get("add_employee_button").remove(addEmployeeButton);
            RootPanel.get("edit_employee_data_button").remove(editEmployeeDataButton);
            RootPanel.get("edit_employee_role_button").remove(editEmployeeRoleButton);
            RootPanel.get("add_employee_to_team_button").remove(addEmployeeToTeamButton);
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
                currentWeekNumber = list.get(0);
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
            } else if (Objects.equals(addEmployeeToTeamButton, clickEventSource)) {
                addEmployeeToTeamDialogBox = new AddEmployeeToTeamDialogBox(userLoginInfo);
                x = addEmployeeToTeamButton.getElement().getAbsoluteRight();
                y = addEmployeeToTeamButton.getElement().getAbsoluteTop();
                dialogBox = addEmployeeToTeamDialogBox;
                flagCloseDialogBox = 2;
            } else if (Objects.equals(editEmployeeDataButton, clickEventSource)) {
                editEmployeeDataDialogBox = new EditEmployeeDataDialogBox();
                x = editEmployeeDataButton.getElement().getAbsoluteRight();
                y = editEmployeeDataButton.getElement().getAbsoluteTop();
                dialogBox = editEmployeeDataDialogBox;
                flagCloseDialogBox = 3;
            } else if (Objects.equals(editEmployeeRoleButton, clickEventSource)) {
                editEmployeeRoleDialogBox = new EditEmployeeRoleDialogBox(userLoginInfo);
                x = editEmployeeRoleButton.getElement().getAbsoluteRight();
                y = editEmployeeRoleButton.getElement().getAbsoluteTop();
                dialogBox = editEmployeeRoleDialogBox;
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
