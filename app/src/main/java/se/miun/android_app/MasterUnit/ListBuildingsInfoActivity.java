package se.miun.android_app.MasterUnit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.miun.android_app.Adapter.BuildingListAdapter;
import se.miun.android_app.R;

public class ListBuildingsInfoActivity extends AppCompatActivity {
    private ExpandableListView buildingsExListView;
    private BuildingListAdapter buildingListAdapter;
    private ArrayList<String> buildingList;
    private HashMap<String, List<String>> floorplanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_buildings_info);


        buildingsExListView = (ExpandableListView) findViewById(R.id.buildingsExListView);

        getBuildings();

    }

    private void getBuildings() {
        buildingList = new ArrayList<String>();
        floorplanList = new HashMap<String, List<String>>();

        buildingList.add("Building 1");
        buildingList.add("Building 2");
        buildingList.add("Building 3");

        List<String> floorplansBuilding1 = new ArrayList<String>();
        floorplansBuilding1.add("Floor 1");
        floorplansBuilding1.add("Floor 2");
        floorplansBuilding1.add("Floor 3");

        List<String> floorplansBuilding2 = new ArrayList<String>();
        floorplansBuilding2.add("Floor 1");
        floorplansBuilding2.add("Floor 2");

        List<String> floorplansBuilding3 = new ArrayList<String>();
        floorplansBuilding3.add("Floor 1");
        floorplansBuilding3.add("Floor 2");
        floorplansBuilding3.add("Floor 3");
        floorplansBuilding3.add("Floor 4");

        floorplanList.put(buildingList.get(0), floorplansBuilding1);
        floorplanList.put(buildingList.get(1), floorplansBuilding2);
        floorplanList.put(buildingList.get(2), floorplansBuilding3);

        buildingListAdapter = new BuildingListAdapter(this, buildingList, floorplanList);


        buildingsExListView.setAdapter(buildingListAdapter);

    }
}
