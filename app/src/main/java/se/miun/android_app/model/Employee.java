package se.miun.android_app.model;


import com.google.gson.annotations.SerializedName;

public class Employee {

    @SerializedName("employeeId")
    private String Id;

    @SerializedName("employeeFirstName")
    private String FirstName;

    @SerializedName("employeeLastName")
    private String LastName;

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getId() {
        return Id;
    }


}
