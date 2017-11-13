package se.miun.android_app.MasterUnit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ListBuildingsInfoActivity extends AppCompatActivity {
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
                    Toast.makeText(context, "Size: " + response.body().size(), Toast.LENGTH_SHORT).show();
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
}
