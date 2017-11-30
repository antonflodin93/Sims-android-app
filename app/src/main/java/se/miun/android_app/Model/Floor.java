package se.miun.android_app.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class Floor implements Serializable {

    @SerializedName("floorId")
    private int floorId;

    @SerializedName("floorLevel")
    private String floorLevel;

    @SerializedName("floorPlanFilePath")
    private String floorPlanFilePath;

    @SerializedName("objects")
    private ArrayList<FactoryObject> objects;


    @SerializedName("numOfEmployees")
    private int numOfEmployees;



    public Floor(int floorId, String floorLevel, String floorPlanFilePath, int numOfEmployees) {
        this.floorId = floorId;
        this.floorLevel = floorLevel;
        this.floorPlanFilePath = floorPlanFilePath;
        this.numOfEmployees = numOfEmployees;
    }

    public Floor(String floorLevel, String floorPlanFilePath) {
        this.floorLevel = floorLevel;
        this.floorPlanFilePath = floorPlanFilePath;
    }

    public Floor() {
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public void setFloorLevel(String floorLevel) {
        this.floorLevel = floorLevel;
    }

    public String getFloorLevel() {
        return floorLevel;
    }

    public void setFloorNumber(String floorLevel) {
        this.floorLevel = floorLevel;
    }

    public String getFloorPlanFilePath() {
        return floorPlanFilePath;
    }

    public void setFloorPlanFilePath(String floorPlanFilePath) {
        this.floorPlanFilePath = floorPlanFilePath;
    }

    public ArrayList<FactoryObject> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<FactoryObject> objects) {
        this.objects = objects;
    }

    public int getNumOfEmployees() {
        return numOfEmployees;
    }

    public void setNumOfEmployees(int numOfEmployees) {
        this.numOfEmployees = numOfEmployees;
    }
}
