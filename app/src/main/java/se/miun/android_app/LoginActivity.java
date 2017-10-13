package se.miun.android_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginBtn, createAccountBtn;
    private EditText passwordEditText, accountNameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize components
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        Button createAccountBtn = (Button) findViewById(R.id.createAccountBtn);
        createAccountBtn.setOnClickListener(this);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        EditText accountNameEditText = (EditText) findViewById(R.id.passwordEditText);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.loginBtn) {
            login();
        } else if(view.getId() == R.id.createAccountBtn){
            // Start the rangetest activity
            Intent myIntent = new Intent(getApplicationContext(), RegisterAccountActivity.class);
            this.startActivity(myIntent);
        }

    }

    private void login() {
        // To do: DB connection

        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        this.startActivity(myIntent);
    }
}
