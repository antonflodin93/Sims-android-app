package se.miun.android_app.Service;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
import se.miun.android_app.Model.Employee;
import se.miun.android_app.Model.Message;;

public class RegularMessageService extends Service {
    private static final String TAG = "MessageService";
    public static final String BROADCAST_ACTION = "com.websmithing.broadcasttest.displayevent";
    private final Handler handler = new Handler();
    private Retrofit retrofit;
    private ApiInterface apiInterface;
    private ArrayList<Message> messages = new ArrayList<>();
    private Call<List<Message>> call;
    Intent intent;
    int counter = 0;
    private String company;
    private int employeeId;

    private Callback<ArrayList<Message>> messageCallback = new Callback<ArrayList<Message>>() {

        @Override
        public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
            // Check if user is matched in the database
            if (response.code() == 200) {
                Log.d("MessageService", "got messages");
                messages = response.body();
                Retrofit retrofit;
                retrofit = ApiClient.getApiClient();
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                Call<ArrayList<Message>> callcompany = null;
                callcompany = apiInterface.getCompanyMessage(company);
                callcompany.enqueue(messageCompanyCallback);
            }
        }

        @Override
        public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
        }
    };


    private Callback<ArrayList<Message>> messageCompanyCallback = new Callback<ArrayList<Message>>() {

        @Override
        public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
            // Check if user is matched in the database
            if (response.code() == 200) {
                if(response.body() != null){
                    messages.addAll(response.body());
                    Retrofit retrofit;
                    retrofit = ApiClient.getApiClient();
                    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                    Call<ArrayList<Message>> callEmployee = null;
                    callEmployee = apiInterface.getEmployeeMessage(employeeId);
                    callEmployee.enqueue(messageEmployeeCallback);
                }

            }
        }

        @Override
        public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
        }
    };

    private Callback<ArrayList<Message>> messageEmployeeCallback = new Callback<ArrayList<Message>>() {

        @Override
        public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
            // Check if user is matched in the database
            if (response.code() == 200) {
                if(response.body() != null){
                    messages.addAll(response.body());

                }

            }
            DisplayLoggingInfo();
        }

        @Override
        public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
        }
    };


    // Execute every 5 seconds
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            //MessageService.this.DisplayLoggingInfo();
            // Get messages
            Retrofit retrofit;
            retrofit = ApiClient.getApiClient();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            Call<ArrayList<Message>> call = null;
            call = apiInterface.getRegularMessages();
            call.enqueue(messageCallback);
            RegularMessageService.this.handler.postDelayed(this, 5000);
        }
    };


    public RegularMessageService() {
    }

    public void onCreate() {
        super.onCreate();
        this.intent = new Intent("RegularMessageService");
    }

    public void onStart(Intent intent, int startId) {
        employeeId = intent.getIntExtra("employeeId", 0);
        getEmployeeCompany();
    }

    private void DisplayLoggingInfo() {
        Log.d("MessageService", "entered DisplayLoggingInfo");
        this.intent.putExtra("messages", messages);
        this.sendBroadcast(this.intent);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        this.handler.removeCallbacks(this.sendUpdatesToUI);
        super.onDestroy();
    }

    void getEmployeeCompany(){
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<Employee> call;
        call = apiInterface.getEmployeeById(employeeId);
        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.code() == 200) {
                    company = response.body().getEmployeeCompany();
                    Log.d("MYTEST", company);
                    RegularMessageService.this.handler.removeCallbacks(RegularMessageService.this.sendUpdatesToUI);
                    RegularMessageService.this.handler.postDelayed(RegularMessageService.this.sendUpdatesToUI, 1000L);

                } else {
                    try {
                        Toast.makeText(getApplicationContext(), "Error: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}