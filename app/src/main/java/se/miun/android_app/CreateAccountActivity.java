package se.miun.android_app;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userNameEditText, emailEditText, passwordEditText, reEnterPasswordEditText, phoneNumberEditText, companyNameEditText;
    private Button cancelToLogin, createNewAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Initialize components
        cancelToLogin = (Button) findViewById(R.id.cancelToLogin);
        cancelToLogin.setOnClickListener(this);
        createNewAccount = (Button) findViewById(R.id.createNewAccount);
        createNewAccount.setOnClickListener(this);

        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        reEnterPasswordEditText = (EditText) findViewById(R.id.reEnterPasswordEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        companyNameEditText = (EditText) findViewById(R.id.companyNameEditText);
    }

    @Override
    public void onClick(View view) {
        // Create account or go back to login page
        if(view.getId() == R.id.createNewAccount){
            createNewAccount();
        } else if(view.getId() == R.id.cancelToLogin){
            Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
            this.startActivity(myIntent);
        }

    }

    private void createNewAccount() {
        if(correctCredentials() == true){
            Toast.makeText(getBaseContext(), "correct", Toast.LENGTH_SHORT);
            // Go to login screen
            Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
            this.startActivity(myIntent);
        }
        // To do: DB connection



    }

    // Check if the entered information is correct
    private boolean correctCredentials() {
        boolean correct = false;
        if(isEmpty(userNameEditText) ){
            userNameEditText.setHintTextColor(Color.RED);
            userNameEditText.setText("");
            userNameEditText.setHint(userNameEditText.getHint());
            correct = false;
        }

        if(isEmpty(emailEditText) ){
            emailEditText.setHintTextColor(Color.RED);
            emailEditText.setText("");
            emailEditText.setHint(emailEditText.getHint());
            correct = false;
        } else if(!isEmail(emailEditText)){
            emailEditText.setHintTextColor(Color.RED);
            emailEditText.setHint("Enter a correct email address");
            correct = false;
        }

        if(isEmpty(passwordEditText) ){
            passwordEditText.setHintTextColor(Color.RED);
            reEnterPasswordEditText.setText("");
            passwordEditText.setHint(passwordEditText.getHint());
            correct = false;
        }
        if(isEmpty(reEnterPasswordEditText)){
            reEnterPasswordEditText.setHintTextColor(Color.RED);
            reEnterPasswordEditText.setText("");
            passwordEditText.setText("");
            reEnterPasswordEditText.setHint(reEnterPasswordEditText.getHint());
            correct = false;
        } else if(!reEnterPasswordEditText.equals(passwordEditText) ){
            reEnterPasswordEditText.setHintTextColor(Color.RED);
            reEnterPasswordEditText.setHint("The passwords do not match");
            reEnterPasswordEditText.setText("");
            passwordEditText.setText("");
            passwordEditText.setText("");
            correct = false;
        }
        if(isEmpty(phoneNumberEditText) ){
            phoneNumberEditText.setHintTextColor(Color.RED);
            phoneNumberEditText.setHint("Enter a valid phone number (0701234567)");
            correct = false;
        } else if(phoneNumberEditText.getInputType() != InputType.TYPE_CLASS_NUMBER){
            phoneNumberEditText.setHintTextColor(Color.RED);
            phoneNumberEditText.setText("");
            phoneNumberEditText.setHint(phoneNumberEditText.getHint());
            correct = false;
        }
        if(isEmpty(companyNameEditText) ){
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
