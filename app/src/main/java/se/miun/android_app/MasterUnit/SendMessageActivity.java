package se.miun.android_app.MasterUnit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
import se.miun.android_app.EmployeeUnit.EmployeeUnitActivity;
import se.miun.android_app.Model.Company;
import se.miun.android_app.Model.Employee;
import se.miun.android_app.R;

public class SendMessageActivity extends AppCompatActivity {

    private Spinner senderTypeSpinner, employeeSpinner, companySpinner, employeesInCompanySpinner;
    private List<String> senderTypeList, companyList, employeeList, employeeInCompanyList;
    private List<Spinner> theSpinners = new ArrayList<>();
    private Context context;
    private ApiInterface apiInterface;
    private int HTTP_RESPONSE_ACCEPTED = 200;
    private String company, employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        context = this;


        // Initialize the "senderto" spinner
        senderTypeSpinner = (Spinner) findViewById(R.id.senderTypeSpinner);
        senderTypeList = new ArrayList<>();
        senderTypeList.add("Select one option...");
        senderTypeList.add("Company");
        senderTypeList.add("Employee");
        senderTypeList.add("Everyone");
        ArrayAdapter<String> senderTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, senderTypeList);
        senderTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        senderTypeSpinner.setAdapter(senderTypeAdapter);
        senderTypeSpinner.setSelection(0);
        senderTypeSpinner.setOnItemSelectedListener(new SenderTypeOnItemSelectedListener());
        theSpinners.add(senderTypeSpinner);

        // Initilize company spinner
        companySpinner = (Spinner) findViewById(R.id.companySpinner);
        companyList = new ArrayList<>();
        companyList.add("Select company to send message to...");
        ArrayAdapter<String> companyAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, companyList);
        companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        companySpinner.setAdapter(companyAdapter);
        companySpinner.setSelection(0);
        companySpinner.setOnItemSelectedListener(new CompanyOnItemSelectedListener());
        theSpinners.add(companySpinner);

        // Initialize the "employeeSpinner" spinner
        employeeSpinner = (Spinner) findViewById(R.id.employeeSpinner);
        employeeList = new ArrayList<>();
        employeeList.add("Select employee to send message to...");
        ArrayAdapter<String> employeeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, employeeList);
        employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employeeSpinner.setAdapter(employeeAdapter);
        employeeSpinner.setSelection(0);
        employeeSpinner.setOnItemSelectedListener(new EmployeeOnItemSelectedListener());
        theSpinners.add(employeeSpinner);


        // Initialize employeesincompany spinner
        employeesInCompanySpinner = (Spinner) findViewById(R.id.employeesInCompanySpinner);
        employeeInCompanyList = new ArrayList<>();
        employeeInCompanyList.add("Select employee to send message to...");
        ArrayAdapter<String> employeeInCompanyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, employeeInCompanyList);
        employeeInCompanyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employeesInCompanySpinner.setAdapter(employeeInCompanyAdapter);
        employeesInCompanySpinner.setSelection(0);
        employeesInCompanySpinner.setOnItemSelectedListener(new EmployeeInCompanyOnItemSelectedListener());
        theSpinners.add(employeesInCompanySpinner);


    }

    private void hideAllSpinnerExcept(Spinner spinner) {
        for (Spinner s : theSpinners) {
            if (!s.equals(spinner)) {
                s.setVisibility(View.GONE);
            }
        }
    }


    // Class that is used for listener for the sendertype spinner
    private class SenderTypeOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // Check which kind of sendertype
            String senderType = parent.getItemAtPosition(position).toString();
            if (senderType.equals("Company")) {
                // List companies
                companySpinner.setVisibility(View.VISIBLE);
                employeeSpinner.setVisibility(View.GONE);
                getCompanies();

            } else if (senderType.equals("Employee")) {

                employeeSpinner.setVisibility(View.VISIBLE);
                companySpinner.setVisibility(View.GONE);
                getEmployees();


            } else if (senderType.equals("Everyone")) {


            }

        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {        }


        private void getCompanies() {
            Retrofit retrofit;
            retrofit = ApiClient.getApiClient();
            apiInterface = retrofit.create(ApiInterface.class);
            Call<ArrayList<Company>> call;
            call = apiInterface.getAllCompanies();
            call.enqueue(new Callback<ArrayList<Company>>() {
                @Override
                public void onResponse(Call<ArrayList<Company>> call, Response<ArrayList<Company>> response) {
                    if (response.code() == HTTP_RESPONSE_ACCEPTED) {
                        // Clear the list of companies and add all companies
                        companyList.clear();
                        companyList.add("Select company...");
                        for (Company c : response.body()) {
                            companyList.add(c.getCompanyName());
                        }
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<Company>> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void getEmployees(){
            Retrofit retrofit;
            retrofit = ApiClient.getApiClient();
            apiInterface = retrofit.create(ApiInterface.class);
            Call<ArrayList<Employee>> call = null;
            call = apiInterface.getEmployees();
            call.enqueue(new Callback<ArrayList<Employee>>() {
                @Override
                public void onResponse(Call<ArrayList<Employee>> call, Response<ArrayList<Employee>> response) {
                    if (response.code() == HTTP_RESPONSE_ACCEPTED) {
                        // Clear the list of companies and add all companies
                        employeeList.clear();
                        employeeList.add("Select employee...");
                        for (Employee e : response.body()) {
                            employeeList.add(e.getEmployeeFirstName() + " " + e.getEmployeeLastName() + " (" + e.getEmployeeCompany() + ")");
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

    private class EmployeeInCompanyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            employee = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    private class EmployeeOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            employee = parent.getItemAtPosition(position).toString();


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class CompanyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            company = parent.getItemAtPosition(position).toString();
            if(!company.equals("Select company to send message to...")){
                getEmployeesInCompany();
                employeesInCompanySpinner.setVisibility(View.VISIBLE);
                Toast.makeText(context, "COmpany: " + company, Toast.LENGTH_SHORT).show();
            }


        }

        private void getEmployeesInCompany() {
            Retrofit retrofit;
            retrofit = ApiClient.getApiClient();
            apiInterface = retrofit.create(ApiInterface.class);
            Call<ArrayList<Employee>> call = null;
            call = apiInterface.getEmployeesInCompany(company);
            call.enqueue(new Callback<ArrayList<Employee>>() {
                @Override
                public void onResponse(Call<ArrayList<Employee>> call, Response<ArrayList<Employee>> response) {
                    if (response.code() == HTTP_RESPONSE_ACCEPTED) {
                        // Clear the list of companies and add all companies
                        employeeInCompanyList.clear();
                        employeeInCompanyList.add("Select employee...");
                        for (Employee e : response.body()) {
                            employeeInCompanyList.add(e.getEmployeeFirstName() + " " + e.getEmployeeLastName() + " (" + e.getEmployeeCompany() + ")");
                        }
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<Employee>> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


}
