package se.miun.android_app.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anton on 2017-11-03.
 */

public class Company {

    @SerializedName("companyName")
    private String companyName;


    public Company(){    }

    public Company(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
