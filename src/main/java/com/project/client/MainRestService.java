package com.project.client;

import com.project.shared.entities.*;
import com.project.shared.entities.*;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.*;
import java.util.Date;
import java.util.List;

@Path("/service")
public interface MainRestService extends RestService {

    @POST
    @Path("/save_employee")
    @Produces("application/json")
    void saveEmployee(Employee employee, MethodCallback<Status> callback);

    @POST
    @Path("/update_employee")
    @Produces("application/json")
    void updateEmployee(Employee employee, MethodCallback<Status> callback);

    @DELETE
    @Path("/delete_employee_{id}")
    @Produces("application/json")
    void deleteEmployee(@PathParam("id") long id, MethodCallback<Employee> callback);

    @GET
    @Path("/get_all_employees")
    @Produces("application/json")
    void getAllEmployees(MethodCallback<List<Employee>> callback);

    @POST
    @Path("/save_working_times")
    @Produces("application/json")
    void saveWorkingTimes(List<WorkingTime> workingTimes, MethodCallback<Status> callback);

    @POST
    @Path("/remove_working_times")
    @Produces("application/json")
    void removeWorkingTimes(EmployeeWithDays employeeWithDays, MethodCallback<Void> callback);

    @POST
    @Path("/update_working_time")
    @Produces("application/json")
    void updateWorkingTime(EmployeeWithDays employeeWithDays, MethodCallback<Void> callback);

    @GET
    @Path("/calendar_{week_number}")
    @Produces("application/json")
    void getCalendar(@PathParam("week_number") int weekNumber, MethodCallback<List<Date>> callback);

    @POST
    @Path("/employee_week_info_role_{week_number}")
    @Produces("application/json")
    void getEmployeeWeekInfoRole(@PathParam("week_number") int weekNumber, UserLoginInfo userLoginInfo, MethodCallback<List<EmployeeWeekInfo>> callback);

    @GET
    @Path("/employee_week_info_{week_number}")
    @Produces("application/json")
    void getEmployeeWeekInfo(@PathParam("week_number") int weekNumber, MethodCallback<List<EmployeeWeekInfo>> callback);

    @GET
    @Path("/week_info")
    @Produces("application/json")
    void getCurrentWeekNumberAndWeeksInYear(MethodCallback<List<Integer>> callback);

    @GET
    @Path("/find_employee_{first_name}_{last_name}")
    @Produces("application/json")
    void findEmployeeByFirstNameAndLastName(@PathParam("first_name") String firstName, @PathParam("last_name") String lastName, MethodCallback<Employee> callback);

    @POST
    @Path("/save_user")
    @Produces("application/json")
    void saveUser(User user, MethodCallback<Status> callback);

    @DELETE
    @Path("/delete_user_{id}")
    @Produces("application/json")
    void deleteUser(@PathParam("id") long id, MethodCallback<User> callback);

    @GET
    @Path("/get_all_users")
    @Produces("application/json")
    void getAllUsers(MethodCallback<List<User>> callback);

    @GET
    @Path("/find_user_{login}_{password}")
    @Produces("application/json")
    void findUserByLoginAndPassword(@PathParam("login") String login, @PathParam("password") String password, MethodCallback<User> callback);

    @GET
    @Path("/login_{login}_{password}")
    void loginUser(@PathParam("login") String login, @PathParam("password") String password, MethodCallback<UserLoginInfo> callback);

    @POST
    @Path("/save_as_workday_or_holiday_{is_holiday}")
    @Produces("application/json")
    void saveWorkdaysHolidays(List<Date> selectedDays, @PathParam("is_holiday") boolean isHoliday, MethodCallback<Status> callback);

    @GET
    @Path("/working_week_{week_number}")
    @Produces("application/json")
    void getWorkingWeek(@PathParam("week_number") int weekNumber, MethodCallback<List<WorkingDayInfo>> callback);

    @GET
    @Path("/find_user_{login}")
    @Produces("application/json")
    void findUserByLogin(@PathParam("login") String login, MethodCallback<User> callback);

    @POST
    @Path("/update_user")
    @Produces("application/json")
    void updateUser(User user, MethodCallback<Status> callback);
}