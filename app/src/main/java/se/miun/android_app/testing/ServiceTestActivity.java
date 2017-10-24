package se.miun.android_app.testing;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import java.util.ArrayList;

import se.miun.android_app.R;
import se.miun.android_app.Service.RegularMessageService;
import se.miun.android_app.model.Message;

public class ServiceTestActivity extends Activity {

    private static final String TAG = "BroadcastTest";
    private Intent intent;
    private ArrayList<Message> messages;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            ServiceTestActivity.this.updateUI(intent);
        }
    };

    public ServiceTestActivity() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_service_test);
        this.intent = new Intent(this, RegularMessageService.class);
    }

    public void onResume() {
        super.onResume();
        this.startService(this.intent);
        this.registerReceiver(this.broadcastReceiver, new IntentFilter("com.websmithing.broadcasttest.displayevent"));
    }

    public void onPause() {
        super.onPause();
        this.unregisterReceiver(this.broadcastReceiver);
        this.stopService(this.intent);
    }

    private void updateUI(Intent intent) {
        String counter = intent.getStringExtra("counter");
        String time = intent.getStringExtra("time");
        messages = (ArrayList<Message>) intent.getSerializableExtra("messages");
        TextView messageTextView = (TextView)this.findViewById(R.id.messageTextView);
        TextView messageIdTextView = (TextView)this.findViewById(R.id.messageIdTextView);
        messageTextView.setText(messages.get(messages.size() - 1).getMessageText());
        messageIdTextView.setText(messages.get(messages.size() - 1).getMessageId());
    }
}