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
    private final static int HTTP_RESPONSE_UNAUTHORIZED = 401;
    private final static int HTTP_RESPONSE_NOT_FOUND = 404;
    private final static int HTTP_RESPONSE_ACCEPTED = 202;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        context = this;

        // Initialize components
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        createAccountBtn = (Button) findViewById(R.id.createAccountBtn);
        createAccountBtn.setOnClickListener(this);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        accountNameEditText = (EditText) findViewById(R.id.accountNameEditText);
        errormessageTextView = (TextView) findViewById(R.id.errormessageTextView);
        passwordEditText.setText("masterunit");
        accountNameEditText.setText("masterunit");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.loginBtn) {
            login();

        } else if (view.getId() == R.id.createAccountBtn) {
            // Start the register activity
            Intent myIntent = new Intent(getApplicationContext(), RegisterAccountActivity.class);
            this.startActivity(myIntent);
        }

    }

    private void login() {
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
            // Check if user exists in data base
            checkCorrectCredentials();
        }
    }

    private void checkCorrectCredentials() {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        apiInterface = retrofit.create(ApiInterface.class);

        // Combine username and password with : as delimiter
        String concatedUserPassword = accountNameEditText.getText().toString() + ":" + passwordEditText.getText().toString();

        // Create the basic authentication header
        String authorizedHeader = "Basic " + Base64.encodeToString(concatedUserPassword.getBytes(), Base64.NO_WRAP);
        Call<String> call = apiInterface.login(authorizedHeader);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // Check if user is matched in the database
                if (response.code() == HTTP_RESPONSE_ACCEPTED) {
                    Intent myIntent = new Intent(getApplicationContext(), MasterUnitActivity.class);
                    context.startActivity(myIntent);
                } else if (response.code() == HTTP_RESPONSE_UNAUTHORIZED) {
                    errormessageTextView.setVisibility(View.VISIBLE);
                    errormessageTextView.setTextColor(Color.RED);
                    errormessageTextView.setText("Username or password is invalid.");
                } else if (response.code() < 500 && response.code() >= 400){
                    errormessageTextView.setVisibility(View.VISIBLE);
                    errormessageTextView.setTextColor(Color.RED);
                    errormessageTextView.setText("Client/Server error occurred (" + String.valueOf(response.code()) + ")");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                errormessageTextView.setText(t.getMessage());
            }
        });
    }
}
