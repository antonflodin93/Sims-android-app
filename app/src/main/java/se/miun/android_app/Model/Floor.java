package se.miun.android_app.Model;

import com.google.gson.annotations.SerializedName;


public class Floor {

    @SerializedName("floorId")
    private int floorId;

    @SerializedName("floorLevel")
    private String floorLevel;

    @SerializedName("floorPlanFilePath")
    private String floorPlanFilePath;

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
}
