package se.miun.android_app.Model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class FactoryObject implements Serializable {

    @SerializedName("objectId")
    private int objectId;

    @SerializedName("objectName")
    private String objectName;

    @SerializedName("objectFloorId")
    private int objectFloorId;

    @SerializedName("areaXStart")
    private int areaXStart;

    @SerializedName("areaXEnd")
    private int areaXEnd;

    @SerializedName("areaYStart")
    private int  areaYStart;

    @SerializedName("areaYEnd")
    private int areaYEnd;





    public FactoryObject() {
    }


    public FactoryObject(int objectId, String objectName, int objectFloorId, int areaXStart, int areaXEnd, int areaYStart, int areaYEnd) {
        this.objectId = objectId;
        this.objectName = objectName;
        this.objectFloorId = objectFloorId;
        this.areaXStart = areaXStart;
        this.areaXEnd = areaXEnd;
        this.areaYStart = areaYStart;
        this.areaYEnd = areaYEnd;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public int getObjectFloorId() {
        return objectFloorId;
    }

    public void setObjectFloorId(int objectFloorId) {
        this.objectFloorId = objectFloorId;
    }

    public int getAreaXStart() {
        return areaXStart;
    }

    public void setAreaXStart(int areaXStart) {
        this.areaXStart = areaXStart;
    }

    public int getAreaXEnd() {
        return areaXEnd;
    }

    public void setAreaXEnd(int areaXEnd) {
        this.areaXEnd = areaXEnd;
    }

    public int getAreaYStart() {
        return areaYStart;
    }

    public void setAreaYStart(int areaYStart) {
        this.areaYStart = areaYStart;
    }

    public int getAreaYEnd() {
        return areaYEnd;
    }

    public void setAreaYEnd(int areaYEnd) {
        this.areaYEnd = areaYEnd;
    }
}
