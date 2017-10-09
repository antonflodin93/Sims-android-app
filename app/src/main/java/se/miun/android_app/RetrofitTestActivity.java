package se.miun.android_app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.miun.android_app.model.Employee;


public class RetrofitTestActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    private List<Employee> employees;
    private ApiInterface apiInterface;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofittest);
        mContext = this;

        // Initialize variables
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        // Create instance of apiinterface
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // Make call to get all employees
        Call<List<Employee>> call = apiInterface.getEmployees();

        call.enqueue(new Callback<List<Employee>>() {
            @Override

            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                // Return the list of employees
                employees = response.body();


                // Initialize the adapter
                adapter = new RecyclerAdapter(employees);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}