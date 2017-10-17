package se.miun.android_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
import se.miun.android_app.model.Employee;


public class RegisterAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userNameEditText, emailEditText, passwordEditText, reEnterPasswordEditText, phoneNumberEditText, companyNameEditText,
            firstNameEditText, lastNameEditText;
    private TextView errorMessageTextView;
    private Button cancelToLogin, createNewAccount;
    private ApiInterface apiInterface;
    private Context context;
    private int RESPONSE_INTERNAL_SERVER_ERROR = 500;
    private int RESPONSE_FORBIDDEN = 403;
    private int RESPONSE_OK = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        // Initialize components
        cancelToLogin = (Button) findViewById(R.id.cancelToLogin);
        cancelToLogin.setOnClickListener(this);
        createNewAccount = (Button) findViewById(R.id.createNewAccount);
        createNewAccount.setOnClickListener(this);
        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        reEnterPasswordEditText = (EditText) findViewById(R.id.reEnterPasswordEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        companyNameEditText = (EditText) findViewById(R.id.companyNameEditText);
        errorMessageTextView = (TextView) findViewById(R.id.errorMessageTextView);
        context = this;
    }

    @Override
    public void onClick(View view) {
        // Create account or go back to login page
        if (view.getId() == R.id.createNewAccount) {
            createNewAccount();
        } else if (view.getId() == R.id.cancelToLogin) {
            Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
            this.startActivity(myIntent);
        }

    }

    private void createNewAccount() {
        if (correctCredentials() == true) {
            Toast.makeText(getBaseContext(), "correct", Toast.LENGTH_SHORT);
            insertToDatabase();
        }


    }

    // Inserts the user data to database mang
    private void insertToDatabase() {
        // Create instance of apiinterface using retrofit instance
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        apiInterface = retrofit.create(ApiInterface.class);

        // Post a employee to the database
        //Employee employee = new Employee("Anton", "Flodin", "anton", "anfl120@hotmail.com", "hej", "0702733166", "MITTCOMANY");
        Employee employee = new Employee(firstNameEditText.getText().toString(), lastNameEditText.getText().toString(),
                userNameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString(),
                phoneNumberEditText.getText().toString(), companyNameEditText.getText().toString());

        Call<ResponseBody> call = apiInterface.insertEmployee(employee);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // If employee could be added
                if (response.code() == RESPONSE_OK) {
                    // Go to login screen
                    Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    context.startActivity(myIntent);
                } else if (response.code() == RESPONSE_FORBIDDEN || response.code() == RESPONSE_INTERNAL_SERVER_ERROR) {
                    errorMessageTextView.setVisibility(View.VISIBLE);
                    errorMessageTextView.setTextColor(Color.RED);
                    try {
                        errorMessageTextView.setText(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    // Check if the entered information is correct
    private boolean correctCredentials() {
        boolean correct = true;

        if (isEmpty(firstNameEditText)) {
            userNameEditText.setHintTextColor(Color.RED);
            userNameEditText.setText("");
            userNameEditText.setHint(userNameEditText.getHint());
            correct = false;
        }

        if (isEmpty(lastNameEditText)) {
            userNameEditText.setHintTextColor(Color.RED);
            userNameEditText.setText("");
            userNameEditText.setHint(userNameEditText.getHint());
            correct = false;
        }

        if (isEmpty(userNameEditText)) {
            userNameEditText.setHintTextColor(Color.RED);
            userNameEditText.setText("");
            userNameEditText.setHint(userNameEditText.getHint());
            correct = false;
        }

        if (isEmpty(emailEditText)) {
            emailEditText.setHintTextColor(Color.RED);
            emailEditText.setText("");
            emailEditText.setHint(emailEditText.getHint());
            correct = false;
        } else if (!isEmail(emailEditText)) {
            emailEditText.setHintTextColor(Color.RED);
            emailEditText.setHint("Enter a correct email address");
            correct = false;
        }

        if (isEmpty(passwordEditText)) {
            passwordEditText.setHintTextColor(Color.RED);
            reEnterPasswordEditText.setText("");
            passwordEditText.setHint(passwordEditText.getHint());
            correct = false;
        }
        if (isEmpty(reEnterPasswordEditText)) {
            reEnterPasswordEditText.setHintTextColor(Color.RED);
            reEnterPasswordEditText.setText("");
            passwordEditText.setText("");
            reEnterPasswordEditText.setHint(reEnterPasswordEditText.getHint());
            correct = false;
        } else if (!reEnterPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())) {
            reEnterPasswordEditText.setHintTextColor(Color.RED);
            reEnterPasswordEditText.setHint("The passwords do not match");
            reEnterPasswordEditText.setText("");
            passwordEditText.setText("");
            passwordEditText.setText("");
            correct = false;
        }
        if (isEmpty(phoneNumberEditText)) {
            phoneNumberEditText.setHintTextColor(Color.RED);
            phoneNumberEditText.setHint("Enter a valid phone number (0701234567)");
            correct = false;
        } /*else if (phoneNumberEditText.getInputType() != InputType.TYPE_CLASS_NUMBER) {
            phoneNumberEditText.setHintTextColor(Color.RED);
            phoneNumberEditText.setText("");
            phoneNumberEditText.setHint(phoneNumberEditText.getHint());
            correct = false;
        }*/
        if (isEmpty(companyNameEditText)) {
            companyNameEditText.setHintTextColor(Color.RED);
            companyNameEditText.setHint(companyNameEditText.getHint());
            correct = false;
        }

        return correct;
    }

    // Check if the field is empty
    private boolean isEmpty(EditText text) {
        if (text.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    // Check if it is a email
    boolean isEmail(EditText email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches();
    }
}
