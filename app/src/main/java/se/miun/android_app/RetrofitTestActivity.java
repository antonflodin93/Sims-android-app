package se.miun.android_app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RetrofitTestActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    private List<Employee> employees;
    private ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofittest);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Employee>> call = apiInterface.getEmployees();
        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                employees = response.body();
                adapter = new RecyclerAdapter(employees);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {

            }
        });
    }
}