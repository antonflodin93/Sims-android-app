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
