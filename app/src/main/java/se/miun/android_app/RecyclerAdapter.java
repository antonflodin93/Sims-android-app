package se.miun.android_app;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.miun.android_app.model.Employee;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    // Stores the employees
    private List<Employee> employees;

    public RecyclerAdapter(List<Employee> employees){

        this.employees = employees;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.id.setText(employees.get(position).getId());
        holder.name.setText(employees.get(position).getFirstName() + " " + employees.get(position).getLastName());
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, name;
        public MyViewHolder(View itemView) {
            super(itemView);

            // Init the fields for each row
            id = (TextView)itemView.findViewById(R.id.id);
            name = (TextView)itemView.findViewById(R.id.name);



        }
    }

}
