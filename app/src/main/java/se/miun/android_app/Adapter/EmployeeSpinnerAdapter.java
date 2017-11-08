package se.miun.android_app.Adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import se.miun.android_app.Model.Employee;

public class EmployeeSpinnerAdapter extends ArrayAdapter<Employee> {

    private Context context;
    private ArrayList<Employee> employees;
    private int selectedEmployee;

    public EmployeeSpinnerAdapter(Context context, int textViewResourceId, ArrayList<Employee> employees) {
        super(context, textViewResourceId, employees);
        this.context = context;
        this.employees = employees;
    }

    public int getSelectedEmployee(){
        return selectedEmployee;
    }

    @Override
    public int getCount(){
        return employees.size();
    }

    @Override
    public Employee getItem(int position){
        selectedEmployee = Integer.parseInt(employees.get(position).getEmployeeId());
        Toast.makeText(context, "Got item: " + employees.get(position).getEmployeeId(), Toast.LENGTH_SHORT).show();
        return employees.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView employeeTextView = new TextView(context);
        Employee employee = employees.get(position);
        employeeTextView.setText(employee.getEmployeeFirstName() + " " + employee.getEmployeeLastName() + "(" + employee.getEmployeeCompany() + ")");

        // And finally return your dynamic (or custom) view for each spinner item
        return employeeTextView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView employeeTextView = new TextView(context);
        Employee employee = employees.get(position);
        employeeTextView.setText(employee.getEmployeeFirstName() + " " + employee.getEmployeeLastName() + "(" + employee.getEmployeeCompany() + ")");

        return employeeTextView;
    }
}