package se.miun.android_app.MasterUnit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.miun.android_app.Adapter.BuildingListAdapter;
import se.miun.android_app.Adapter.EmployeeRecycleViewAdapter;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
import se.miun.android_app.Model.Building;
import se.miun.android_app.Model.Employee;
import se.miun.android_app.R;

public class ListBuildingsInfoActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener, ExpandableListView.OnItemLongClickListener {
    private ExpandableListView buildingsExListView;
    private BuildingListAdapter buildingListAdapter;
    private ArrayList<Building> buildings;
    private Context context;
    private int HTTP_RESPONSE_ACCEPTED = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_buildings_info);
        context = this;


        buildingsExListView = (ExpandableListView) findViewById(R.id.buildingsExListView);
        buildingsExListView.setOnChildClickListener(this);
        buildingsExListView.setOnItemLongClickListener(this);

        // Get all the buildings
        getBuildings();

    }


    @Override
    protected void onResume() {
        super.onResume();
        getBuildings();
    }

    private void getBuildings() {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ArrayList<Building>> call;
        call = apiInterface.getAllBuildings();
        call.enqueue(new Callback<ArrayList<Building>>() {
            @Override
            public void onResponse(Call<ArrayList<Building>> call, Response<ArrayList<Building>> response) {
                if (response.code() == HTTP_RESPONSE_ACCEPTED) {
                    buildings = response.body();
                    buildingListAdapter = new BuildingListAdapter(context, buildings);
                    buildingsExListView.setAdapter(buildingListAdapter);
                } else{
                    try {
                        Toast.makeText(context, "Error: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Building>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);

        builder.setTitle("Open floorplan")
                .setMessage("Do you want to open floorplan " + buildingListAdapter.getGroup(groupPosition).getBuildingName() + "/" + buildingListAdapter.getChild(groupPosition, childPosition).getFloorLevel() + "?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent = new Intent(getApplicationContext(), FloorplanActivity.class);
                        myIntent.putExtra("filePath", buildingListAdapter.getChild(groupPosition, childPosition).getFloorPlanFilePath());
                        myIntent.putExtra("building", buildingListAdapter.getGroup(groupPosition).getBuildingName());
                        myIntent.putExtra("floor", buildingListAdapter.getChild(groupPosition, childPosition));

                        context.startActivity(myIntent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();




        return false;
    }



    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        long packedPosition = buildingsExListView.getExpandableListPosition(position);

        int itemType = ExpandableListView.getPackedPositionType(packedPosition);
        int buildingPos = ExpandableListView.getPackedPositionGroup(packedPosition);
        int floorPos = ExpandableListView.getPackedPositionChild(packedPosition);

        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ArrayList<Employee>> call;

        // If building clicked
        if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            call = apiInterface.getEmployeesInBuilding(buildingListAdapter.getGroup(buildingPos).getBuildingId());
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
                        for(Employee employee : employees){
                            arrayAdapter.add(employee.getEmployeeFirstName() + " " + employee.getEmployeeLastName() + " (" + employee.getEmployeeCompany() + ") \n" + employee.getEmployeePhonenumber());
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

        // If floor clicked
        else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {

            call = apiInterface.getEmployeesInFloor(buildingListAdapter.getChild(buildingPos, floorPos).getFloorId());
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
                        for(Employee employee : employees){
                            arrayAdapter.add(employee.getEmployeeFirstName() + " " + employee.getEmployeeLastName() + " (" + employee.getEmployeeCompany() + ") \n" + employee.getEmployeePhonenumber());
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



        return true;
    }
}
