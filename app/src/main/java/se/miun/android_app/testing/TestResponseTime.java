package se.miun.android_app.testing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
import se.miun.android_app.EmployeeUnit.EmployeeUnitActivity;
import se.miun.android_app.MasterUnit.MasterUnitActivity;
import se.miun.android_app.R;

public class TestResponseTime extends AppCompatActivity implements View.OnClickListener{
    private Button getResponseTimeButton;
    private TextView displayResponseTime;
    Context context;
    long start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_response_time);

        displayResponseTime = (TextView) findViewById(R.id.displayResponseTime);
        getResponseTimeButton = (Button) findViewById(R.id.getResponseTimeButton);
        getResponseTimeButton.setOnClickListener(this);
        context = this;
        displayResponseTime.setText("HEj");

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.getResponseTimeButton){
            getResponseTimeButton.setEnabled(false);
            start = System.currentTimeMillis();

            // Login the user
            Retrofit retrofit;
            retrofit = ApiClient.getApiClient();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            // Combine username and password with : as delimiter
            String concatedUserPassword = "employee" + ":" + "employee";

            // Create the basic authentication header
            String authorizedHeader = "Basic " + Base64.encodeToString(concatedUserPassword.getBytes(), Base64.NO_WRAP);
            Call<ResponseBody> call = null;

            call = apiInterface.loginAsMaster(authorizedHeader);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    long end = System.currentTimeMillis();
                    displayResponseTime.setText(" " + (end-start));
                    getResponseTimeButton.setEnabled(true);
                    end = start = 0;

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}
