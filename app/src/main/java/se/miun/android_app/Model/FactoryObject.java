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

    @SerializedName("xstart")
    private int xStart;

    @SerializedName("xend")
    private int xEnd;

    @SerializedName("ystart")
    private int  yStart;

    @SerializedName("yend")
    private int yEnd;


    public FactoryObject() {
    }


    public FactoryObject(int objectId, String objectName, int objectFloorId, int xStart, int xEnd, int yStart, int yEnd) {
        this.objectId = objectId;
        this.objectName = objectName;
        this.objectFloorId = objectFloorId;
        this.xStart = xStart;
        this.xEnd = xEnd;
        this.yStart = yStart;
        this.yEnd = yEnd;
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

    public int getxStart() {
        return xStart;
    }

    public void setxStart(int xStart) {
        this.xStart = xStart;
    }

    public int getxEnd() {
        return xEnd;
    }

    public void setxEnd(int xEnd) {
        this.xEnd = xEnd;
    }

    public int getyStart() {
        return yStart;
    }

    public void setyStart(int yStart) {
        this.yStart = yStart;
    }

    public int getyEnd() {
        return yEnd;
    }

    public void setyEnd(int yEnd) {
        this.yEnd = yEnd;
    }
}
