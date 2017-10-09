package se.miun.android_app;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("employees")
    Call<List<Employee>> getEmployees();
}
