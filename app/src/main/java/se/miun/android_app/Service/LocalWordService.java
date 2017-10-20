package se.miun.android_app.Service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import se.miun.android_app.Api.ApiClient;
import se.miun.android_app.Api.ApiInterface;
import se.miun.android_app.model.Message;

public class LocalWordService extends Service {
    private final IBinder mBinder = new MyBinder();
    private List<String> messages = new ArrayList<String>();
    private int counter = 1;
    private List<Message> receivedMessages;
    private Callback<List<Message>> messageCallback = new Callback<List<Message>>() {

        @Override
        public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
            // Check if user is matched in the database
            if (response.code() == 200) {
                Log.i("myapp", "got messages");
                messages.clear();
                receivedMessages = response.body();
                for(Message m : receivedMessages){
                    messages.add(m.getMessageText() + " " + m.getMessageId());
                }
            }
        }
        @Override
        public void onFailure(Call<List<Message>> call, Throwable t) {
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("myapp", "onstartcommand");
        getMessages();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("myapp", "onbind");
        getMessages();
        return mBinder;
    }

    public class MyBinder extends Binder {
        public LocalWordService getService() {
            return LocalWordService.this;
        }
    }

    public List<String> getWordList() {
        Log.i("myapp", "getwordlist");
        getMessages();
        return messages;
    }

    public int getWordListSize(){
        return messages.size();
    }

    private void getMessages() {
        Retrofit retrofit;
        retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<Message>> call = null;
        call = apiInterface.getMessages();
        call.enqueue(messageCallback);
        Log.i("myapp", "getMessages()");
    }
}