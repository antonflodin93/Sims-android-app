package se.miun.android_app;


import com.google.gson.annotations.SerializedName;

public class Employee {

    @SerializedName("employeeId")
    private String Id;

    @SerializedName("employeeName")
    private String Name;

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

}
