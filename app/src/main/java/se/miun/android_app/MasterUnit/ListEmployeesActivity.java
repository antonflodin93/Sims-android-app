package se.miun.android_app.MasterUnit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.miun.android_app.Adapter.EmployeeRecycleViewAdapter;
import se.miun.android_app.Adapter.EmployeeSpinnerAdapter;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
import se.miun.android_app.Model.Employee;
import se.miun.android_app.R;

public class ListEmployeesActivity extends AppCompatActivity {
    private static final int HTTP_RESPONSE_ACCEPTED = 200;
    private ArrayList<Employee> employees = new ArrayList<>();
    private RecyclerView employeeRecycleView;
    private RecyclerView.LayoutManager layoutManager;
    private EmployeeRecycleViewAdapter employeeAdapter;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_employees);

        // Init components
        context = this;
        employeeRecycleView = (RecyclerView) findViewById(R.id.employeeRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        employeeRecycleView.setLayoutManager(layoutManager);
        employeeRecycleView.setHasFixedSize(true);
        getEmployees();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuSearchItem = menu.findItem(R.id.menuSearchItem);
        SearchView searchView = (SearchView) menuSearchItem.getActionView();




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                employeeAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                employeeAdapter.getFilter().filter(newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }




    private void getEmployees() {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ArrayList<Employee>> call;
        call = apiInterface.getEmployees();
        call.enqueue(new Callback<ArrayList<Employee>>() {
            @Override
            public void onResponse(Call<ArrayList<Employee>> call, Response<ArrayList<Employee>> response) {
                if (response.code() == HTTP_RESPONSE_ACCEPTED) {

                    employees = response.body();
                    employeeAdapter = new EmployeeRecycleViewAdapter(employees);
                    employeeRecycleView.setAdapter(employeeAdapter);

                    //employeeAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Employee>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
