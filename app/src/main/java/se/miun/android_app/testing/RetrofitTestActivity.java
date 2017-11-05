package se.miun.android_app.testing;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
import se.miun.android_app.R;
import se.miun.android_app.Adapter.RecyclerAdapter;
import se.miun.android_app.Model.Employee;


public class RetrofitTestActivity extends Activity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    private List<Employee> employees;
    private ApiInterface apiInterface;
    private Button insertOneBtn, listOneBtn, listAllBtn, deleteLatestBtn;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofittest);
        mContext = this;

        // Initialize variables
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        insertOneBtn = (Button) findViewById(R.id.insertOneBtn);
        insertOneBtn.setOnClickListener(this);
        listOneBtn = (Button) findViewById(R.id.listOneBtn);
        listOneBtn.setOnClickListener(this);
        listAllBtn = (Button) findViewById(R.id.listAllBtn);
        listAllBtn.setOnClickListener(this);
        deleteLatestBtn = (Button) findViewById(R.id.deleteLatestBtn);
        deleteLatestBtn.setOnClickListener(this);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        // Create instance of apiinterface using retrofit instance
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.insertOneBtn){
            insertOne();
        } else if(view.getId() == R.id.listOneBtn){
            listOne();
        } else if(view.getId() == R.id.listAllBtn){

        } else if(view.getId() == R.id.deleteLatestBtn){
            deleteOne();
        }




    }

    private void deleteOne() {
        // Get latest employee id
        int id = Integer.parseInt(employees.get(employees.size()-1).getEmployeeId());
        Call<Employee> call = apiInterface.deleteEmployeeById(id);

        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {

                // Initialize the employeeslist
                employees = new ArrayList<>();

                // Add the employee from the response to the list
                employees.add(response.body());

                // Initialize the adapter and set it with the employeelist
                adapter = new RecyclerAdapter(employees);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {

            }
        });
    }

    private void listOne() {
        // Get latest employee id
        int id = Integer.parseInt(employees.get(employees.size()-1).getEmployeeId());
        Call<Employee> call = apiInterface.getEmployeeById(id);

        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {

                // Initialize the employeeslist
                employees = new ArrayList<>();

                // Add the employee from the response to the list
                employees.add(response.body());

                // Initialize the adapter and set it with the employeelist+
                adapter = new RecyclerAdapter(employees);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    private void insertOne() {
        /*
        // Post a employee to the database
        Employee employee = new Employee("Anton", "Flodin", "anton", "anfl120@hotmail.com", "hej", "0702733166", "MITTCOMANY");
        Call<Employee> call = apiInterface.insertEmployee(employee);

        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                listAll();
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
        */
    }
}