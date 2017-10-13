package se.miun.android_app;

import android.content.Intent;
import android.graphics.Color;
import android.support.transition.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginBtn, createAccountBtn;
    private EditText passwordEditText, accountNameEditText;
    private TextView errormessageTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize components
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        Button createAccountBtn = (Button) findViewById(R.id.createAccountBtn);
        createAccountBtn.setOnClickListener(this);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        accountNameEditText = (EditText) findViewById(R.id.accountNameEditText);
        errormessageTextView = (TextView) findViewById(R.id.errormessageTextView);
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
            if (correctCredentials()) {
                //Start the main activity
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                this.startActivity(myIntent);
            } else {
                errormessageTextView.setVisibility(View.VISIBLE);
                errormessageTextView.setTextColor(Color.RED);
                errormessageTextView.setText("Username or password is invalid.");
            }

        }
    }

    private boolean isEmpty(EditText text) {
        if (text.getText().toString().trim().length() > 0) {
            return false;
        }

        return true;
    }

    private boolean correctCredentials() {
        return false;

    }

}
