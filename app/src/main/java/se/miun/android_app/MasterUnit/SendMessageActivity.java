package se.miun.android_app.MasterUnit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
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
import se.miun.android_app.LoginActivity;
import se.miun.android_app.Model.Company;
import se.miun.android_app.Model.Employee;
import se.miun.android_app.Model.Message;
import se.miun.android_app.R;

public class SendMessageActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner senderTypeSpinner, employeeSpinner, companySpinner, employeesInCompanySpinner;
    private List<String> senderTypeList = new ArrayList<>(), companyList = new ArrayList<>();
    private ArrayList<Employee> employees = new ArrayList<>(), employeesInCompany = new ArrayList<>();
    private Context context;
    private ApiInterface apiInterface;
    private int HTTP_RESPONSE_ACCEPTED = 200;
    private String company, employeeString;
    private Button sendMessagesBtn, cancelSendMessageBtn;
    private EditText messageEditText, subjectEditText;
    private EmployeeSpinnerAdapter employeeAdapter, employeeInCompanyAdapter;
    private int selectedEmployee;
    private CheckBox listEmployeesCheckBox;

    private enum SenderType {
        BROADCAST,
        EMPLOYEE,
        COMPANY
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
        cancelSendMessageBtn = (Button) findViewById(R.id.cancelSendMessageBtn);
        cancelSendMessageBtn.setOnClickListener(this);


        messageEditText = (EditText) findViewById(R.id.messageEditText);
        subjectEditText = (EditText) findViewById(R.id.subjectEditText);
        listEmployeesCheckBox = (CheckBox) findViewById(R.id.listEmployeesCheckBox);
        listEmployeesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // If box is checked, display list of employees in the company, otherwise hide the spinner of the employees
                if (isChecked) {
                    getEmployeesInCompany();
                    employeesInCompanySpinner.setVisibility(View.VISIBLE);
                    employeesInCompanySpinner.setSelection(0);

                } else {
                    employeesInCompanySpinner.setVisibility(View.GONE);
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

        // Initilize the other spinners
        companySpinner = (Spinner) findViewById(R.id.companySpinner);
        employeeSpinner = (Spinner) findViewById(R.id.employeeSpinner);
        employeesInCompanySpinner = (Spinner) findViewById(R.id.employeesInCompanySpinner);

    }


    @Override
    public void onClick(View view) {
        // If button user wants to send message
        if (view.getId() == R.id.sendMessagesBtn) {
            if (messageEditText.getText().toString().trim().length() == 0) {
                Toast.makeText(context, "You need to add text in message field.", Toast.LENGTH_SHORT).show();
            } else if (subjectEditText.getText().toString().trim().length() == 0) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setTitle("Empty subject");

                // set dialog message
                alertDialogBuilder
                        .setMessage("You have not entered any subject. Are you sure you want to send the message without subject?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                subjectEditText.setText("(No subject)");

                                Message message = new Message(subjectEditText.getText().toString(), messageEditText.getText().toString());
                                if (senderType == SenderType.BROADCAST) {
                                    sendBroadCastMessage(message);

                                } else if (senderType == SenderType.COMPANY) {
                                    // Check if user wants to send to employee in company
                                    if (listEmployeesCheckBox.isChecked()) {
                                        sendEmployeeMessage(message);

                                    } else {
                                        sendCompanyMessage(message);
                                    }


                                } else if (senderType == SenderType.EMPLOYEE) {
                                    sendEmployeeMessage(message);
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                Message message = new Message(subjectEditText.getText().toString(), messageEditText.getText().toString());
                if (senderType == SenderType.BROADCAST) {
                    sendBroadCastMessage(message);

                } else if (senderType == SenderType.COMPANY) {
                    // Check if user wants to send to employee in company
                    if (listEmployeesCheckBox.isChecked()) {
                        sendEmployeeMessage(message);

                    } else {
                        sendCompanyMessage(message);
                    }
                } else if (senderType == SenderType.EMPLOYEE) {
                    sendEmployeeMessage(message);
                }
            }
        } else if(view.getId() == R.id.cancelSendMessageBtn){
            Intent intent = new Intent(getBaseContext(), MasterUnitActivity.class);
            startActivity(intent);
        }

    }

    private void sendEmployeeMessage(Message message) {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call;
        call = apiInterface.insertEmployeeMessage(message, selectedEmployee);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == HTTP_RESPONSE_ACCEPTED) {
                    Toast.makeText(context, "Sent message", Toast.LENGTH_SHORT).show();
                    messageEditText.setText("");
                    subjectEditText.setText("");

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
                    messageEditText.setText("");
                    subjectEditText.setText("");

                } else {
                    try {
                        Toast.makeText(context, "Code: " + response.errorBody().string(), Toast.LENGTH_LONG).show();
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
                    messageEditText.setText("");
                    subjectEditText.setText("");

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
                listEmployeesCheckBox.setVisibility(View.VISIBLE);
                listEmployeesCheckBox.setChecked(false);

            } else if (senderTypeString.equals("Employee")) {
                senderType = SenderType.EMPLOYEE;
                employeeSpinner.setVisibility(View.VISIBLE);
                companySpinner.setVisibility(View.GONE);
                listEmployeesCheckBox.setVisibility(View.GONE);
                employeesInCompanySpinner.setVisibility(View.GONE);
                getEmployees();


            } else if (senderTypeString.equals("Everyone")) {
                senderType = SenderType.BROADCAST;
                companySpinner.setVisibility(View.GONE);
                employeeSpinner.setVisibility(View.GONE);
                listEmployeesCheckBox.setVisibility(View.GONE);
                employeesInCompanySpinner.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }


    // Class that is used for listener for the employee spinner
    private class EmployeeOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
            if (listEmployeesCheckBox.isChecked()) {
                getEmployeesInCompany();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private class EmployeeInCompanyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedEmployee = employeeAdapter.getSelectedEmployee();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
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
                    Toast.makeText(context, "size: " + response.body().size(), Toast.LENGTH_SHORT).show();
                    companyList.clear();
                    for (Company c : response.body()) {
                        companyList.add(c.getCompanyName());
                    }
                    ArrayAdapter<String> companyAdapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_item, companyList);
                    companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    companySpinner.setAdapter(companyAdapter);
                    companySpinner.setOnItemSelectedListener(new CompanyOnItemSelectedListener());
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
        Call<ArrayList<Employee>> call;
        call = apiInterface.getEmployees();
        call.enqueue(new Callback<ArrayList<Employee>>() {
            @Override
            public void onResponse(Call<ArrayList<Employee>> call, Response<ArrayList<Employee>> response) {
                if (response.code() == HTTP_RESPONSE_ACCEPTED) {

                    employees = response.body();
                    employeeAdapter = new EmployeeSpinnerAdapter(SendMessageActivity.this,
                            android.R.layout.simple_spinner_item, employees);
                    Toast.makeText(context, "Here " + employees.size(), Toast.LENGTH_SHORT).show();
                    employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    employeeSpinner.setAdapter(employeeAdapter);
                    employeeSpinner.setSelection(0);
                    employeeSpinner.setOnItemSelectedListener(new EmployeeOnItemSelectedListener());


                }
            }

            @Override
            public void onFailure(Call<ArrayList<Employee>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getEmployeesInCompany() {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        apiInterface = retrofit.create(ApiInterface.class);
        Call<ArrayList<Employee>> call;
        call = apiInterface.getEmployeesInCompany(company);
        call.enqueue(new Callback<ArrayList<Employee>>() {
            @Override
            public void onResponse(Call<ArrayList<Employee>> call, Response<ArrayList<Employee>> response) {
                if (response.code() == HTTP_RESPONSE_ACCEPTED) {
                    employeesInCompany = response.body();
                    employeeAdapter = new EmployeeSpinnerAdapter(SendMessageActivity.this,
                            android.R.layout.simple_spinner_item, employeesInCompany);
                    employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    employeesInCompanySpinner.setAdapter(employeeAdapter);
                    employeesInCompanySpinner.setSelection(0);
                    employeesInCompanySpinner.setOnItemSelectedListener(new EmployeeInCompanyOnItemSelectedListener());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Employee>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
