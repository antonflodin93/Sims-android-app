package se.miun.android_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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
import se.miun.android_app.EmployeeUnit.EmployeeUnitActivity;
import se.miun.android_app.MasterUnit.MasterUnitActivity;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginBtn, createAccountBtn;
    private EditText passwordEditText, accountNameEditText;
    private TextView errormessageTextView;
    private ApiInterface apiInterface;
    private Context context;
    private String userType;
    private final static int HTTP_RESPONSE_UNAUTHORIZED = 401;
    private final static int HTTP_RESPONSE_NOT_FOUND = 404;
    private final static int HTTP_RESPONSE_ACCEPTED = 202;

    private Callback<ResponseBody> loginCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Get the usertype, master or employee
        userType = getIntent().getStringExtra("userType");
        Toast.makeText(this, userType, Toast.LENGTH_SHORT).show();


        context = this;

        // Initialize components
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        createAccountBtn = (Button) findViewById(R.id.createAccountBtn);
        createAccountBtn.setOnClickListener(this);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        accountNameEditText = (EditText) findViewById(R.id.accountNameEditText);
        errormessageTextView = (TextView) findViewById(R.id.errormessageTextView);

        if(userType.equals("MASTER")) {
            passwordEditText.setText("masterunit");
            accountNameEditText.setText("masterunit");
            createAccountBtn.setEnabled(false);
            createAccountBtn.setVisibility(View.GONE);

        } else{
            passwordEditText.setText("employee");
            accountNameEditText.setText("employee");
        }

        // When the user is logging in
        loginCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Check if user is matched in the database
                if (response.code() == HTTP_RESPONSE_ACCEPTED) {

                    // Check which kind of user and start specific activity
                    if (userType.equals("MASTER")) {
                        Intent myIntent = new Intent(getApplicationContext(), MasterUnitActivity.class);
                        context.startActivity(myIntent);

                    } else if (userType.equals("EMPLOYEE")) {
                        Intent myIntent = new Intent(getApplicationContext(), EmployeeUnitActivity.class);
                        context.startActivity(myIntent);
                    }

                } else {
                    errormessageTextView.setVisibility(View.VISIBLE);
                    errormessageTextView.setTextColor(Color.RED);
                    try {
                        errormessageTextView.setText(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errormessageTextView.setText(t.getMessage());
            }
        };

    }

    @Override
    public void onClick(View view) {
        // If the login was pressed
        if (view.getId() == R.id.loginBtn) {
            // Check if the fields are correct and then login user
            checkCorrectCredentials();
            // If the create accound button was pressed
        } else if (view.getId() == R.id.createAccountBtn) {
            // Start the register activity
            Intent myIntent = new Intent(getApplicationContext(), RegisterAccountActivity.class);
            this.startActivity(myIntent);
        }
    }

    // Check if every field is ok
    private void checkCorrectCredentials() {
        // If one of the fields is empty, highlight with red color and empty both of them
        if (passwordEditText.getText().length() == 0 || accountNameEditText.getText().length() == 0) {
            accountNameEditText.setHintTextColor(Color.RED);
            accountNameEditText.setText("");
            accountNameEditText.setHint(accountNameEditText.getHint());
            passwordEditText.setHintTextColor(Color.RED);
            passwordEditText.setText("");
            passwordEditText.setHint(passwordEditText.getHint());
            errormessageTextView.setVisibility(View.VISIBLE);
            errormessageTextView.setTextColor(Color.RED);
            errormessageTextView.setText("You must enter both fields.");
        } else {
            // Login the user
            Retrofit retrofit;
            retrofit = ApiClient.getApiClient();
            apiInterface = retrofit.create(ApiInterface.class);
            // Combine username and password with : as delimiter
            String concatedUserPassword = accountNameEditText.getText().toString() + ":" + passwordEditText.getText().toString();

            // Create the basic authentication header
            String authorizedHeader = "Basic " + Base64.encodeToString(concatedUserPassword.getBytes(), Base64.NO_WRAP);
            Call<ResponseBody> call = null;
            Toast.makeText(context, userType, Toast.LENGTH_LONG).show();


            if (userType.equals("MASTER")) {
                call = apiInterface.loginAsMaster(authorizedHeader);
            } else if (userType.equals("EMPLOYEE")) {
                call = apiInterface.loginAsEmployee(authorizedHeader);
            }

            call.enqueue(loginCallback);
        }
    }
}
