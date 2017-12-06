package se.miun.android_app.MasterUnit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.miun.android_app.Adapter.EmployeeStatusAdapter;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
import se.miun.android_app.EmployeeUnit.EmployeeUnitActivity;
import se.miun.android_app.Model.Employee;
import se.miun.android_app.Model.Message;
import se.miun.android_app.R;

public class ListEmployeeInfo extends AppCompatActivity {
    private Context context;
    private ListView employeeStatusListView;
    private ArrayList<Employee> employeesInBuilding, employeesAcknowledged,employees = new ArrayList<>();
    private ArrayList<Integer> acknowledgedEmployees = new ArrayList<>(), employesInbuilding = new ArrayList<>();
    private EmployeeStatusAdapter employeeStatusAdapter;
    private Message message;
    private int buildingId;
    private int messageId;
    private int HTTP_RESPONSE_OK = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_employee_info);
        context = this;

        message = (Message) getIntent().getSerializableExtra("message");
        buildingId = message.getBuildingId();
        messageId = Integer.parseInt(message.getMessageId());


        // Init components
        employeeStatusListView = (ListView) findViewById(R.id.employeeStatusListView);

        getEmployeesInBuilding(buildingId);
        getEmployeesAcknowledged(messageId);
    }

    private void setAdapter() {


        employeeStatusAdapter = new EmployeeStatusAdapter(context, employeesInBuilding);
        employeeStatusListView.setAdapter(employeeStatusAdapter);
    }

    private void getEmployeesAcknowledged(int messageId) {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ArrayList<Employee>> call;
        call = apiInterface.getEmployeesAcknowledged(messageId);
        call.enqueue(new Callback<ArrayList<Employee>>() {
            @Override
            public void onResponse(Call<ArrayList<Employee>> call, Response<ArrayList<Employee>> response) {
                if (response.code() == HTTP_RESPONSE_OK) {
                    employeesAcknowledged = response.body();

                    // Add the employees to list
                    for(Employee e : employeesAcknowledged){
                        acknowledgedEmployees.add(Integer.parseInt(e.getEmployeeId()));
                        e.setAcknowledged(true);
                        employees.add(e);
                    }

                    getEmployeesInBuilding(buildingId);

                } else{
                    try {
                        Toast.makeText(context, "Error: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Employee>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEmployeesInBuilding(int buildingId) {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ArrayList<Employee>> call;
        call = apiInterface.getEmployeesInBuilding(buildingId);
        call.enqueue(new Callback<ArrayList<Employee>>() {
            @Override
            public void onResponse(Call<ArrayList<Employee>> call, Response<ArrayList<Employee>> response) {
                if (response.code() == HTTP_RESPONSE_OK) {
                    employeesInBuilding = response.body();

                    for(Employee e : employeesInBuilding){
                        if(acknowledgedEmployees.contains(Integer.parseInt(e.getEmployeeId()))){
                            e.setAcknowledged(true);
                        } else {
                            e.setAcknowledged(false);
                        }
                    }

                    setAdapter();



                } else{
                    try {
                        Toast.makeText(context, "Error: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Employee>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
