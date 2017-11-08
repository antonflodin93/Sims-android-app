package se.miun.android_app.MasterUnit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
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
import se.miun.android_app.Adapter.EmployeeSpinnerAdapter;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
import se.miun.android_app.EmployeeUnit.EmployeeUnitActivity;
import se.miun.android_app.Model.Company;
import se.miun.android_app.Model.Employee;
import se.miun.android_app.Model.Message;
import se.miun.android_app.R;

public class SendMessageActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner senderTypeSpinner, employeeSpinner, companySpinner, employeesInCompanySpinner;
    private List<String> senderTypeList = new ArrayList<>(), companyList = new ArrayList<>(), employeeList = new ArrayList<>(), employeeInCompanyList = new ArrayList<>();
    private ArrayList<Employee> employees = new ArrayList<>();
    private List<Spinner> theSpinners = new ArrayList<>();
    private Context context;
    private ApiInterface apiInterface;
    private int HTTP_RESPONSE_ACCEPTED = 200;
    private String company, employeeString;
    private String PROMPT_COMPANY_SPINNER_TEXT = "Select company to send message to...";
    private String PROMPT_EMPLOYEE_SPINNER = "Select employee to send message to...";
    private Button sendMessagesBtn;
    private EditText messageEditText, subjectEditText;
    private EmployeeSpinnerAdapter employeeAdapter;
    private int selectedEmployee;
    private CheckBox listEmployeesCheckBox;


    private enum SenderType {
        BROADCAST,
        EMPLOYEE,
        COMPANY;
    }

    private SenderType senderType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        context = this;

        // Init components
        sendMessagesBtn = (Button) findViewById(R.id.sendMessagesBtn);
        sendMessagesBtn.setOnClickListener(this);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        subjectEditText = (EditText) findViewById(R.id.subjectEditText);
        listEmployeesCheckBox = (CheckBox) findViewById(R.id.listEmployeesCheckBox);
        listEmployeesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // If box is checked, display list of employees in the company, otherwise hide the spinner of the employees
                if(isChecked){


                } else {

                }
            }
        });


        // Initialize the "senderto" spinner
        senderTypeSpinner = (Spinner) findViewById(R.id.senderTypeSpinner);
        senderTypeList = new ArrayList<>();
        senderTypeList.add("Everyone");
        senderTypeList.add("Company");
        senderTypeList.add("Employee");
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
        companyList.add(PROMPT_COMPANY_SPINNER_TEXT);
        ArrayAdapter<String> companyAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, companyList);
        companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        companySpinner.setAdapter(companyAdapter);
        companySpinner.setSelection(0);
        companySpinner.setOnItemSelectedListener(new CompanyOnItemSelectedListener());
        theSpinners.add(companySpinner);

        // Initialize the employee spinner
        employeeSpinner = (Spinner) findViewById(R.id.employeeSpinner);
        employeeList = new ArrayList<>();
        employeeList.add(PROMPT_EMPLOYEE_SPINNER);
        employeeAdapter = new EmployeeSpinnerAdapter(SendMessageActivity.this,
                android.R.layout.simple_spinner_item,
                employees);

        //ArrayAdapter<String> employeeAdapter = new ArrayAdapter<>(this,
        //      android.R.layout.simple_spinner_item, employeeList);
        employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employeeSpinner.setAdapter(employeeAdapter);
        employeeSpinner.setSelection(0);
        employeeSpinner.setOnItemSelectedListener(new EmployeeOnItemSelectedListener());
        theSpinners.add(employeeSpinner);


        // Initialize employeesincompany spinner
        employeesInCompanySpinner = (Spinner) findViewById(R.id.employeesInCompanySpinner);
        employeeInCompanyList = new ArrayList<>();
        employeeInCompanyList.add(PROMPT_EMPLOYEE_SPINNER);
        ArrayAdapter<String> employeeInCompanyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, employeeInCompanyList);
        employeeInCompanyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employeesInCompanySpinner.setAdapter(employeeInCompanyAdapter);
        employeesInCompanySpinner.setSelection(0);
        employeesInCompanySpinner.setOnItemSelectedListener(new EmployeeInCompanyOnItemSelectedListener());
        theSpinners.add(employeesInCompanySpinner);
    }

    @Override
    public void onClick(View view) {
        // If button is pressed
        if (view.getId() == R.id.sendMessagesBtn) {
            Message message = new Message(subjectEditText.getText().toString(), messageEditText.getText().toString());
            if (senderType == SenderType.BROADCAST) {
                sendBroadCastMessage(message);

            } else if (senderType == SenderType.COMPANY) {
                if (employeeString == PROMPT_EMPLOYEE_SPINNER) {
                    sendCompanyMessage(message);
                } else {
                    //sendEmployeeMessage(message);
                }
            } else if (senderType == SenderType.EMPLOYEE) {
                //sendEmployeeMessage(message);
            }
        }

    }


    private void sendCompanyMessage(Message message) {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call;
        call = apiInterface.insertCompanyMessage(message, company);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == HTTP_RESPONSE_ACCEPTED) {
                    Toast.makeText(context, "Sent message", Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        Toast.makeText(context, "Code: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendBroadCastMessage(Message message) {

        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call;
        call = apiInterface.insertBroadcastMessage(message);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == HTTP_RESPONSE_ACCEPTED) {
                    Toast.makeText(context, "Sent message", Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        Toast.makeText(context, "Code: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Class that is used for listener for the sendertype spinner
    private class SenderTypeOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // Check which kind of sendertype
            String senderTypeString = parent.getItemAtPosition(position).toString();
            if (senderTypeString.equals("Company")) {
                senderType = SenderType.COMPANY;
                // List companies
                companySpinner.setVisibility(View.VISIBLE);
                employeeSpinner.setVisibility(View.GONE);
                getCompanies();

            } else if (senderTypeString.equals("Employee")) {
                senderType = SenderType.EMPLOYEE;
                employeeSpinner.setVisibility(View.VISIBLE);
                companySpinner.setVisibility(View.GONE);
                employeesInCompanySpinner.setVisibility(View.GONE);
                getEmployees();


            } else if (senderTypeString.equals("Everyone")) {
                senderType = SenderType.BROADCAST;
                companySpinner.setVisibility(View.GONE);
                employeeSpinner.setVisibility(View.GONE);
                employeesInCompanySpinner.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }


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
                        companyList.add(PROMPT_COMPANY_SPINNER_TEXT);
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

        private void getEmployees() {
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
                        employees = response.body();
                        employeeAdapter = new EmployeeSpinnerAdapter(SendMessageActivity.this,
                                android.R.layout.simple_spinner_item,
                                employees);

                        //ArrayAdapter<String> employeeAdapter = new ArrayAdapter<>(this,
                        //      android.R.layout.simple_spinner_item, employeeList);
                        employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        employeeSpinner.setAdapter(employeeAdapter);


                        Toast.makeText(context, "Size " + employees.size(), Toast.LENGTH_SHORT).show();
                        employeeList.clear();
                        employeeList.add(PROMPT_EMPLOYEE_SPINNER);
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
            employeeString = parent.getItemAtPosition(position).toString();
            selectedEmployee = employeeAdapter.getSelectedEmployee();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    private class EmployeeOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            employeeString = parent.getItemAtPosition(position).toString();
            selectedEmployee = employeeAdapter.getSelectedEmployee();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class CompanyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            company = parent.getItemAtPosition(position).toString();
            if (!company.equals(PROMPT_COMPANY_SPINNER_TEXT)) {
                getEmployeesInCompany();
                employeeInCompanyList.clear();
                employeeInCompanyList.add(PROMPT_EMPLOYEE_SPINNER);
                employeesInCompanySpinner.setVisibility(View.VISIBLE);
                employeesInCompanySpinner.setSelection(0);
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
                        employees = response.body();
                        employeeAdapter = new EmployeeSpinnerAdapter(SendMessageActivity.this,
                                android.R.layout.simple_spinner_item,
                                employees);

                        //ArrayAdapter<String> employeeAdapter = new ArrayAdapter<>(this,
                        //      android.R.layout.simple_spinner_item, employeeList);
                        employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        employeeSpinner.setAdapter(employeeAdapter);
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
