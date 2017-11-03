package se.miun.android_app.MasterUnit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import se.miun.android_app.R;

public class SendMessageActivity extends AppCompatActivity {

    private Spinner senderTypeSpinner, employeeSpinner, companySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        // Initialize the "senderto" spinner
        senderTypeSpinner = (Spinner) findViewById(R.id.senderTypeSpinner);
        List<String> list = new ArrayList<String>();
        list.add("Company");
        list.add("Employee");
        list.add("Everyone");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        senderTypeSpinner.setAdapter(dataAdapter);

        senderTypeSpinner = (Spinner) findViewById(R.id.senderTypeSpinner);
        senderTypeSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        employeeSpinner = (Spinner) findViewById(R.id.employeeSpinner);
        companySpinner = (Spinner) findViewById(R.id.companySpinner);

    }

    // Class that is used for listener for the spinner
    private class CustomOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
