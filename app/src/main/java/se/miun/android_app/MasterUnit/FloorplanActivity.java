package se.miun.android_app.MasterUnit;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.miun.android_app.Adapter.ObjectAdapter;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
import se.miun.android_app.Model.Building;
import se.miun.android_app.Model.Employee;
import se.miun.android_app.Model.FactoryObject;
import se.miun.android_app.Model.Floor;
import se.miun.android_app.R;

public class FloorplanActivity extends Activity implements View.OnClickListener{
    private ImageView floorplanImageView, floorplanImageViewDrawed;
    private TextView textViewFloorPlan;
    private LinearLayout floorplanLinearLayout;
    private String filePath;
    private Context context;
    private int numofemployees = 0;
    private Building building;
    private Floor floor;
    private ArrayList<FactoryObject> objects;
    private ObjectAdapter objectAdapter;
    private Button listObjectsBtn, listEmployeesBtn;
    private ListView objectListView;
    private int HTTP_RESPONSE_ACCEPTED = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floorplan);
        context = this;
        filePath = getIntent().getStringExtra("filePath");
        building = (Building) getIntent().getSerializableExtra("building");
        floor = (Floor) getIntent().getSerializableExtra("floor");

        // Get objects for the floorplan
        objects = floor.getObjects();

        textViewFloorPlan = (TextView) findViewById(R.id.textViewFloorPlan);
        floorplanLinearLayout = (LinearLayout) findViewById(R.id.floorplanLinearLayout);
        textViewFloorPlan.setText(building.getBuildingName() + "/" + floor.getFloorLevel() + " (" + floor.getNumOfEmployees() + " employee(s))");
        floorplanImageView = new FloorplanImageView(context, filePath, objects, floor.getFloorId());
        floorplanLinearLayout.addView(floorplanImageView);
        listObjectsBtn = (Button) findViewById(R.id.listObjectsBtn);
        listObjectsBtn.setOnClickListener(this);
        listEmployeesBtn = (Button) findViewById(R.id.listEmployeesBtn);
        listEmployeesBtn.setOnClickListener(this);





    }


    @Override
    public void onClick(View v) {
        // List objects in floor
        if(v.getId() == R.id.listObjectsBtn){

            // Display dialog listing objects
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
            builderSingle.setCancelable(false);
            builderSingle.setIcon(android.R.drawable.ic_dialog_info);
            builderSingle.setTitle("Objects in " + building.getBuildingName() + "/" + floor.getFloorLevel());

            objectAdapter= new ObjectAdapter (FloorplanActivity.this, 0, objects);
            //objectListView.setAdapter(objectAdapter);

            builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });


            // When an object is clicked
            builderSingle.setAdapter(objectAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Remove the old imageview and create a new instance of it passing the choosen object
                    floorplanLinearLayout.removeView(floorplanImageView);
                    floorplanImageView = new FloorplanImageView(context, filePath, objects, building.getBuildingId(), objectAdapter.getItem(which));
                    floorplanLinearLayout.addView(floorplanImageView);

                }
            });
            builderSingle.show();

            // List employees in floor
        } else if(v.getId() == R.id.listEmployeesBtn){



            Retrofit retrofit;
            retrofit = ApiClient.getApiClient();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            Call<ArrayList<Employee>> call;


            call = apiInterface.getEmployeesInFloor(floor.getFloorId());
            call.enqueue(new Callback<ArrayList<Employee>>() {
                @Override
                    public void onResponse(Call<ArrayList<Employee>> call, Response<ArrayList<Employee>> response) {
                        if (response.code() == HTTP_RESPONSE_ACCEPTED) {
                            ArrayList<Employee> employees = response.body();

                            // Display dialog
                            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                            dialog.setIcon(android.R.drawable.ic_dialog_info);
                            dialog.setTitle("Information about employees");

                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
                            if(employees.isEmpty()){
                                arrayAdapter.add("No employees in floor");
                            } else{
                                for(Employee employee : employees){
                                    arrayAdapter.add(employee.getEmployeeFirstName() + " " + employee.getEmployeeLastName() + " (" + employee.getEmployeeCompany() + ") \n" + employee.getEmployeePhonenumber());
                                }
                            }


                            dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialog.show();



                        } else{
                            try {
                                Toast.makeText(context, "Error: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Employee>> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        }
    }
}
