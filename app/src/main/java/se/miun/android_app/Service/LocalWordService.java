package se.miun.android_app.Service;


import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
import se.miun.android_app.EmployeeUnit.EmployeeUnitActivity;
import se.miun.android_app.MasterUnit.MasterUnitActivity;
import se.miun.android_app.model.Message;

public class LocalWordService extends Service {
    private final IBinder mBinder = new MyBinder();
    private List<String> resultList = new ArrayList<String>();
    private int counter = 1;
    List<Message> messages;
    private Callback<List<Message>> messageCallback = new Callback<List<Message>>() {

        @Override
        public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
            // Check if user is matched in the database
            if (response.code() == 200) {
                messages = response.body();

                resultList.add(messages.get(0).getMessageText() + " " + counter++);

                //resultList.add(messages.get(0).getMessageText() + " " + counter++);

            } else {

            }
        }

        @Override
        public void onFailure(Call<List<Message>> call, Throwable t) {

        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        addResultValues();
        Log.i("myapp", "Started service");
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        addResultValues();
        return mBinder;
    }

    public class MyBinder extends Binder {
        public LocalWordService getService() {
            return LocalWordService.this;
        }
    }

    public List<String> getWordList() {
        return resultList;
    }

    private void addResultValues() {

        // Login the user
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<Message>> call = null;


        call = apiInterface.getMessages();


        call.enqueue(messageCallback);
        //resultList.add(input.get(random.nextInt(3)) + " " + counter++);
        if (counter == Integer.MAX_VALUE) {
            counter = 0;
        }
    }
}