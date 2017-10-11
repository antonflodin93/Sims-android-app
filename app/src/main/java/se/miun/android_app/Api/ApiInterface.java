package se.miun.android_app.Api;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;
import se.miun.android_app.model.Employee;

public interface ApiInterface {

    // Gets all the employees
    @GET("employees")
    Call<List<Employee>> getEmployees();

    // Gets employee by id
    @GET("employees/{id}")
    Call<Employee> getEmployeeById(@Path("id") int id);

    // Insert employee into the database
    @POST("employees")
    Call<Employee> insertEmployee(@Body Employee employee);

    // Delete employee
    @DELETE("employees/{id}")
    Call<Employee> deleteEmployeeById(@Path("id") int id);
}
