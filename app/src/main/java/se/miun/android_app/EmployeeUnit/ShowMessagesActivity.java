package se.miun.android_app.EmployeeUnit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import se.miun.android_app.Adapter.MessageAdapter;
import se.miun.android_app.Adapter.RecyclerAdapter;
import se.miun.android_app.R;
import se.miun.android_app.EmployeeUnit.EmployeeUnitActivity.MessageType;
import se.miun.android_app.Service.MessageService;
import se.miun.android_app.model.Message;
import se.miun.android_app.testing.ServiceTestActivity;

public class ShowMessagesActivity extends AppCompatActivity {

    private static final String TAG = "BroadcastTest";
    private Intent intent;
    private ArrayList<Message> messages;
    private RecyclerView messageRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MessageAdapter adapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            ShowMessagesActivity.this.updateUI(intent);
        }
    };

    public ShowMessagesActivity() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_show_messages);
        this.intent = new Intent(this, MessageService.class);

        messageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        messageRecyclerView.setLayoutManager(layoutManager);
        messageRecyclerView.setHasFixedSize(true);


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
        messages = (ArrayList<Message>) intent.getSerializableExtra("messages");
        adapter = new MessageAdapter(messages);
        messageRecyclerView.setAdapter(adapter);
    }
}
