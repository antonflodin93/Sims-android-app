package se.miun.android_app.MasterUnit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

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

public class ListBuildingsInfoActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener {
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

        // Get all the buildings
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
                        myIntent.putExtra("floor", buildingListAdapter.getChild(groupPosition, childPosition).getFloorLevel());

                        context.startActivity(myIntent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();




        return true;
    }
}
