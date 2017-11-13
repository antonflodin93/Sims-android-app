package se.miun.android_app.Adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.miun.android_app.Model.Building;
import se.miun.android_app.Model.Floor;
import se.miun.android_app.R;

public class BuildingListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Building> buildings;
    private int numOfEmployees = 0;

    public BuildingListAdapter(Context context, ArrayList<Building> buildings) {
        this.context = context;
        this.buildings = buildings;

    }

    @Override
    public Floor getChild(int groupPosition, int childPosititon) {
        Floor floor = buildings.get(groupPosition).getFloors().get(childPosititon);
        return floor;
        /*
        return this.floorplanList.get(this.buildingList.get(groupPosition))
                .get(childPosititon);
                */
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // Gets the floorplan
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = getChild(groupPosition, childPosition).getFloorLevel();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_floorplan, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.floorplanTextView);

        txtListChild.setText(childText + " (" + numOfEmployees + ")");
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int numOfFloors = buildings.get(groupPosition).getFloors().size();
        return numOfFloors;
        /*


        return this.floorplanList.get(this.buildingList.get(groupPosition))
                .size();
         */
    }

    @Override
    public Building getGroup(int groupPosition) {
        return this.buildings.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.buildings.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // Gets the building
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition).getBuildingName();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_buildings, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.buildingNameTextView);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}