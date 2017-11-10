package se.miun.android_app.Adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import se.miun.android_app.Model.Employee;

public class EmployeeSpinnerAdapter extends ArrayAdapter<Employee> {

    private Context context;
    private ArrayList<Employee> employees;

    public EmployeeSpinnerAdapter(Context context, int textViewResourceId, ArrayList<Employee> employees) {
        super(context, textViewResourceId, employees);
        this.context = context;
        this.employees = employees;
    }


    @Override
    public int getCount(){
        return employees.size();
    }

    @Override
    public Employee getItem(int position){
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

        TextView employeeSpinnerTextView = new TextView(context);
        employeeSpinnerTextView.setTextColor(Color.BLACK);
        employeeSpinnerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);

        Employee employee = employees.get(position);
        employeeSpinnerTextView.setText(employee.getEmployeeFirstName() + " " + employee.getEmployeeLastName() + " (" + employee.getEmployeeCompany() + ")");

        // And finally return your dynamic (or custom) view for each spinner item
        return employeeSpinnerTextView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView employeeSpinnerTextView = new TextView(context);
        employeeSpinnerTextView.setTextColor(Color.BLACK);
        employeeSpinnerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        Employee employee = employees.get(position);
        employeeSpinnerTextView.setText(employee.getEmployeeFirstName() + " " + employee.getEmployeeLastName() + " (" + employee.getEmployeeCompany() + ")");

        // And finally return your dynamic (or custom) view for each spinner item
        return employeeSpinnerTextView;
    }
}