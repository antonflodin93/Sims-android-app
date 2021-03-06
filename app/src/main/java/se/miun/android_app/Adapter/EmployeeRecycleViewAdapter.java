package se.miun.android_app.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import se.miun.android_app.Model.Employee;
import se.miun.android_app.R;
import se.miun.android_app.Model.Message;
import android.view.View.OnClickListener;

public class EmployeeRecycleViewAdapter extends RecyclerView.Adapter<EmployeeRecycleViewAdapter.EmployeeViewHolder> implements Filterable {

    private List<Employee> employees = new ArrayList<>();
    private EmployeeFilter employeeFilter;
    private RecyclerView recyclerView;
    private Context context;
    private String latestFloor;
    private String latestBuilding;



    public EmployeeRecycleViewAdapter(Context context, List<Employee> employees) {
        this.employees = employees;
        this.context = context;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list_row_item, parent, false);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(v);
                Employee employee = employees.get(itemPosition);

                // Display dialog
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                builderSingle.setIcon(android.R.drawable.ic_dialog_info);
                builderSingle.setTitle("Information about employee");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
                arrayAdapter.add("Name: " + employee.getEmployeeFirstName() + " " + employee.getEmployeeLastName());
                arrayAdapter.add("Company: " + employee.getEmployeeCompany());
                arrayAdapter.add("Email: " + employee.getEmployeeEmail());
                arrayAdapter.add("Phone Number: " + employee.getEmployeePhonenumber());

                if(latestFloor == null && latestBuilding == null){
                    arrayAdapter.add("Last seen: " +  "No information");
                } else{
                    arrayAdapter.add("Last seen: " +  latestBuilding + "/" + latestFloor);
                }


                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builderSingle.show();




            }
        });

        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {

        holder.employeeNameTextView.setText(employees.get(position).getEmployeeFirstName() + " " + employees.get(position).getEmployeeLastName());
        holder.employeeCompanyTextView.setText(employees.get(position).getEmployeeCompany());
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }




    public class EmployeeViewHolder extends RecyclerView.ViewHolder{
        TextView employeeNameTextView, employeeCompanyTextView;

        public EmployeeViewHolder(View itemView) {
            super(itemView);

            // Init the fields for each row
            employeeNameTextView = (TextView) itemView.findViewById(R.id.employeeNameTextView);
            employeeCompanyTextView = (TextView) itemView.findViewById(R.id.employeeCompanyTextView);
        }
    }

    // Used for searches of employees
    @Override
    public Filter getFilter() {
        if(employeeFilter == null)
            employeeFilter = new EmployeeFilter(this, employees);
        return employeeFilter;
    }


    private static class EmployeeFilter extends Filter {

        private final EmployeeRecycleViewAdapter adapter;

        private final List<Employee> originalList;

        private final List<Employee> filteredList;

        private EmployeeFilter(EmployeeRecycleViewAdapter adapter, List<Employee> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase();

                // Check if there is any employee that has the given name or company
                for (final Employee employee : originalList) {
                    String employeeString = employee.getEmployeeFirstName().toLowerCase() + " " + employee.getEmployeeLastName().toLowerCase() + " " + employee.getEmployeeCompany().toLowerCase();
                    if (employeeString.contains(filterPattern)) {
                        filteredList.add(employee);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.employees.clear();
            adapter.employees.addAll((ArrayList<Employee>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}

