package se.miun.android_app;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.GET;
import se.miun.android_app.model.Employee;

public interface ApiInterface {

    // Gets all the employees
    @GET("employees")
    Call<List<Employee>> getEmployees();
}
