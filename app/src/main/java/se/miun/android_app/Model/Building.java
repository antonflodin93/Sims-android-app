package se.miun.android_app.Model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Building {

    @SerializedName("buildingId")
    private int buildingId;

    @SerializedName("buildingName")
    private String buildingName;

    @SerializedName("floors")
    private ArrayList<Floor> floors;

    public Building() {
    }


    public Building(int buildingId, String buildingName, ArrayList<Floor> floors) {
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.floors = floors;
    }

    public Building(String buildingName) {
        this.buildingName = buildingName;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public ArrayList<Floor> getFloors() {
        return floors;
    }

    public void setFloors(ArrayList<Floor> floors) {
        this.floors = floors;
    }
}
