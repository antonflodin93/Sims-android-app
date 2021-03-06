package se.miun.android_app.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import se.miun.android_app.R;

import java.util.ArrayList;

import se.miun.android_app.Model.Employee;

public class EmployeeStatusAdapter extends ArrayAdapter<Employee> {

    private ArrayList<Employee> employees;
    public EmployeeStatusAdapter(Context context, ArrayList<Employee> employees) {
        super(context, 0, employees);
        this.employees = employees;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Employee employee = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.employee_status_object, parent, false);
        }
        // Lookup view for data population
        TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView statusTextView = (TextView) convertView.findViewById(R.id.statusTextView);

        if(employee.isAcknowledged()){
            statusTextView.setText("ACKNOWLEDGED");
        } else {
            statusTextView.setText("NOT ACKNOWLEDGED");
        }
        nameTextView.setText(employee.getEmployeeFirstName() + " " + employee.getEmployeeLastName());

        return convertView;
    }
}