package se.miun.android_app.Adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import se.miun.android_app.R;

public class BuildingListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> buildingList;
    private HashMap<String, List<String>> floorplanList;

    public BuildingListAdapter(Context context, List<String> buildingList,
                               HashMap<String, List<String>> floorplanList) {
        this.context = context;
        this.buildingList = buildingList;
        this.floorplanList = floorplanList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.floorplanList.get(this.buildingList.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // Gets the floorplan
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_floorplan, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.floorplanTextView);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.floorplanList.get(this.buildingList.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.buildingList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.buildingList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // Gets the building
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
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