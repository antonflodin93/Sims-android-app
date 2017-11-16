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


    public FactoryObject() {
    }

    public FactoryObject(int objectId, String objectName, int objectFloorId) {
        this.objectId = objectId;
        this.objectName = objectName;
        this.objectFloorId = objectFloorId;
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
}
