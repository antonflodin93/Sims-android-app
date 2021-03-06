package se.miun.android_app.Api;


import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import se.miun.android_app.Model.Building;
import se.miun.android_app.Model.Company;
import se.miun.android_app.Model.Employee;
import se.miun.android_app.Model.Floor;
import se.miun.android_app.Model.Message;

public interface ApiInterface {

    /*
    *  LOGIN
    */

    // For login to the system as master
    @GET("secured/login/master")
    Call<ResponseBody> loginAsMaster(@Header("Authorization") String authorizationHeader);

    // For login to the system as employee
    @GET("secured/login/employee")
    Call<ResponseBody> loginAsEmployee(@Header("Authorization") String authorizationHeader);


    /*
    *  EMPLOYEES
    */

    // Gets all the employees
    @GET("employees")
    Call<ArrayList<Employee>> getEmployees();

    // Gets all employees in a company
    @GET("employees/company/{companyName}")
    Call<ArrayList<Employee>> getEmployeesInCompany(@Path("companyName") String companyName);

    // Gets employee by id
    @GET("employees/userid/{id}")
    Call<Employee> getEmployeeById(@Path("id") int id);

    // Gets employee by username
    @GET("employees/username/{id}")
    Call<Employee> getEmployeeByUsername(@Path("username") String username);

    // Insert employee into the database
    @POST("employees")
    Call<ResponseBody> insertEmployee(@Body Employee employee);

    // Delete employee
    @DELETE("employees/{id}")
    Call<Employee> deleteEmployeeById(@Path("id") int id);

    // Get employees in building
    @GET("employees/building/{buildingId}")
    Call<ArrayList<Employee>> getEmployeesInBuilding(@Path("buildingId") int buildingId);

    // Get employees in floor
    @GET("employees/floor/{floorId}")
    Call<ArrayList<Employee>> getEmployeesInFloor(@Path("floorId") int floorId);


    // Delete employee in building
    @DELETE("employees/building/{employeeID}/")
    Call<ResponseBody> exitBuildingEmployee(@Path("employeeID") int employeeID);

    // Change floor for a employee
    @PUT("employees/floor/{floorId}/employee/{employeeID}")
    Call<ResponseBody> changeFloorEmployee(@Path("floorId") int floorId, @Path("employeeID") int employeeID);

    // Employee enters building
    @POST("employees/building/{buildingId}/employee/{employeeID}")
    Call<ResponseBody> enterBuildingEmployee(@Path("buildingId") int buildingId, @Path("employeeID") int employeeID);

    // Employee enters floor first time
    @POST("employees/floor/{floorId}/employee/{employeeID}")
    Call<ResponseBody> enterFloorEmployee(@Path("floorId") int floorId, @Path("employeeID") int employeeID);

    // Gets all employees that have acknowledged message
    @GET("employees/message/{messageId}")
    Call<ArrayList<Employee>> getEmployeesAcknowledged(@Path("messageId") int messageId);


    /*
    *  MESSAGES
    */

    // Gets all the regular messages
    @GET("messages/regular")
    Call<ArrayList<Message>> getRegularMessages();

    // Gets all the warning messages
    @GET("messages/warning")
    Call<ArrayList<Message>> getWarningMessages();

    // Post regular message for broadcast
    @POST("messages/regular")
    Call<ResponseBody> insertBroadcastMessage(@Body Message message);

    // Post regular message for an employee
    @POST("messages/regular/employee/{employeeId}")
    Call<ResponseBody> insertEmployeeMessage(@Body Message message, @Path("employeeId") int employeeId);

    // Get regular message for an employee
    @GET("messages/regular/employee/{employeeId}")
    Call<ArrayList<Message>> getEmployeeMessage(@Path("employeeId") int employeeId);

    // Post regular message for a company
    @POST("messages/regular/company/{companyName}")
    Call<ResponseBody> insertCompanyMessage(@Body Message message, @Path("companyName") String companyName);

    // Get regular message for a company
    @GET("messages/regular/company/{companyName}")
    Call<ArrayList<Message>> getCompanyMessage(@Path("companyName") String companyName);


    // Post regular message for a factory object
    @POST("messages/regular/factoryobject/{factoryobjectId}")
    Call<ResponseBody> insertRegularMessageFactoryObject(@Body Message message, @Path("factoryobjectId") int factoryobjectId);


    // Post warning message for a factory object
    @POST("messages/warning/factoryobject/{factoryobjectId}")
    Call<ResponseBody> insertWarningMessageFactoryObject(@Body Message message, @Path("factoryobjectId") int factoryobjectId);

    // Post warning message for a building
    @POST("messages/warning/building/{buildingId}")
    Call<ResponseBody> addBuildingWarningMessage(@Path("buildingId") int buildingId, @Body Message message);

    // Get all warning messages for all buildings
    @GET("messages/warning/building")
    Call<ArrayList<Message>> getAllBuildingWarningMessages();

    // Get all warning messages for a building that are not yet acknowledged by user
    @GET("messages/warning/building/{buildingId}/employee/{employeeID}")
    Call<ArrayList<Message>> getBuildingWarningMessageNotAcked(@Path("buildingId") int buildingId, @Path("employeeID") int employeeID);

    // Acknowledge warning message /warning/{messageId}/employee/{employeeID}
    @POST("messages/warning/{messageId}/employee/{employeeID}")
    Call<ResponseBody> acknowledgeMessage(@Path("messageId") int messageId, @Path("employeeID") int employeeID);

    // Delete building message
    @DELETE("messages/warning/building/{messageId}")
    Call<ResponseBody> deleteBuildingMessage(@Path("messageId") String messageId);


    /*
    *  COMPANY
    */

    // Gets all the companies
    @GET("companies")
    Call<ArrayList<Company>> getAllCompanies();



    /*
    *   BUILDING
    */

    // Gets all the buildings
    @GET("buildings")
    Call<ArrayList<Building>> getAllBuildings();




    /*
    *   FLOOR
    */

    // Get a floor by id
    @GET("floors/{floorId}")
    Call<Floor> getFloorById(@Path("floorId") int floorId);

    // Get a floor that employee is inside
    @GET("floors/{employeeID}")
    Call<Floor> getFloorForEmployee(@Path("employeeID") int employeeID);



}
