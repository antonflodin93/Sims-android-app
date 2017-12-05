package se.miun.android_app.Model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Building implements Serializable {

    @SerializedName("buildingId")
    private int buildingId;

    @SerializedName("buildingName")
    private String buildingName;

    @SerializedName("numOfEmployees")
    private int numOfEmployees;

    @SerializedName("floors")
    private ArrayList<Floor> floors;

    public Building() {
    }


    public Building(int buildingId, String buildingName, ArrayList<Floor> floors, int numOfEmployees) {
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.floors = floors;
        this.numOfEmployees = numOfEmployees;
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

    public int getNumOfEmployees() {
        return numOfEmployees;
    }

    public void setNumOfEmployees(int numOfEmployees) {
        this.numOfEmployees = numOfEmployees;
    }
}
