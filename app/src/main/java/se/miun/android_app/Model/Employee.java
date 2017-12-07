package se.miun.android_app.Model;


import com.google.gson.annotations.SerializedName;

public class Employee {

    @SerializedName("employeeId")
    private String employeeId;

    @SerializedName("employeeFirstName")
    private String employeeFirstName;

    @SerializedName("employeeLastName")
    private String employeeLastName;

    @SerializedName("employeeUsername")
    private String employeeUsername;

    @SerializedName("employeeEmail")
    private String employeeEmail;

    @SerializedName("employeePassword")
    private String employeePassword;

    @SerializedName("employeePhonenumber")
    private String employeePhonenumber;

    @SerializedName("employeeCompany")
    private String employeeCompany;

    @SerializedName("acknowledgeCurrentMillis")
    private Long acknowledgeCurrentMillis;

    private boolean acknowledged;

    private int floorId;

    private int buildingId;


    public Employee(String employeeFirstName, String employeeLastName, String employeeUsername,
                    String employeeEmail, String employeePassword, String employeePhonenumber, String employeeCompany) {
        this.employeeFirstName = employeeFirstName;
        this.employeeLastName = employeeLastName;
        this.employeeUsername = employeeUsername;
        this.employeeEmail = employeeEmail;
        this.employeePassword = employeePassword;
        this.employeePhonenumber = employeePhonenumber;
        this.employeeCompany = employeeCompany;
    }

    public Employee(String employeeFirstName, String employeeLastName, String employeeUsername,
                    String employeeEmail, String employeePhonenumber, String employeeCompany) {
        this.employeeFirstName = employeeFirstName;
        this.employeeLastName = employeeLastName;
        this.employeeUsername = employeeUsername;
        this.employeeEmail = employeeEmail;
        this.employeePhonenumber = employeePhonenumber;
        this.employeeCompany = employeeCompany;
    }


    public Employee(String employeeFirstName, String employeeLastName, String employeeCompany) {
        this.employeeFirstName = employeeFirstName;
        this.employeeLastName = employeeLastName;
        this.employeeCompany = employeeCompany;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public void setEmployeeUsername(String employeeUsername) {
        this.employeeUsername = employeeUsername;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }

    public void setEmployeePhonenumber(String employeePhonenumber) {
        this.employeePhonenumber = employeePhonenumber;
    }

    public void setEmployeeCompany(String employeeCompany) {
        this.employeeCompany = employeeCompany;
    }

    public Long getAcknowledgeCurrentMillis() {
        return acknowledgeCurrentMillis;
    }

    public void setAcknowledgeCurrentMillis(Long acknowledgeCurrentMillis) {
        this.acknowledgeCurrentMillis = acknowledgeCurrentMillis;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public String getEmployeeUsername() {
        return employeeUsername;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public String getEmployeePassword() {
        return employeePassword;
    }

    public String getEmployeePhonenumber() {
        return employeePhonenumber;
    }

    public String getEmployeeCompany() {
        return employeeCompany;
    }
}
