package se.miun.android_app.Api;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;
import se.miun.android_app.model.Employee;

public interface ApiInterface {

    // For login to the system
    @GET("secured/login")
    Call<String> login(@Header("Authorization") String authorizationHeader);

    // Gets all the employees
    @GET("employees")
    Call<List<Employee>> getEmployees();

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
}
