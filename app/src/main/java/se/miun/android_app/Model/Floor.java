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

    public Floor(int floorId, String floorLevel, String floorPlanFilePath) {
        this.floorId = floorId;
        this.floorLevel = floorLevel;
        this.floorPlanFilePath = floorPlanFilePath;
    }

    public Floor(String floorLevel, String floorPlanFilePath) {
        this.floorLevel = floorLevel;
        this.floorPlanFilePath = floorPlanFilePath;
    }

    public Floor() {
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
}
