package se.miun.android_app;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

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
        holder.Id.setText(employees.get(position).getId());
        holder.Name.setText(employees.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Id, Name;
        public MyViewHolder(View itemView) {
            super(itemView);
            Id = (TextView)itemView.findViewById(R.id.id);
            Name = (TextView)itemView.findViewById(R.id.name);

        }
    }

}
