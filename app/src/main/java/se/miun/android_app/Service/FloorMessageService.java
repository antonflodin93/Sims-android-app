package se.miun.android_app.Service;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
import se.miun.android_app.Model.Message;

public class FloorMessageService extends Service {
    private final Handler handler = new Handler();
    private Retrofit retrofit;
    private ApiInterface apiInterface;
    private ArrayList<Message> messages = new ArrayList<>();
    private Call<List<Message>> call;
    private int floorId, employeeId;
    Intent intent;
    int counter = 0;
    private Callback<ArrayList<Message>> messageCallback = new Callback<ArrayList<Message>>() {

        @Override
        public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
            if (response.code() == 200) {
                messages = response.body();
                DisplayLoggingInfo();
            }
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
            call = apiInterface.getFloorWarningMessage(floorId, employeeId);
            call.enqueue(messageCallback);
            FloorMessageService.this.handler.postDelayed(this, 5000);
        }
    };

    public FloorMessageService() {
    }

    public void onCreate() {
        super.onCreate();
        this.intent = new Intent("FloorMessageService");

    }

    public void onStart(Intent intent, int startId) {
        floorId = intent.getIntExtra("floorId", 0);
        employeeId = intent.getIntExtra("employeeId", 0);
        this.handler.removeCallbacks(this.sendUpdatesToUI);
        this.handler.postDelayed(this.sendUpdatesToUI, 1000L);
    }

    private void DisplayLoggingInfo() {
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
}
