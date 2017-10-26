package se.miun.android_app.Service;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
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
    private Callback<ArrayList<Message>> messageCallback = new Callback<ArrayList<Message>>() {

        @Override
        public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
            // Check if user is matched in the database
            if (response.code() == 200) {
                Log.d("MessageService", "got messages");
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
        this.handler.removeCallbacks(this.sendUpdatesToUI);
        this.handler.postDelayed(this.sendUpdatesToUI, 1000L);
    }

    private void DisplayLoggingInfo() {
        Log.d("MessageService", "entered DisplayLoggingInfo");
        this.intent.putExtra("messages", messages);
        //this.intent.putExtra("time", (new Date()).toLocaleString());
        //this.intent.putExtra("counter", String.valueOf(++this.counter));
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